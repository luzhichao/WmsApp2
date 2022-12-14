package com.linkingwin.elearn.common.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * 第三方插件，展示banner
 */
public class GlideImageLoader extends ImageLoader {


    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }
}
