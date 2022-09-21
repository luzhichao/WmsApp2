package com.linkingwin.education.studyonline.common.base.loader;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.linkingwin.education.studyonline.R;
import com.youth.banner.loader.ImageLoader;

public class GlideLoader extends ImageLoader {

    public static GlideLoader newInstance() {
        return new GlideLoader();
    }


    public void displayImage(Context context, Object path, ImageView imageView) {
        //设置加载中占位符旋转动画
        final ObjectAnimator anim = ObjectAnimator.ofInt(imageView, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();
        //加载图片
        Glide.with(context)
                .load(path)
                .placeholder(R.drawable.rotate_pro)       //加载中占位符
                .fallback(R.drawable.global_img_default)  //为空占位符
                .error(R.drawable.global_img_default)     //错误占位符
                .centerCrop()
                .into(imageView);
    }
}
