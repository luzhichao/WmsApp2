package com.linkingwin.education.studyonline.student.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewInScrollView extends ListView {

    public ListViewInScrollView(Context context) {
        super(context);
    }

    public ListViewInScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewInScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            // 当手指触摸listview时，让父控件交出ontouch权限,不能滚动
            case MotionEvent.ACTION_DOWN:
                setParentScrollAble(false);
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 当手指松开时，让父控件重新获取onTouch权限
                setParentScrollAble(true);
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }
}
