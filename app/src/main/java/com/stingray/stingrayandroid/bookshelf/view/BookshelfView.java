package com.stingray.stingrayandroid.bookshelf.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;

import com.stingray.stingrayandroid.R;

public class BookshelfView extends GridView {

	private Bitmap background;

	public BookshelfView(Context context) {
		super(context);
		init();
	}

	public BookshelfView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BookshelfView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	protected void init() {
		//Set the background image of the ShelfView panel.
		background = BitmapFactory.decodeResource(getResources(), R.drawable.shelf_panel_new);
	}

	//Draw a background in the screen and create multiple panels using height & width.
	@Override
	protected void dispatchDraw(Canvas canvas) {
		int top = getChildCount() > 0 ? getChildAt(0).getTop() : 0;
		for (int y = top; y < getHeight(); y += background.getHeight()) {
			for (int x = 0; x < getWidth(); x += background.getWidth()) {
				canvas.drawBitmap(background, x, y, null);
			}
		}
		super.dispatchDraw(canvas);
	}
}
