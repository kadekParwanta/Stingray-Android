package com.stingray.stingrayandroid.bookshelf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.stingray.stingrayandroid.Model.Yearbook;
import com.stingray.stingrayandroid.R;
import com.stingray.stingrayandroid.bookshelf.view.ImageLoader;

import java.util.List;

public class BooksAdapter extends BaseAdapter {

	private Context context;
	private final List<Yearbook> books;
	LayoutInflater inflater;
	ImageLoader mImageLoader;

	public BooksAdapter(Context context, List<Yearbook> books) {

		this.context = context;
		this.books = books;
	}

	@Override
	public Yearbook getItem(int position) {
		return books.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return books.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View gridView = null;
		// gridView = new View(context);
		if (convertView == null) {
			// BookDto book = books.get(position);
			// if (book != null) {
			// if (convertView == null) {

			convertView = inflater.inflate(R.layout.item, null);
			// try {
			// Bitmap icon = BitmapFactory.decodeStream((new
			// URL(books.get(position)
			// .getBook_cover_image())).openConnection().getInputStream());
			// TextView textView = (TextView)
			// convertView.findViewById(R.id.grid_item_label);
			// textView.setText(books.get(position).getBook_title());
			// get layout from mobile.xml
			System.out.println("Image Thumb Url===>"
					+ books.get(position).getCoverUrl().toString());
			ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_cover);
			// mImageLoader = new ImageLoader(context, false,
			// R.drawable.ic_launcher);
			mImageLoader = new ImageLoader(context, 180, 180, false, R.drawable.placeholder);
			mImageLoader.DisplayImage(books.get(position).getCoverUrl(), imageView);
			// imageView.setImageBitmap(icon);
			// } catch (MalformedURLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }

			// ImageView imageView = (ImageView)
			// gridView.findViewById(R.id.grid_item_new);
			// imageView.setVisibility(book.isNewItem() ? View.VISIBLE :
			// View.INVISIBLE);

			// } else {
			// gridView = (View) convertView;
			// }}

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String toast = "Sekolah==" + books.get(position).getSchool().getName()
							+ "\n Angkatan==>" + books.get(position).getYear()
							+ "\n Epub Url==>" + books.get(position).getEpubUrl();
					System.out.println(toast);
					Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();

				}
			});
		}
		return convertView;
	}

}
