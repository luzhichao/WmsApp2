package com.linkingwin.education.studyonline.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


/**
 * @Desc
 * @Author Willowgao
 * @Version V1.0
 * @Date 2018/7/12
 */

public class PermissionHelper {


    /**
     * 存储
     */
    public static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;

    /**
     * 定位
     */
    public static final int ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE = 0x01;

    /**
     * 定位
     */
    public static final int ACCESS_FINE_CAMERA_REQUEST_CODE = 0x02;


    private static Activity getActivity(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else {
            return null;
        }
    }


    /**
     * 定位权限
     */
    public static void checkLocationPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(context), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_COMMANDS_REQUEST_CODE);
        }
    }

    /**
     * 定位权限
     */
    public static void cameraPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(context), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACCESS_FINE_CAMERA_REQUEST_CODE);
        }
    }


}
