package com.linkingwin.education.studyonline.common.base.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestTracker;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.loader.GlideLoader;

import org.xutils.x;

import java.io.InputStream;

/**
 * 使用GlideLoader加载图片到对应的ImageView组件上
 */
public class GlideImageView {

    private DrawableRequestBuilder drawableRequestBuilder;
    private ImageView imageView;

    public GlideImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public GlideImageView drawing(String imageURI) {
        //设置加载中占位符旋转动画
        final ObjectAnimator anim = ObjectAnimator.ofInt(imageView, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();
        drawableRequestBuilder = Glide.with(x.app())
                .load(imageURI)
                .placeholder(R.drawable.rotate_pro)       //加载中占位符
                .fallback(R.drawable.global_img_default)  //为空占位符
                .error(R.drawable.global_img_default)     //错误占位符
                .centerCrop();
        return this;
    }

    public DrawableRequestBuilder getDrawableRequestBuilder() {
        return drawableRequestBuilder;
    }

    public void display() {
        drawableRequestBuilder.into(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
