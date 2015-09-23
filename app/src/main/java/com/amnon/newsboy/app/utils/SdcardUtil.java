package com.amnon.newsboy.app.utils;

import android.text.TextUtils;
import android.util.Log;

import com.amnon.newsboy.app.NewsBoysApp;

import java.io.File;

/**
 *  sdcard 的一些 方法
 * Created by Amnon on 2015/9/19.
 */
public class SdcardUtil {
    private static final String TAG = "SdcardUtil";
    private static String mImagePath = "";
    private static String mPacketName = "";
    private static String mHidePacketName = "";
    private final static String sImagePathName = "images/";
    static {
        initPath();
    }

    /**
     *  初始化需要的文件夹
     */
    public static void initPath() {
        String hidePacketName = FileUtil.getSDPath() + getHidePacketName();
        if (!FileUtil.isFileExist(hidePacketName)) {
            boolean isSuccess = FileUtil.createSDDir(hidePacketName);
            if (!isSuccess) {
                Log.e(TAG, hidePacketName + "create error ");
            }
        }
        String imagePath = getImagePath();
        if (!FileUtil.isFileExist(imagePath)){
            boolean isSuccess = FileUtil.createSDDir(imagePath);
            if (!isSuccess) {
                Log.e(TAG, imagePath + "create error ");
            }
        }
    }

    /**
     *  程序的包名
     * @return 程序的包名
     */
    public static String getPacketName(){
        if (TextUtils.isEmpty(mPacketName)) {
            mPacketName = NewsBoysApp.getApplication().getPackageName();
        }
        return mPacketName;
    }

    /**
     *  获得隐藏的 包名文件名 带有盘符分割符号
     *  需自己加上 sdCard路径
     * @return 文件名
     */
    public static String getHidePacketName() {
        if (TextUtils.isEmpty(mHidePacketName)) {
            mHidePacketName = "." + getPacketName();
            mHidePacketName = mHidePacketName + File.separator;
        }
        return mHidePacketName;
    }

    /**
     *  获取 SD卡图片文件夹全路径
     * @return 图片文件夹路径
     */
    public static String getImagePath(){
        if (TextUtils.isEmpty(mImagePath)) {
            mImagePath = FileUtil.getSDPath() + getHidePacketName() + sImagePathName;
        }
        return mImagePath;
    }
}
