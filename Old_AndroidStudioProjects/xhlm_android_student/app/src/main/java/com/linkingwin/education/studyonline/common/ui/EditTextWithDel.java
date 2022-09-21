package com.linkingwin.education.studyonline.common.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class EditTextWithDel extends AppCompatEditText {

    private final static String TAG = "EditTextWithDel";
    private Drawable imgInable;
    private Context mContext;

    public EditTextWithDel(Context context) {
        this (context, null, 0);
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        mContext = context;
        init ();
    }

    private void init() {
        imgInable = ContextCompat.getDrawable (mContext, android.R.drawable.ic_delete);
        addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setDrawable ();
            }
        });
        setDrawable ();
    }
    // 设置删除图片
    private void setDrawable() {
        if (length () < 1) {
            setCompoundDrawablesWithIntrinsicBounds (null, null, null, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds (null, null, imgInable, null);
        }
    }

    // 处理删除操作


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgInable != null && event.getAction () == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX ();
            int eventY = (int) event.getRawY ();
            Log.d (TAG, "(" + eventX + ", " + eventY + ")");
            Rect rect = new Rect ();
            getGlobalVisibleRect (rect);
            rect.left = rect.right - 70;
            Log.d (TAG, rect.toString ());
            if (rect.contains (eventX, eventY)) {
                setText ("");
            }
        }
        return super.onTouchEvent (event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize ();
    }
}