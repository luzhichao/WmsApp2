package com.linkingwin.education.studyonline.common.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.linkingwin.education.studyonline.common.utils.BeanUtils;
import com.linkingwin.education.studyonline.student.vo.ITextBean;

import org.xutils.view.annotation.ContentView;

public abstract class DropPopupDialog<T extends ITextBean> {


    private Context context;

    private PopupDialogButton<T> popupDialogButton;

    private View contentView;

    public DropPopupDialog(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public final View getContentView() {
        if (null == contentView) {
            ContentView cView = BeanUtils.getAnnotation(getClass(), ContentView.class);
            if (null == cView) {
                throw new RuntimeException("该PopupDialog未设置ContentView");
            } else {
                contentView = LayoutInflater.from(context).inflate(cView.value(), null);
            }
        }
        return contentView;
    }

    public final void setValue(T bean) {
        popupDialogButton.setValue(bean);
    }

    public void setPopupDialogButton(PopupDialogButton<T> popupDialogButton) {
        this.popupDialogButton = popupDialogButton;
    }
}