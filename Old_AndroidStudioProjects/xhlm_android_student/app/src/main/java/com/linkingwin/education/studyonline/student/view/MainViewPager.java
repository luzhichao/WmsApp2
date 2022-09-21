package com.linkingwin.education.studyonline.student.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MainViewPager extends ViewPager {
    private boolean isSlidingEnable = false ;

    public MainViewPager(Context context) {
        super(context);
    }

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写此函数
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return  this.isSlidingEnable && super.onTouchEvent(ev);
    }
    //重写此函数
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isSlidingEnable && super.onInterceptTouchEvent(ev);
    }

    public void setSlidingEnable(boolean slidingEnable) {
        isSlidingEnable = slidingEnable;
    }
}
