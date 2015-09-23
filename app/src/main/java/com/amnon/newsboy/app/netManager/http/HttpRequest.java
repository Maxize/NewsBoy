package com.amnon.newsboy.app.netManager.http;

import android.util.Log;

import com.amnon.newsboy.app.utils.FileUtil;
import com.amnon.newsboy.app.utils.ImageUtil;
import com.amnon.newsboy.app.utils.OkHttpUtil;
import com.amnon.newsboy.app.utils.SdcardUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.amnon.newsboy.app.utils.Md5;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * httpRequest action
 * more use look here https://github.com/square/okhttp/wiki/Recipes
 * or 中文翻译 http://www.cnblogs.com/ct2011/p/3997368.html
 * 这个类 有一些使用了 volley的请求，有一些又是直接使用 okHttp 请求的，
 * 至于要使用哪个，看君口味
 *
 * @author Amnon Ma
 */
public class HttpRequest {

    public final static String TAG = "HttpRequest";

    public static void testDownloadImage() {
        List<String> imageUrls = Arrays.asList
                (
                        "https://lh3.googleusercontent.com/X7LeBu-pcZT072M2mtywDQWCKuqTMjdCWrrAaofQI7_6=w950-h713-no",
                        "https://lh3.googleusercontent.com/srl6bOcG8KT0SdlEtkgxvGJOjKh22kWLBLrS25McOUsm=w950-h713-no",
                        "https://lh3.googleusercontent.com/uOZbJGuX8Ut5j-Yw2IfzaCe30rdrbD93fmZI1bRLRHR7=w950-h706-no",
                        "https://lh3.googleusercontent.com/X2g2LKEruoxITTE1hXG5Lp3tJALhptCDp0gKZ932SWwt=w950-h713-no",
                        "https://lh3.googleusercontent.com/EuGPCu0gYpWneIYHFayUDcNK7qrifvWwIYYoFMFu6TP3=w950-h713-no",
                        "https://lh3.googleusercontent.com/7QQGcjlShMdu7sXchzLUexsA8BScXnOe82baYOysAZmk=w950-h713-no",
                        "https://lh3.googleusercontent.com/5H2ql2TAP0dw_U2kNEP9__nbyBrbvX9Lek0qW3i2K-rF=w950-h713-no",
                        "https://lh3.googleusercontent.com/6iA-Q8DyXPOIASEJWMBU1szFzd9isijp5HbDhB17-Q-O=w950-h713-no"
                );
        for (final String imageUrl : imageUrls) {
            final String imgName = Md5.hash(imageUrl);
            String imgFullPath = SdcardUtil.getImagePath() + imgName + ImageUtil.PNG_SUFFIX;
            if (FileUtil.isFileExist(imgFullPath)) {
                return;
            }
            Request request = new Request.Builder()
                    .url(imageUrl)
                    .build();
            OkHttpUtil.enqueue(request, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.w(TAG, imageUrl + " download image error!");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    InputStream inputStream = response.body().byteStream();
                    FileUtil.write2SDFromInput(SdcardUtil.getImagePath(), imgName + ImageUtil.PNG_SUFFIX, inputStream);
                    Log.d(TAG, imgName + " download success");
                }
            });
        }
    }

    /**
     * 通过图片地址 下载图片
     *
     * @param imageUrl 图片地址
     */
    public void downloadImage(final String imageUrl) {
        final String imgName = Md5.hash(imageUrl);
        if (imageUrl != null) {
            Request request = new Request.Builder()
                    .url(imageUrl)
                    .build();
            OkHttpUtil.enqueue(request, new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.w(TAG, "download image error!");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    InputStream inputStream = response.body().byteStream();
                    FileUtil.write2SDFromInput(SdcardUtil.getImagePath(), imgName + ImageUtil.PNG_SUFFIX, inputStream);

                }
            });
        }

    }

    /**
     * 上传图片到服务器
     *
     * @param urlStr   服务器地址
     * @param filePath 文件地址
     * @param api      api 由 lua端传过来
     * @return 结果字符串
     * @throws Exception
     */
    public static String uploadImage(String urlStr, String filePath, String api) throws Exception {
        File file = new File(filePath);
        final MediaType mediaType = MediaType.parse("image/*");
        Log.d(TAG, "uploadIcon --> url= " + urlStr + ", file = " + filePath + ", api = " + api);
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("api", api)
                .addFormDataPart("icon", "icon.jpg",
                        RequestBody.create(mediaType, file))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        Response response = OkHttpUtil.execute(request);
        return response.body().string();
    }

    /**
     * 异步上传图片到服务器
     *
     * @param urlStr   服务器地址
     * @param filePath 文件地址
     * @param api      api 由 lua端传过来
     * @param callback 回调
     * @throws Exception
     */
    public static void uploadImage(String urlStr, String filePath, String api, Callback callback) throws Exception {
        File file = new File(filePath);
        final MediaType mediaType = MediaType.parse("image/*");
        Log.d(TAG, "uploadIcon --> url= " + urlStr + ", file = " + filePath + ", api = " + api);
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("api", api)
                .addFormDataPart("icon", "icon.jpg",
                        RequestBody.create(mediaType, file))
                .build();

        Request request = new Request.Builder()
                .url(urlStr)
                .post(requestBody)
                .build();

        OkHttpUtil.enqueue(request, callback);
    }

    /**
     * 根据 地址下载xml
     *
     * @param xmlUrl xml地址
     * @return 返回获取的数据
     * @throws Exception
     */
    public static String downloadXml(String xmlUrl) throws Exception {
        Log.d(TAG, "downloadXml --> xmlUrl= " + xmlUrl);
        return OkHttpUtil.getStringFromServer(xmlUrl);
    }
}
