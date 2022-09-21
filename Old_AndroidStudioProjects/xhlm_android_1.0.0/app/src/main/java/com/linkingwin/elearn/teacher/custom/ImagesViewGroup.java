package com.linkingwin.elearn.teacher.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.linkingwin.elearn.R;

import java.util.List;

public class ImagesViewGroup extends ViewGroup {
    private int maringTop = 12;
    private List<ImageInfo> imageInfoList;
    private Context context;
    private ClickListener clickListener;

    public ImagesViewGroup(Context context) {
        this(context, null);
    }

    public ImagesViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagesViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImagesViewGroup);
//        maringTop = (int) a.getDimension(R.styleable.ImagesViewGroup_ivg_marginTop, 12);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        maringTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, maringTop, dm);
//        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //控件总宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int size = imageInfoList.size();
        for (int i = 0; i < size; i++) {
            height += (imageInfoList.get(i).height + maringTop);
        }
        Log.d("yzp", width + "   " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (imageInfoList == null) {
            return;
        }
        int left = 0;
        int right = r;
        int top = t;
        int bottom = b;
        int size = imageInfoList.size();
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                bottom = imageInfoList.get(i).height;
            } else {
                top = bottom + maringTop;
                bottom = imageInfoList.get(i).height + top;
            }
            ImageView imageView = new ImageView(context);
//            LayoutParams param = new MarginLayoutParams(imageInfoList.get(i).width, imageInfoList.get(i).height);
//            addView(imageView, param);
            addView(imageView);
//            Glide.with(context).load(imageInfoList.get(i).imageUrl).fitCenter().into(imageView);
            imageView.layout(left, top, right, bottom);
            final int finalI = i;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.imageClick(imageInfoList.get(finalI).width, imageInfoList.get(finalI).height,
                            imageInfoList.get(finalI).imageUrl, finalI);
                }
            });
            Log.d("yzp", left + "  " + top + "  " + right + "   " + bottom);

        }
    }

    private void generateImageView(String imageUrl) {
        ImageView imageView = new ImageView(context);

    }

    public void setImageInfoList(List<ImageInfo> list) {
        this.imageInfoList = list;
    }

    public static class ImageInfo {
        public int width;
        public int height;
        public String imageUrl;

        public ImageInfo(int width, int height, String imageUrl) {
            this.width = width;
            this.height = height;
            this.imageUrl = imageUrl;
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void imageClick(int width, int height, String imageUrl, int pos);
    }
}
