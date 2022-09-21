package com.linkingwin.elearn.common.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.iceteck.silicompressorr.VideoCompress;

import java.io.File;

/**
 * 文件工具类
 */
public class FileUtils {

    //{后缀名，MIME类型}
    private static final String[][] MIME_MapTable = {
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    /**
     * 打开文件
     * @param file
     * @param activity
     */
    private void openFile(File file, Activity activity) {
        try {
            Intent intent = new Intent();
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= 24){
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(activity, "com.babyinhand.fileprovider", file);
                intent.setDataAndType(contentUri, type);
            }else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //设置intent的data和Type属性。
                intent.setDataAndType(Uri.fromFile(file), type);
            }
            //跳转
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "不能打开视频文件", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取文件大小
     * @param path
     * @return
     */
    public static String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }

    /**
     * 文件压缩
     * @param inputPath     源文件路径
     * @param outputPath    压缩后路径
     */
    public synchronized static void compressFile(final String inputPath, final String outputPath, VideoView videoView){
        VideoCompress.compressVideoLow(inputPath, outputPath, new VideoCompress.CompressListener() {
            @Override
            public void onStart() {
                Log.i("视频压缩开始时间：", DateUtils.getFormatNowStr());
            }

            @Override
            public void onSuccess() {
                Log.i("视频压缩完成时间：", DateUtils.getFormatNowStr());
                Log.i("视频压缩压缩后大小：", getFileSize(outputPath));
                videoView.setVideoPath(outputPath);
            }

            @Override
            public void onFail() {
                Log.i("视频压缩失败时间：", DateUtils.getFormatNowStr());
            }

            @Override
            public void onProgress(float percent) {
                Log.i("视频压缩进度：", String.valueOf(percent) + "%");
            }
        });
    }

}
