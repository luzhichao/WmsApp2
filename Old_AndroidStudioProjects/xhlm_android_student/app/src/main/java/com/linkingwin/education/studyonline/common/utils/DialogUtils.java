package com.linkingwin.education.studyonline.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.apache.commons.lang3.StringUtils;

/**
 * @描述 对话框工具类
 * @作者 gsh
 * @时间 2017/8/17 17:32
 */

public class DialogUtils {

    public static Dialog confirmDialog(Context context, DialogText text){
        return confirmDialog(context,text,null,null);
    }

    public static Dialog confirmDialog(Context context, DialogText text, DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancleClickListener){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        if(StringUtils.isNotEmpty(text.getTitle())){
            builder.setTitle(text.getTitle());
        }
        if(text.getView()!=null){
            builder.setView(text.getView());
        }else{
            if(StringUtils.isNotEmpty(text.getMessage())){
                builder.setMessage(text.getMessage());
            }
        }

        builder.setPositiveButton(text.getPositive(), okClickListener);
        builder.setNegativeButton(text.getNegative(), cancleClickListener);
        builder.setCancelable(false);
        return builder.create();
    }

    public static Dialog listDialog(Context context, DialogText text, String[] items, DialogInterface.OnClickListener selectClickListener, DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancleClickListener){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        if(StringUtils.isNotEmpty(text.getTitle())){
            builder.setTitle(text.getTitle());
        }
        builder.setItems(items,selectClickListener);

        builder.setCancelable(false);

        builder.setPositiveButton(text.getPositive(), okClickListener);
        builder.setNegativeButton(text.getNegative(), cancleClickListener);
        return builder.create();
    }

    public static class DialogText{
        private String title;
        private String message;
        private View view;
        private String positive="确定";
        private String negative="取消";

        public DialogText(String title, String message) {
            this.title = title;
            this.message = message;
        }

        public DialogText(String title, View view) {
            this.title = title;
            this.view = view;
        }

        public DialogText(String title, String message, String positive, String negative) {
            this.title = title;
            this.message = message;
            this.positive = positive;
            this.negative = negative;
        }

        public DialogText(String title, View view, String positive, String negative) {
            this.title = title;
            this.view = view;
            this.positive = positive;
            this.negative = negative;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public String getPositive() {
            return positive;
        }

        public void setPositive(String positive) {
            this.positive = positive;
        }

        public String getNegative() {
            return negative;
        }

        public void setNegative(String negative) {
            this.negative = negative;
        }
    }
}
