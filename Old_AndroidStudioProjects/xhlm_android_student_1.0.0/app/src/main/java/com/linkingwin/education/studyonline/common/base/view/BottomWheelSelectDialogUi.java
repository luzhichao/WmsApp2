package com.linkingwin.education.studyonline.common.base.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import jsc.kit.wheel.base.IWheel;
import jsc.kit.wheel.dialog.ColumnWheelDialog;

public class BottomWheelSelectDialogUi<T1 extends IWheel, T2 extends IWheel, T3 extends IWheel, T4 extends IWheel, T5 extends IWheel> {

    private ColumnWheelDialog<T1, T2, T3, T4, T5> dialog;

    private T1[] items1 = null;
    private T2[] items2 = null;
    private T3[] items3 = null;
    private T4[] items4 = null;
    private T5[] items5 = null;

    private int selectedIndex1 = 0;
    private int selectedIndex2 = 0;
    private int selectedIndex3 = 0;
    private int selectedIndex4 = 0;
    private int selectedIndex5 = 0;

    public static
        <T1 extends IWheel, T2 extends IWheel, T3 extends IWheel, T4 extends IWheel, T5 extends IWheel>
        BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> newInstance() {
        return new BottomWheelSelectDialogUi<T1, T2, T3, T4, T5>();
    }

    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> initDialog(Context context) {
        this.dialog = new ColumnWheelDialog<>(context);
        dialog.show();
        return this;
    }

    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> setTitle(String title) {
        dialog.setTitle(title);
        return this;
    }

    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> setItemData1(T1[] item, int selectedIndex) {
        this.items1 = item;
        this.selectedIndex1 = selectedIndex;
        return this;
    }


    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> setItemData2(T2[] item, int selectedIndex) {
        this.items2 = item;
        this.selectedIndex2 = selectedIndex;
        return this;
    }


    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> setItemData3(T3[] item, int selectedIndex) {
        this.items3 = item;
        this.selectedIndex3 = selectedIndex;
        return this;
    }


    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> setItemData4(T4[] item, int selectedIndex) {
        this.items4 = item;
        this.selectedIndex4 = selectedIndex;
        return this;
    }


    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> setItemData5(T5[] item, int selectedIndex) {
        this.items5 = item;
        this.selectedIndex5 = selectedIndex;
        return this;
    }

    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> setCancelButton(String cancelButtonName, final OnClickListener<T1, T2, T3, T4, T5> listener) {
        dialog.setCancelButton(cancelButtonName, new ColumnWheelDialog.OnClickCallBack<T1, T2, T3, T4, T5>() {
            @Override
            public boolean callBack(View v, @Nullable T1 item0, @Nullable T2 item1, @Nullable T3 item2, @Nullable T4 item3, @Nullable T5 item4) {
                if (null != listener) {
                    return listener.onClick(item0, item1, item2, item3 ,item4);
                }
                return false;
            }
        });
        return this;
    }

    public BottomWheelSelectDialogUi<T1, T2, T3, T4, T5> setOKButton(String okButtonName, final OnClickListener<T1, T2, T3, T4, T5>  listener) {
        dialog.setOKButton(okButtonName, new ColumnWheelDialog.OnClickCallBack<T1, T2, T3, T4, T5>() {
            @Override
            public boolean callBack(View v, @Nullable T1 item0, @Nullable T2 item1, @Nullable T3 item2, @Nullable T4 item3, @Nullable T5 item4) {
                if (null != listener) {
                    return listener.onClick(item0, item1, item2, item3 ,item4);
                }
                return false;
            }
        });
        return this;
    }

    public void show() {
        dialog.setItems(items1, items2, items3, items4, items5);
        dialog.setSelected(selectedIndex1, selectedIndex2, selectedIndex3, selectedIndex4, selectedIndex5);
    }

    public interface OnClickListener<T1, T2, T3, T4, T5> {
        boolean onClick(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5);
    }

}
