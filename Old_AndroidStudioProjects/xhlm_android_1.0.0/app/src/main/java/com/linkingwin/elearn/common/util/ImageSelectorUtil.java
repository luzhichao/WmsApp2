package com.linkingwin.elearn.common.util;

import android.app.Activity;
import android.widget.LinearLayout;

import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelector;
import com.linkingwin.elearn.R;

import java.util.ArrayList;

/**
 * 选择手机图片或拍照初始化方法
 */
public class ImageSelectorUtil {

    private static ImageConfig imageConfig;

    /**
     * 初始化单选图片选择,直接打开
     *
     *  @param requestCode
     * @param activity
     */
    public static ImageConfig initImageSelector(int requestCode, Activity activity) {
        imageConfig
                = new ImageConfig.Builder(new GlideLoader())
                .steepToolBarColor(activity.getResources().getColor(R.color.blue))
                .titleBgColor(activity.getResources().getColor(R.color.blue))
                .titleSubmitTextColor(activity.getResources().getColor(R.color.white))
                .titleTextColor(activity.getResources().getColor(R.color.white))
                //请求自定义code
                .requestCode(requestCode)
                // 开启单选   （默认为多选）
                .singleSelect()
                // 开启拍照功能 （默认关闭）
                .showCamera()
                .build();

        ImageSelector.open(activity, imageConfig);   // 开启图片选择器
        return imageConfig;
    }

    /**
     * 初始化多选图片选择
     *
     * @param activity
     * @param mutiNum
     * @param paths
     */
    public static ImageConfig initImageMutiSelector(int requestCode, Activity activity, int mutiNum, int rowImageCount, boolean isDelete, ArrayList<String> paths, LinearLayout llContainer) {
        imageConfig
                = new ImageConfig.Builder(new GlideLoader())
                .steepToolBarColor(activity.getResources().getColor(R.color.blue))
                .titleBgColor(activity.getResources().getColor(R.color.blue))
                .titleSubmitTextColor(activity.getResources().getColor(R.color.white))
                .titleTextColor(activity.getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）
                .mutiSelect()
                // 多选时的最大数量
                .mutiSelectMaxSize(mutiNum)
                // 已选择的图片路径
                .pathList(paths)
                // 开启拍照功能 （默认关闭）
                .showCamera()
                //设置图片显示容器，参数：、（容器，每行显示数量，是否可删除）
                .setContainer(llContainer, rowImageCount, isDelete)
                //请求自定义code
                .requestCode(requestCode)
                .crop()
                .build();

        return imageConfig;
    }

    /**
     * 打开照片选择
     * @param imageConfig
     * @param activity
     */
    public static void openMutiSelect(ImageConfig imageConfig, Activity activity){
        ImageSelector.open(activity, imageConfig);   // 开启图片选择器
    }

    /**
     * 显示图片
     *
     * @param paths
     */
    public static void show(ImageConfig config, ArrayList<String> paths){
        config.getContainerAdapter().refreshData(paths, config.getImageLoader());
    }
}
