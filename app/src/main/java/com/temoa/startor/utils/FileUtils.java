package com.temoa.startor.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件操作工具类
 * Created by Temoa
 * on 2016/9/6 20:35
 */
public class FileUtils {

    /**
     * 需要权限
     * 存储卡读取(READ_EXTERNAL_STORAGE)
     * 存储卡写入(WRITE_EXTERNAL_STORAGE)
     */
    private FileUtils() {

    }

    /**
     * 程序系统文目录
     */
    public static String getFileDir(Context context) {
        return String.valueOf(context.getFilesDir());
    }

    /**
     * 程序系统文件目录绝对路径
     */
    public static String getFileDir(Context context, String path) {
        String targetPath = context.getFilesDir() + formatPath(path);
        mkDir(targetPath);
        return targetPath;
    }

    /**
     * 程序系统缓存目录
     */
    public static String getCacheDir(Context context) {
        return String.valueOf(context.getCacheDir());
    }

    /**
     * 程序系统缓存目录
     */
    public static String getCacheDir(Context context, String path) {
        String targetPath = context.getFilesDir() + formatPath(path);
        mkDir(targetPath);
        return targetPath;
    }

    /**
     * 内存卡文件目录
     */
    public static String getExternalFileDir(Context context) {
        return String.valueOf(context.getExternalFilesDir(""));
    }

    /**
     * 内存卡文件目录
     */
    public static String getExternalFileDir(Context context, String path) {
        String targetPath = context.getExternalFilesDir("") + formatPath(path);
        mkDir(targetPath);
        return targetPath;
    }

    /**
     * 内存卡缓存目录
     */
    public static String getExternalCacheDir(Context context) {
        return String.valueOf(context.getExternalCacheDir());
    }

    /**
     * 内存卡缓存目录
     */
    public static String getExternalCacheDir(Context context, String path) {
        String targetPath = context.getExternalCacheDir() + formatPath(path);
        mkDir(targetPath);
        return targetPath;
    }

    /**
     * 公共下载目录
     */
    public static String getPublicDownload() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath();
    }

    /**
     * 格式化文件路径
     *
     * @param path "filename" "/filename" "/filename/"
     * @return "/filename"
     */
    private static String formatPath(String path) {
        if (!path.startsWith("/"))
            path = "/" + path;
        while (path.endsWith("/"))
            path = new String(path.toCharArray(), 0, path.length() - 1);
        return path;
    }

    /**
     * @param filePath 文件路径
     * @return 获取文件对象
     */
    public static File getFile(String filePath) {
        File file = new File(filePath);
        if (file.exists())
            return file;
        return null;
    }

    public static boolean fileExists(String filePath) {
        return getFile(filePath) != null;
    }

    /**
     * 创建目录
     */
    public static void mkDir(String dirPath) {
        File file = new File(dirPath);
        if (!(file.exists() && file.isDirectory())) {
            boolean mkdirs = file.mkdirs();
            if (mkdirs) {
                LogUtils.i("file/path is make");
            }
        }
    }

    /**
     * 删除目标文件
     *
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            boolean delete = file.delete();
            if (delete) {
                LogUtils.i("file/path is delete");
            }
        }
    }

    /**
     * 内存卡是否内在
     */
    public static boolean isMountSdcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

}
