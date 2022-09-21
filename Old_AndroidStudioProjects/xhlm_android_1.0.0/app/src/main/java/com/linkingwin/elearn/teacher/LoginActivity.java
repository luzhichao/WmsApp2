package com.linkingwin.elearn.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.jaiky.imagespickers.Image;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.GlideImageLoader;
import com.linkingwin.elearn.common.util.LoginAuto;
import com.linkingwin.elearn.common.util.LoginVerify;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.teacher.activities.ForgetPWD;
import com.linkingwin.elearn.teacher.activities.HomePage;
import com.linkingwin.elearn.teacher.activities.RealAuth;
import com.linkingwin.elearn.teacher.activities.Register;
import com.youth.banner.Banner;


import java.util.ArrayList;
import java.util.concurrent.FutureTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import qiu.niorgai.StatusBarCompat;

public class LoginActivity extends BaseActivity {
    //获取界面控件
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.et_num)
    EditText et_num;
    @BindView(R.id.et_pwd)
    EditText et_pass;
    @BindView(R.id.tv_Tip)
    TextView tv_Tip;
    @BindView(R.id.login_banner)
    Banner loginBanner;

    @BindView( R.id.iv_login_pic )
    ImageView iv_login_pic;
    @BindView( R.id.iv_vertical_bar )
    ImageView iv_vertical_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        //加载布局
        setContentView( R.layout.activity_login );
        //绑定注解，必须要加载布局后才可以
        ButterKnife.bind( this );
        //如果记录已经登陆，则直接跳转到主页

        if (ElearnApplication.isLogin()) {
            JSONObject json = ElearnApplication.getJsonLoinInfo();
            et_num.setText( json.getString( "username" ) );
            et_pass.setText( json.getString( "password" ) );
            LoginAuto.loginAuto( json, this, tv_Tip,HomePage.class,null,false,true );
        }
        //设置沉浸式bar,全屏显示
        setTranslucentStatusBar( true );

        //设置手机号码输入的校验
        et_num.setOnFocusChangeListener( new LoginVerify( et_num, tv_Tip ) );

        //加载Banner布局视图
        initBannerView();
    }

    //登陆校验
    @OnClick(R.id.bt_login)
    public void loginVerify(View view) {
        final String username = et_num.getText().toString();
        final String password = et_pass.getText().toString();
        if ("".equals( username ) || "".equals( password )) {
            Toast.makeText( LoginActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT ).show();
            return;
        }
        JSONObject loginInfo=new JSONObject(  );
        loginInfo.put( "username",username );
        loginInfo.put( "password",password );
        LoginAuto.loginAuto( loginInfo, this, tv_Tip,HomePage.class,null,true,true );
    }

    //新用户注册页面跳转
    @OnClick(R.id.bt_Register)
    public void AccountRegister(View view) {
        Intent intent = new Intent( LoginActivity.this,
                Register.class );
        LoginActivity.this.startActivity( intent );
    }

    //忘记密码页面跳转
    @OnClick(R.id.tv_forgetPWD)
    public void ForgetPWD(View view) {
        Intent intent = new Intent( LoginActivity.this,
                ForgetPWD.class );
        LoginActivity.this.startActivity( intent );
    }

    //加载Banner布局
    private void initBannerView() {
        ArrayList<Integer> imagePath;
        ArrayList<String> imageTitle;
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add( R.mipmap.vertical_bar );
        imageTitle.add( "title" );
        Tools.initBannerView( loginBanner, imagePath, imageTitle );
    }
}
