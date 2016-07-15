package com.stingray.stingrayandroid.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.etsy.android.grid.util.DynamicHeightImageView;

/**
 * Created by Kadek_P on 7/15/2016.
 */
public class CustomDynamicHeightImage extends DynamicHeightImageView {

    public CustomDynamicHeightImage(Context context) {
        super(context);
    }

    public CustomDynamicHeightImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static float radius = 2.0f;

    Path clipPath = new Path();
    RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());

    @Override
    protected void onDraw(Canvas canvas) {
        rect.left = 0;
        rect.top = 0;
        rect.right = this.getWidth();
        rect.bottom = this.getHeight();
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
