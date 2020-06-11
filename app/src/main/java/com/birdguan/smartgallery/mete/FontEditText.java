package com.birdguan.smartgallery.mete;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * @Author: birdguan
 * @Date: 2020/6/8 21:14
 */
public class FontEditText extends AppCompatEditText {
    private static final String TAG = "SmartGallery/FontEditText";
    public FontEditText(Context context) {
        super(context);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Bitmap getCacheBitmapFromView() {
        final boolean drawingCacheEnabled = true;
        this.setDrawingCacheEnabled(drawingCacheEnabled);
        this.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = this.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            this.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }
}
