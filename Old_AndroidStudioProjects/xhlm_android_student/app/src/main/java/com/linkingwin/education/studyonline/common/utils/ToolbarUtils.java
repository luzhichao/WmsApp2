package com.linkingwin.education.studyonline.common.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.annotation.ToolbarContent;

import java.util.HashSet;

public class ToolbarUtils {

    private static final HashSet<Class<?>> IGNORED = new HashSet<Class<?>>();

    static {
        IGNORED.add(Object.class);
        IGNORED.add(Activity.class);
        IGNORED.add(android.app.Fragment.class);
        try {
            IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
            IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
        } catch (Throwable ignored) {
        }
    }

    public static ToolbarContent findToolbarContent(Class<?> thisCls) {
        if (thisCls == null || IGNORED.contains(thisCls)) {
            return null;
        }
        ToolbarContent toolbar = thisCls.getAnnotation(ToolbarContent.class);
        return toolbar;
    }

    public static void initToolbar(final AppCompatActivity activity, Toolbar toolbar, ToolbarContent toolbarContent) {
        if (toolbar == null || toolbarContent == null) return;
        if(toolbarContent.resId ()==-1)
            toolbar.setNavigationIcon(null);
        else
            toolbar.setNavigationIcon(toolbarContent.resId ());
        toolbar.setTitle(toolbarContent.title ());
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//点击左上角返回键
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().finishActivity(activity);
            }
        });
    }
}
