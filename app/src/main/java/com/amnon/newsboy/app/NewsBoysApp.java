package com.amnon.newsboy.app;

import android.app.Application;

/**
 * Created by Amnon on 2015/9/16.
 */
public class NewsBoysApp extends Application {

    private static NewsBoysApp mNewsBoysApp = null;

    public static NewsBoysApp getApplication() {
        return mNewsBoysApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNewsBoysApp = this;
    }
}
