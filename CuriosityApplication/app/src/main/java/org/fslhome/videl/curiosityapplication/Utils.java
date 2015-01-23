package org.fslhome.videl.curiosityapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

/**
 * Created by videl on 22/01/15.
 */
public final class Utils {
    /**
     * Compatibility method.
     *
     * @param view
     *            the view to set the background on
     * @param background
     *            the background
     */
    @TargetApi(16)
    public static void setBackground(View view, Drawable background) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }
    static Bitmap viewToBitmap(Context c, View view) {
        view.measure(View.MeasureSpec.getSize(view.getMeasuredWidth()),
                View.MeasureSpec.getSize(view.getMeasuredHeight()));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Drawable drawable = new BitmapDrawable(c.getResources(),
                android.graphics.Bitmap.createBitmap(view.getDrawingCache()));
        view.setDrawingCacheEnabled(false);
        return AndroidGraphicFactory.convertToBitmap(drawable);
    }
}
