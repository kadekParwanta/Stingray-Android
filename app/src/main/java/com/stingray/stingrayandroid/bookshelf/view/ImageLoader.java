package com.stingray.stingrayandroid.bookshelf.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;

import com.stingray.stingrayandroid.Helper.FileCache;
import com.stingray.stingrayandroid.Helper.MemoryCache;
import com.stingray.stingrayandroid.Helper.Utils;

public class ImageLoader {

	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService executorService;
	private int bmpWidth = 0;
	private int bmpHeight = 0;
	private Boolean scaled = false;
	private Handler handler = new Handler();// handler to display images in UI
											// thread

	 private int stub_id = 0;

	public ImageLoader(Context context, int bmpWidth, int bmpHeight, Boolean scaled, int default_img) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		this.bmpWidth = bmpWidth;
		this.bmpHeight = bmpHeight;
		this.scaled = scaled;
		this.stub_id = default_img;
	}

	public ImageLoader(Context context,Boolean scaled, int default_img) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);		
		this.scaled = scaled;
		 this.stub_id = default_img;
	}
	@SuppressWarnings("deprecation")
	public void DisplayImage(String url, ImageView imageView) {
		
		try{
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);

		if (bitmap != null) {
			if (scaled) {
				imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			} else {
				imageView.setImageBitmap(bitmap);
			}
		} else {
			queuePhoto(url, imageView);
			if (scaled) {
				imageView.setBackgroundResource(stub_id);
				
			} else {
				imageView.setImageResource(stub_id);
			}
		}
		
		}catch (OutOfMemoryError e)
        {        
	           e.printStackTrace();
	           System.gc();
	           memoryCache.clear();
	        }
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
				System.gc();
			
			return null;
		}
	}

	/**
	 * This function use for get Bitmap from Sdcard
	 * 
	 * @param ImagePath
	 *            for string image path
	 * @param context
	 *            Application or current activity reference
	 * @param Width
	 *            for image width set
	 * @param Height
	 *            for image height set
	 * @return Bitmap
	 */
	public static Bitmap DecodeFileGetBitmap(File mFile, int Width, int Height) {
		try {
			if (mFile.exists()) {

				BitmapFactory.Options mBitmapFactoryOptions = new BitmapFactory.Options();
				mBitmapFactoryOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(mFile.getAbsolutePath().toString(), mBitmapFactoryOptions);
				int SampleSize = calculateInSampleSize(mBitmapFactoryOptions, Width, Height);
				mBitmapFactoryOptions.inJustDecodeBounds = false;
				mBitmapFactoryOptions.inScaled = false;
				mBitmapFactoryOptions.inSampleSize = SampleSize;
				 mBitmapFactoryOptions.inPurgeable = true;
				// return BitmapFactory.decodeStream(new
				// FileInputStream(mFile),null,mBitmapFactoryOptions);

				return BitmapFactory.decodeFile(mFile.getAbsolutePath().toString(), mBitmapFactoryOptions);
			} else {
				// Log.w(Constant.TAG,"ImagePath Not Found.");
				// return BitmapFactory.decodeResource(context.getResources(),
				// R.drawable.glow_img);
				return null;
			}

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			// return BitmapFactory.decodeResource(context.getResources(),
			// R.drawable.glow_img);
			System.gc();
			
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			// return BitmapFactory.decodeResource(context.getResources(),
			// R.drawable.glow_img);
			return null;
		}
	}

	/**
	 * This function use for calculateSampleSize
	 * 
	 * @param
	 * @return int
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		// try {
		// //decode image size
		// BitmapFactory.Options o = new BitmapFactory.Options();
		// o.inJustDecodeBounds = true;
		// o.inDither=false; //Disable Dithering mode
		// o.inPurgeable=true; //Tell to gc that whether it needs free memory,
		// the Bitmap can be cleared
		// o.inInputShareable=true; //Which kind of reference will be used to
		// recover the Bitmap data after being clear, when it will be used in
		// the future
		// o.inTempStorage=new byte[32 * 1024];
		// FileInputStream stream1=new FileInputStream(f);
		// BitmapFactory.decodeStream(stream1,null,o);
		// stream1.close();
		//
		// //Find the correct scale value. It should be the power of 2.
		// int width_tmp=o.outWidth, height_tmp=o.outHeight;
		// int scale=1;
		// while (true) {
		// if (width_tmp / 2 < bmpWidth|| height_tmp / 2 < bmpHeight)
		// break;
		// width_tmp /= 2;
		// height_tmp /= 2;
		// scale *= 2;
		// }
		//
		// //decode with inSampleSize
		// BitmapFactory.Options o2 = new BitmapFactory.Options();
		// o2.inDither=false; //Disable Dithering mode
		// o2.inPurgeable=true; //Tell to gc that whether it needs free memory,
		// the Bitmap can be cleared
		// o2.inInputShareable=true; //Which kind of reference will be used to
		// recover the Bitmap data after being clear, when it will be used in
		// the future
		// o2.inTempStorage=new byte[32 * 1024];
		//
		//
		// o2.inSampleSize=scale;
		// FileInputStream stream2=new FileInputStream(f);
		// Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
		//
		//
		// stream2.close();
		// return bitmap;
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// catch (Exception e) {
		// e.printStackTrace();
		// }
		// return null;
		return DecodeFileGetBitmap(f, bmpWidth, bmpHeight);
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		@SuppressWarnings("deprecation")
		public void run() {
			try{
			
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				if (scaled) {
					photoToLoad.imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
				} else {
					photoToLoad.imageView.setImageBitmap(bitmap);
				}
			else {
				
				if (scaled) {
					photoToLoad.imageView.setBackgroundResource(stub_id);
					
				} else {
					photoToLoad.imageView.setImageResource(stub_id);
				}
			}
			
			}catch (OutOfMemoryError e)
	        {        
 	           e.printStackTrace();
 	           System.gc();
 	          memoryCache.clear();
 	        }catch(Exception e){
				e.printStackTrace();
			}
			// photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}

/*package com.kataria.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.kataria.R;

public class ImageLoader {
    
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler=new Handler();//handler to display images in UI thread
    
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }
    
    final int stub_id=R.drawable.stub;
    public void DisplayImage(String url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }
        
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    private Bitmap getBitmap(String url) 
    {
        File f=fileCache.getFile(url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex){
           ex.printStackTrace();
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
            try{
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}*/

/*package com.svg.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;

public class ImageLoader {

	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	private ExecutorService executorService;
	private int bmpWidth = 0;
	private int bmpHeight = 0;
	private Boolean scaled = false;
	private Handler handler = new Handler();// handler to display images in UI
											// thread

	 private int stub_id = 0;

	public ImageLoader(Context context, int bmpWidth, int bmpHeight, Boolean scaled, int default_img) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		this.bmpWidth = bmpWidth;
		this.bmpHeight = bmpHeight;
		this.scaled = scaled;
		 this.stub_id = default_img;
	}

	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);

		if (bitmap != null) {
			if (scaled) {
				imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			} else {
				imageView.setImageBitmap(bitmap);
			}
		} else {
			queuePhoto(url, imageView);
			if (scaled) {
				imageView.setBackgroundResource(stub_id);
				
			} else {
				imageView.setImageResource(stub_id);
			}
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			
			return null;
		}
	}

	*//**
	 * This function use for get Bitmap from Sdcard
	 * 
	 * @param ImagePath
	 *            for string image path
	 * @param context
	 *            Application or current activity reference
	 * @param Width
	 *            for image width set
	 * @param Height
	 *            for image height set
	 * @return Bitmap
	 *//*
	public static Bitmap DecodeFileGetBitmap(File mFile, int Width, int Height) {
		try {
			if (mFile.exists()) {

				BitmapFactory.Options mBitmapFactoryOptions = new BitmapFactory.Options();
				mBitmapFactoryOptions.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(mFile.getAbsolutePath().toString(), mBitmapFactoryOptions);
				int SampleSize = calculateInSampleSize(mBitmapFactoryOptions, Width, Height);
				mBitmapFactoryOptions.inJustDecodeBounds = false;
				mBitmapFactoryOptions.inScaled = false;
				mBitmapFactoryOptions.inSampleSize = SampleSize;
				 mBitmapFactoryOptions.inPurgeable = true;
				// return BitmapFactory.decodeStream(new
				// FileInputStream(mFile),null,mBitmapFactoryOptions);

				return BitmapFactory.decodeFile(mFile.getAbsolutePath().toString(), mBitmapFactoryOptions);
			} else {
				// Log.w(Constant.TAG,"ImagePath Not Found.");
				// return BitmapFactory.decodeResource(context.getResources(),
				// R.drawable.glow_img);
				return null;
			}

		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			// return BitmapFactory.decodeResource(context.getResources(),
			// R.drawable.glow_img);
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			// return BitmapFactory.decodeResource(context.getResources(),
			// R.drawable.glow_img);
			return null;
		}
	}

	*//**
	 * This function use for calculateSampleSize
	 * 
	 * @param
	 * @return int
	 *//*
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		// try {
		// //decode image size
		// BitmapFactory.Options o = new BitmapFactory.Options();
		// o.inJustDecodeBounds = true;
		// o.inDither=false; //Disable Dithering mode
		// o.inPurgeable=true; //Tell to gc that whether it needs free memory,
		// the Bitmap can be cleared
		// o.inInputShareable=true; //Which kind of reference will be used to
		// recover the Bitmap data after being clear, when it will be used in
		// the future
		// o.inTempStorage=new byte[32 * 1024];
		// FileInputStream stream1=new FileInputStream(f);
		// BitmapFactory.decodeStream(stream1,null,o);
		// stream1.close();
		//
		// //Find the correct scale value. It should be the power of 2.
		// int width_tmp=o.outWidth, height_tmp=o.outHeight;
		// int scale=1;
		// while (true) {
		// if (width_tmp / 2 < bmpWidth|| height_tmp / 2 < bmpHeight)
		// break;
		// width_tmp /= 2;
		// height_tmp /= 2;
		// scale *= 2;
		// }
		//
		// //decode with inSampleSize
		// BitmapFactory.Options o2 = new BitmapFactory.Options();
		// o2.inDither=false; //Disable Dithering mode
		// o2.inPurgeable=true; //Tell to gc that whether it needs free memory,
		// the Bitmap can be cleared
		// o2.inInputShareable=true; //Which kind of reference will be used to
		// recover the Bitmap data after being clear, when it will be used in
		// the future
		// o2.inTempStorage=new byte[32 * 1024];
		//
		//
		// o2.inSampleSize=scale;
		// FileInputStream stream2=new FileInputStream(f);
		// Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
		//
		//
		// stream2.close();
		// return bitmap;
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// catch (Exception e) {
		// e.printStackTrace();
		// }
		// return null;
		return DecodeFileGetBitmap(f, bmpWidth, bmpHeight);
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				if (scaled) {
					photoToLoad.imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
				} else {
					photoToLoad.imageView.setImageBitmap(bitmap);
				}
			else {
				
				if (scaled) {
					photoToLoad.imageView.setBackgroundResource(stub_id);
					
				} else {
					photoToLoad.imageView.setImageResource(stub_id);
				}
			}
			// photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}

package com.kataria.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.kataria.R;

public class ImageLoader {
    
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler=new Handler();//handler to display images in UI thread
    
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }
    
    final int stub_id=R.drawable.stub;
    public void DisplayImage(String url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }
        
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }
    
    private Bitmap getBitmap(String url) 
    {
        File f=fileCache.getFile(url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex){
           ex.printStackTrace();
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
            try{
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}*/