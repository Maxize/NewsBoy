package com.amnon.newsboy.app.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.amnon.newsboy.app.NewsBoysApp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 文件管理类
 * Created by Amnon on 2015/9/19.
 */
public class FileUtil {
    private static String SDPath;
    private static String XML_DIR;

    /**
     *  获取 SD 目录
     * @return SD目录
     */
    public static String getSDPath() {
        if (isHasUseableSDCard()) {
            SDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;//获取根目录
        }
        return SDPath;
    }

    /**
     *  判断sd卡是否存在
     * @return sd 卡 是否存在
     */
    private static boolean isHasUseableSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     *  返回 SD 卡目录下的 xml 文件目录
     * @return SD 卡目录下的 xml 文件目录
     */
    public static String getXmlDir() {
        StringBuilder sb;
        if (XML_DIR == null) {
            sb = new StringBuilder();
            sb.append(getSDPath());
            sb.append(".");
            sb.append(NewsBoysApp.getApplication().getPackageName());
            sb.append("/xml/");
            XML_DIR = sb.toString();
        }
        return XML_DIR;
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file 文件
     * @return md5 值
     */
    public static String getFileMD5(File file) {
        if (file == null || !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取单个文件的内容
     *
     * @param file 文件
     * @return 文件的字符串
     */
    public static String getFile2Str(File file) {
        String content = "";
        if (file == null || !file.isFile()) {
            return content;
        }
        InputStream is;
        try {
            is = new FileInputStream(file);
            //读取数据的包装流
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //str用于读取一行数据
            String str;
            //StringBuffer用于存储所欲数据
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            content = sb.toString();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 创建文件
     *
     * @param fileName 文件名字
     * @return 文件 可为空
     * @throws IOException
     */
    public static File createFile(String fileName) throws IOException {
        File file = new File(fileName);
        Boolean isSuccess = file.createNewFile();
        if (isSuccess) {
            return file;
        }
        return null;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName 目录名字
     * @return 文件目录
     */
    public static boolean createSDDir(String dirName) {
        File dir = new File(dirName);
        return dir.mkdirs();
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            createSDDir(path);
            file = createFile(path + fileName);
            if (file != null) {
                output = new FileOutputStream(file);
                int data = input.read();
                while (data != -1) {
                    output.write(data);
                    data = input.read();
                }
                output.flush();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 删除单个文件
     *
     * @param delFilePath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String delFilePath) {
        boolean flag = false;
        File file = new File(delFilePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 对字符串进行压缩
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String gzipString(String str) throws IOException {
        if (TextUtils.isEmpty(str.trim())) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();

        return out.toString("ISO-8859-1");
    }

    /**
     * 解压 字符串
     *
     * @param str
     * @return 解压后的字符串
     * @throws IOException
     */
    public static String unGzipString(String str) throws IOException {
        if (null == str || "".equals(str.trim())) {
            return str;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        GZIPInputStream gzipInputStream = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gzipInputStream.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
        return out.toString();
    }

}

