package com.linkingwin.education.studyonline.common.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Checkable;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.student.vo.ITextBean;

public class PopupDialogButton<T extends ITextBean> extends RelativeLayout implements Checkable, View.OnClickListener {

    private View self;
    private T value;
    private TextView text;
    private View bLine;
    private boolean isOpen = false;
    private PopupWindow popupWindow;
    private DropPopupDialog<T> dropPopupDialog;
    private View anchor;

    public PopupDialogButton(Context context) {
        super(context);
        init(context);
    }

    public PopupDialogButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PopupDialogButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void togglePropWindow(boolean isOpen) {
        this.isOpen = isOpen;
        setCheckStyle(isOpen);
        if (null != popupWindow) {
            if (isOpen && !popupWindow.isShowing()) {
                popupWindow.showAsDropDown(getAnchor());
            } else {
                popupWindow.dismiss();
            }
        }
    }

    private void setCheckStyle(boolean isOpen) {
        Drawable icon;
        if (isOpen) {
            icon = getResources().getDrawable(R.mipmap.ic_dropdown_actived);
            text.setTextColor(getResources().getColor(R.color.btn_green_noraml));
            bLine.setVisibility(VISIBLE);
        } else {
            icon = getResources().getDrawable(R.mipmap.ic_dropdown_normal);
            text.setTextColor(getResources().getColor(R.color.black_deep));
            bLine.setVisibility(GONE);
        }
        text.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
    }


    @SuppressLint("WrongConstant")
    private PopupWindow newPopupWindow(View contentView) {
        //内容，高度，宽度
        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationTopFade);
        //菜单背景色
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        //关闭事件
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return popupWindow;
    }

    private void init(Context context) {
        self =  LayoutInflater.from(getContext()).inflate(R.layout.dropdown_tab_button,this, true);
        text = self.findViewById(R.id.textView);
        bLine = self.findViewById(R.id.bottomLine);
        setOnClickListener(this);
    }

    public View getAnchor() {
        if (null == anchor) {
            return self;
        }
        return anchor;
    }

    public PopupDialogButton<T> setAnchor(View anchor) {
        this.anchor = anchor;
        return this;
    }

    @Override
    public void setChecked(boolean checked) {
        togglePropWindow(checked);
    }

    @Override
    public boolean isChecked() {
        return isOpen;
    }

    @Override
    public void toggle() {
        setChecked(!isOpen);
    }

    public void setValue(T value) {
        this.value = value;
        text.setText(value.lableName());
        togglePropWindow(false);
    }

    public PopupDialogButton<T> setOnDismissListener(PopupWindow.OnDismissListener onDismisslistener) {
        if (null != popupWindow) {
            popupWindow.setOnDismissListener(onDismisslistener);
        }
        return this;
    }

    public T getValue() {
        return value;
    }

    @Override
    public void onClick(View v) {
        togglePropWindow(!isOpen);
    }

    public PopupDialogButton<T> setDropPopupDialog(@NonNull DropPopupDialog<T> dropPopupDialog) {
        this.dropPopupDialog = dropPopupDialog;
        this.popupWindow = newPopupWindow(dropPopupDialog.getContentView());
        dropPopupDialog.setPopupDialogButton(this);
        dropPopupDialog.getContentView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePropWindow(false);
            }
        });
        return this;
    }


}
