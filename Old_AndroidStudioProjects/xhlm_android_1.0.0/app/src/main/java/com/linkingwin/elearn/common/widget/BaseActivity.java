package com.linkingwin.elearn.common.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.linkingwin.elearn.common.http.MyHttpCycleContext;

import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpTaskHandler;
import qiu.niorgai.StatusBarCompat;

public class BaseActivity extends AppCompatActivity implements MyHttpCycleContext {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setTranslucentStatusBar(Boolean vis){
        if(vis){
            StatusBarCompat.translucentStatusBar( this );
        }
    }

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
    }
}
