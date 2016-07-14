package com.stingray.stingrayandroid.bookshelf.activity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CommonClass {
	
	private ConnectivityManager connectivity=null;
	private NetworkInfo netinfo=null;
	
	private static final int TIMEOUTCONNECTION = 85000;
	private static final int TIMEOUTSOCKET = 90000;


	public String getMd5Hash(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String md5 = number.toString(16);
			while (md5.length() < 32)
				md5 = "0" + md5;
			return md5;
		} catch (NoSuchAlgorithmException e) {
			Log.e("MD5", e.getLocalizedMessage());
			return null;
		}
	}	
	
	public boolean CheckNetwork(Context mContext) {
		    this.connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	        this.netinfo= connectivity.getActiveNetworkInfo();
	        if(netinfo!=null && netinfo.isConnectedOrConnecting()==true)
	        {	      
	        	return true;
	        }
	        else
	        {
//	        	Toast.makeText(mContext, "Network not available", Toast.LENGTH_LONG).show();
	        	return false;       	
	        }
		 
	}
	/**This method use for check Network Connectivity No Message
	 */
	public boolean CheckNetworkNoMessage(Context mContext) {
		    this.connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	        this.netinfo= connectivity.getActiveNetworkInfo();
	        if(netinfo!=null && netinfo.isConnected()==true)
	        {
	        	return true;
	        }
	        else
	        {
	        	return false;       	
	        }
	}

	/**This function use for check service running or not*/
	public static boolean IsServiceRunning(Context context,String package_class) {
	    ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (package_class.equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
    }

	/**This method use for PostConnection to Server
	 */
	public static void PrintLog(String WebserviceName,String response)
	{
		Log.i("TAG",WebserviceName+" : "+response);
	}

	/**This method use for GetConnection to Server
	 */
	/**This method use for GETHTTPConnection to Server
	 */
	public String GETHTTPConnection(String strUrl)
    {
		HttpURLConnection mHttpURLConnection=null;
        int intResponse = -1;
        try{
                URL mURL = new URL(strUrl);
                URLConnection mURLConnection = mURL.openConnection();

	            if (!(mURLConnection instanceof HttpURLConnection)) {
//	               	Log.e(Constant.TAG,"Error in HTTP connection Not Connect");
	                throw new IOException("Not an HTTP connection");
	            }

                mHttpURLConnection = (HttpURLConnection) mURLConnection;
                mHttpURLConnection.setRequestMethod("GET");
                mHttpURLConnection.setUseCaches (false);
                mHttpURLConnection.setDoInput(true);
                mHttpURLConnection.setDoOutput(true);
                mHttpURLConnection.setInstanceFollowRedirects(true);
                mHttpURLConnection.connect();
                intResponse = mHttpURLConnection.getResponseCode();

                if (intResponse == HttpURLConnection.HTTP_OK) {
                	return InputStreamToString(mHttpURLConnection.getInputStream());
                }
                else{
                	return null;
                }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	return null;
        }
        finally{
        	if(mHttpURLConnection!=null){
        		mHttpURLConnection.disconnect();
        	}
        }
    }

	/**This method use for POSTHTTPConnection to Server
	 */
	public String POSTHTTPConnection(String strUrl,String[] Key,String[] Value)
    {
		HttpURLConnection mHttpURLConnection=null;
        int intResponse = -1;
        try{
                URL mURL = new URL(strUrl);
                URLConnection mURLConnection = mURL.openConnection();

	            if (!(mURLConnection instanceof HttpURLConnection)) {
//	               	Log.e(Constant.TAG,"Error in HTTP connection Not Connect");
	                throw new IOException("Not an HTTP connection");
	            }

                mHttpURLConnection = (HttpURLConnection) mURLConnection;
                mHttpURLConnection.setRequestMethod("POST");
                mHttpURLConnection.setUseCaches (false);
                mHttpURLConnection.setDoInput(true);
                mHttpURLConnection.setDoOutput(true);
                mHttpURLConnection.setInstanceFollowRedirects(true);
                /**This comment code use if encode request UTF_8 format*/
                /*                String Query = String.format("version=%s", URLEncoder.encode("android", HTTP.UTF_8));
                                mHttpURLConnection.setRequestProperty("Accept-Charset", HTTP.UTF_8);
                                mHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" +  HTTP.UTF_8);
                                output.write(Query.getBytes(HTTP.UTF_8));*/

//                /**This code use for post request*/
                StringBuffer Query = new StringBuffer();
                if(Key!=null || Value!=null){
                	for(int i=0;i < Key.length;i++){
                		Query.append(String.format("%s=%s",Key[i],Value[i]).toString().trim());
                		if(i >= 0 && i < (Key.length -1) && Key.length > 1){
                			Query.append("&");
                		}
                	}
                }

                OutputStream mOutputStream = null;
                try {
                	 mOutputStream = mHttpURLConnection.getOutputStream();
                	 Log.i("Request = ", Query.toString());
                	 mOutputStream.write(Query.toString().getBytes());
                } finally {
                     if (mOutputStream != null) try { mOutputStream.close(); } catch (IOException logOrIgnore) {}
                }

                mHttpURLConnection.connect();
                intResponse = mHttpURLConnection.getResponseCode();

                if (intResponse == HttpURLConnection.HTTP_OK) {
                	return InputStreamToString(mHttpURLConnection.getInputStream());
                }
                else{
                	return null;
                }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	return null;
        }
        finally{
        	if(mHttpURLConnection!=null){
        		mHttpURLConnection.disconnect();
        	}
        }
    }

	/**This method use for convert InputStream To String
	 */
	private String InputStreamToString(InputStream mInputStream){
		 String strLine = null;
		//convert response in to the string.
		try {
			  if(mInputStream!=null){
				  BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream), 8);
				  StringBuilder mStringBuilder = new StringBuilder();
			  	  while((strLine = mBufferedReader.readLine()) != null)
			  	  {
			  		mStringBuilder.append(strLine);
				  }
			  	  mInputStream.close();
			  	  return mStringBuilder.toString();
			  }
			  else
			  {
				  return null;
			  }
		   }
		   catch (Exception e) {
			   e.printStackTrace();
		       return null;
		  }
	}

	/**This method used for opening Connection for Post method
	 */
	   public static InputStream OpenHttpConnectionPost(String strUrl)throws IOException
	   {
	       InputStream mInputStream = null;
	       int intResponse = -1;
	              
	       URL mURL = new URL(strUrl); 
	       URLConnection mURLConnection = mURL.openConnection();
	                
	       if (!(mURLConnection instanceof HttpURLConnection)) 
	       {           
	          	//Log.e(strTAG,"Error in HTTP connection Not Connect");
	       	System.out.println("Error in HTTP connection Not Connect");
	           throw new IOException("Not an HTTP connection");
	       }
	       try{
	           HttpURLConnection mHttpURLConnection = (HttpURLConnection) mURLConnection;
	           mHttpURLConnection.setAllowUserInteraction(false);
	           mHttpURLConnection.setInstanceFollowRedirects(true);
	           mHttpURLConnection.setRequestMethod("POST");
	           mHttpURLConnection.connect(); 
	           intResponse = mHttpURLConnection.getResponseCode();
	           
	           if (intResponse == HttpURLConnection.HTTP_OK) 
	           {
	        	   mInputStream = mHttpURLConnection.getInputStream();                                 
	           }                     
	       }
	       catch (Exception e)
	       {
	       	//Log.e(strTAG,"Error in Error connecting");
//	       	System.out.println("Error in Error connecting");
	           throw new IOException("Error connecting");            
	       }
	       return mInputStream;     
	   }
	
	 /**This method use to Check Memory Card
	 */  
    public Boolean MemoryCardCheck(Context context) {
		
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
    	{
    		return true;
    	}
    	else
    	{
    		Toast.makeText(context, "SD card not mounted", Toast.LENGTH_LONG).show();
    		return false;
    	}
	}
    
	 /**This method use to Check Memory Card
	 */  
   public Boolean MemoryCardCheck() {
	   	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
	   	{
	   		return true;
	   	}
	   	else
	   	{
	   		return false;
	   	}
	}

  /**This function use for calculateSampleSize
	  * @param 
	  * @return int
	  */   
  public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
  // Raw height and width of image
  final int height = options.outHeight;
  final int width = options.outWidth;
  int inSampleSize = 1;

  if (height > reqHeight || width > reqWidth) {

      // Calculate ratios of height and width to requested height and width
      final int heightRatio = Math.round((float) height / (float) reqHeight);
      final int widthRatio = Math.round((float) width / (float) reqWidth);

      // Choose the smallest ratio as inSampleSize value, this will guarantee
      // a final image with both dimensions larger than or equal to the
      // requested height and width.
      inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
  }

  return inSampleSize;
}
	 /**This method use to Write File String
	 */  
    public void WriteString(String FilePath,String Text){
    	try{
                File mFile=new File(FilePath);
                FileOutputStream mFileOutputStream = new FileOutputStream(mFile);
                OutputStreamWriter mOutputStreamWriter = new OutputStreamWriter(mFileOutputStream); 
                mOutputStreamWriter.write(Text);
                mOutputStreamWriter.flush();
                mOutputStreamWriter.close();
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
  
  /**This method is used for deleting file from Path from SD Card
 	 */
    public void DeleteFile_FromPath(String path,Context context)
    {
		File mFile = new File(path);
		if(MemoryCardCheck(context))
		{
			if (mFile.exists()) 
			{
				mFile.delete();
			}
		}
    }
    /**This method is used for converting binary number to six digit format by adding left bits
	 */
    
    public String ConvertToSixDigitFormat(double location)
    {
    	NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
		DecimalFormat df = (DecimalFormat)nf;
		df.applyPattern("00.000000");
		return (String.valueOf(df.format(location)));
    }
    
	public void sendMail(Context context, String subject, String bodytext, Uri mUri) {
		try {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			emailIntent.setType("text/html");
			emailIntent.putExtra(Intent.EXTRA_EMAIL, "support@cammio.com");
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			emailIntent.putExtra(Intent.EXTRA_TEXT, bodytext);
			emailIntent.putExtra(Intent.EXTRA_STREAM, mUri);
			context.startActivity(Intent.createChooser(emailIntent, "Send Mail..."));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//	public boolean copyFile(String from, String to) {
//		try {
//
//			System.out.println("from :" + from);
//			System.out.println("to :" + to);
//
//			int bytesum = 0;
//			int byteread = 0;
//			File oldfile = new File(from);
//			if (oldfile.exists()) {
//				InputStream inStream = new FileInputStream(from);
//				FileOutputStream fs = new FileOutputStream(to);
//				byte[] buffer = new byte[1444];
//				while ((byteread = inStream.read(buffer)) != -1) {
//					bytesum += byteread;
//					fs.write(buffer, 0, byteread);
//				}
//				inStream.close();
//				fs.close();
//			}
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//	public boolean copyFromAssets(Context context, String fileName, String to) {
//		try {
//
//			System.out.println("fileName :" + fileName);
//			System.out.println("to :" + to);
//
//			int bytesum = 0;
//			int byteread = 0;
//			InputStream inStream = context.getAssets().open(fileName);
//			FileOutputStream fs = new FileOutputStream(to);
//			byte[] buffer = new byte[1444];
//			while ((byteread = inStream.read(buffer)) != -1) {
//				bytesum += byteread;
//				fs.write(buffer, 0, byteread);
//			}
//			inStream.close();
//			fs.close();
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//	}
//	 /**This method is used for converting binary number to seven digit format by adding left bits
//	 */
//    
//    public String ConvertToSevenDigitFormat(String binary )
//    {
////    	Integer.toBinaryString(Integer.parseInt(binary));
//    	double binary_formate = Double.parseDouble(binary);
//    	
//    	NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
//		DecimalFormat df = (DecimalFormat)nf;
//		df.applyPattern("0000000");
//		return (String.valueOf(df.format(binary_formate)));
//    }
//	 /**This method is used for converting binary number to twenty four digit format by adding left bits
//	 */
//    public String ConvertToTwentyFourDigitFormat(String binary)
//    {
//    	double binary_formate = Double.parseDouble(binary);
//    	NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
//		DecimalFormat df = (DecimalFormat)nf;
//		df.applyPattern("000000000000000000000000");
//		return (String.valueOf(df.format(binary_formate)));
//    }
    
	public static String Convert_Decimal_To_Binary(String decimalString)
	{
		String binaryString = Integer.toBinaryString(Integer.parseInt(decimalString));
		return binaryString;
	}
	public static String ConvertTo24DigitFormate(String binaryString)
	{
		String TwentyFour_String = "000000000000000000000000";
		String convertedBinary = TwentyFour_String+binaryString;
		String Final_Binary_WithPadding = convertedBinary.substring((convertedBinary.length())-(TwentyFour_String.length()), convertedBinary.length());
		return Final_Binary_WithPadding;
	}
	public static String ConvertTo7DigitFormate(String binaryString)
	{
		String Seven_String = "0000000";
		String convertedBinary = Seven_String+binaryString;
		String Final_Binary_WithPadding = convertedBinary.substring((convertedBinary.length())-(Seven_String.length()), convertedBinary.length());
		return Final_Binary_WithPadding;
	}
	public static String getIMEINumber(Context context)
	{
	    TelephonyManager mTelephonyManager =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyManager.getDeviceId();
//		return "355856040355386";
	}
	public static String getHashValue_SHA512(String data)
	{
		StringBuffer hexString = new StringBuffer();
		try 
		{
			MessageDigest md = MessageDigest.getInstance("SHA-512");
	        md.update(data.getBytes());
	        byte byteData[] = md.digest();
	        
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	 
	        System.out.println("Hex format : " + sb.toString());
	 
	        //convert the byte to hex format method 2
	        
	    	for (int i=0;i<byteData.length;i++) 
	    	{
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
        } 
		catch (Exception e) 
		{
        	e.printStackTrace();
        }
		return hexString.toString();
	}
	public static String getHashValue_MD5(String data)
	{
		StringBuffer hexString = new StringBuffer();
		try 
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(data.getBytes());
	        byte byteData[] = md.digest();
	        
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	 
	        System.out.println("Hex format : " + sb.toString());
	 
	        //convert the byte to hex format method 2
	        
	    	for (int i=0;i<byteData.length;i++) 
	    	{
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
        } 
		catch (Exception e) 
		{
        	e.printStackTrace();
        }
		return hexString.toString();
	}

	
//	 /**
//     * 
//     * @param context
//     * @param object
//     * @param filename
//     */
//    public static void WiteObjectToFile(Context context, Object object, String filename) {
//        ObjectOutputStream objectOut = null;
//        try {
//            FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
//            objectOut = new ObjectOutputStream(fileOut);
//            objectOut.writeObject(object);
//            fileOut.getFD().sync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (objectOut != null) {
//                try {
//                    objectOut.close();
//                } catch (IOException e) {
//                    // do nowt
//                	e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    /**
//     * 
//     * @param context
//     * @param filename
//     * @return
//     */
//    public static Object ReadObjectFromFile(Context context, String filename) {
//        ObjectInputStream objectIn = null;
//        Object object = null;
//        try {
//            FileInputStream fileIn = context.getApplicationContext().openFileInput(filename);
//            objectIn = new ObjectInputStream(fileIn);
//            object = objectIn.readObject();
//        } catch (FileNotFoundException e) {
//            // Do nothing
//        	 e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            if (objectIn != null) {
//                try {
//                    objectIn.close();
//                } catch (IOException e) {
//                    // do nowt
//                	e.printStackTrace();
//                }
//            }
//        }
//        return object;
//    }
	
	public static int getDIP(Context context, float values){
		int DPI=context.getResources().getDisplayMetrics().densityDpi;
		if(DisplayMetrics.DENSITY_MEDIUM == DPI){
			return (int) (values * ((float)context.getResources().getDisplayMetrics().density) + 0.5f);
		}
		else if(DisplayMetrics.DENSITY_HIGH == DPI){
			return (int) (values * ((float)context.getResources().getDisplayMetrics().density) + 0.5f);
		}
		else{
			return (int) (values * ((float)context.getResources().getDisplayMetrics().density) + 0.5f);
		}
	 }

}