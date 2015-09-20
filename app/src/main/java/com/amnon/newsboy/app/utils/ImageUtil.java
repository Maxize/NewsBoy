package com.amnon.newsboy.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.io.InputStream;

/**
 * Created by Amnon on 2015/9/19.
 */
public class ImageUtil {

    public final static String PNG_SUFFIX = ".png";
    public final static String JPG_SUFFIX = ".jpg";

    public static Bitmap loadBitmap(InputStream inputStream) {
        return BitmapFactory.decodeStream(inputStream);
    }
}
