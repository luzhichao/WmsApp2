package com.linkingwin.education.studyonline.common.base.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

public abstract class BottomSheetDialogUi {

    private BottomSheetDialog bottomSheetDialog;
    private Context context;

    public BottomSheetDialogUi(@NonNull Context context, @LayoutRes int layoutResId) {
        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(layoutResId);
        this.context = context;
    }

    public BottomSheetDialog getBottomSheetDialog() {
        return bottomSheetDialog;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return bottomSheetDialog.getDelegate().findViewById(id);
    }

    public BottomSheetDialogUi show() {
        bottomSheetDialog.show();
        return this;
    }

    public void dismiss() {
        bottomSheetDialog.dismiss();
    }

    public abstract BottomSheetDialogUi init();

    public Context getContext() {
        return context;
    }
}
