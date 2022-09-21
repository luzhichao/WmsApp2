package com.linkingwin.elearn.common.util;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jaiky.imagespickers.ImageLoader;
import com.linkingwin.elearn.R;

/**
 * 第三方选择照片插件
 */
public class GlideLoader implements ImageLoader {
    @Override
    public void displayImage(final Context context, final String path, final ImageView imageView) {
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
//                .listener(new RequestListener<String, Bitmap>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        if (imageView == null) {
//                            return false;
//                        }
//
//                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
//                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
//                        float scale = (float) vw / (float) resource.getWidth();
//                        int vh = Math.round(resource.getHeight() * scale);
//                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
//                        imageView.setLayoutParams(params);
//
//                        return false;
//                    }
//                })
//                .thumbnail( 0.1f )
                .into(imageView);

    }
}
