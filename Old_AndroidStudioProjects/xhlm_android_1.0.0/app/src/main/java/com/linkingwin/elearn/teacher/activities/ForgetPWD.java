package com.linkingwin.elearn.teacher.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.util.LoginVerify;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

public class ForgetPWD extends BaseActivity {

    @BindView(R.id.forget_num)
    EditText forget_num;
    @BindView(R.id.forget_title)
    TitleView titleView;
    @BindView(R.id.forget_tip)
    TextView forget_tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        titleView.setTitleText( getString(R.string.forget_title),R.color.titleWhite );
        //加载沉浸式bar,全屏显示
        setTranslucentStatusBar( true );
        //设置手机号码输入的校验
        forget_num.setOnFocusChangeListener(new LoginVerify(forget_num,forget_tip));
    }
}
