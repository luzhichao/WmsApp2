package com.linkingwin.elearn.teacher.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.util.LoginAuto;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;

import com.linkingwin.elearn.common.util.LoginVerify;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;

import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import qiu.niorgai.StatusBarCompat;

public class Register extends BaseActivity {
    @BindView(R.id.register_banner)
    Banner register_banner;
    @BindView(R.id.et_register_num)
    EditText et_register_num;
    @BindView( R.id.et_register_verNUM )
    EditText et_register_verNUM;
    @BindView(R.id.tv_TipNum_reg)
    TextView tv_Tip;
    @BindView(R.id.et_register_againPWD)
    TextView et_register_againPWD;
    @BindView(R.id.et_register_PWD)
    TextView et_register_PWD;
    @BindView(R.id.title_register)
    TitleView titleView;
    @BindView( R.id.bt_getVerNUM )
    Button bt_getVerNum;
    @BindView( R.id.cb_protocol )
    CheckBox cb_protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        titleView.setTitleText(getString(R.string.newAccountTitle),R.color.titleWhite);
        //设置手机号码输入的校验
        et_register_num.setOnFocusChangeListener(new LoginVerify(et_register_num,tv_Tip));
        //加载Banner布局视图
//        initBannerView();
        //加载沉浸式bar,全屏显示
        setTranslucentStatusBar( true );
    }

    /**
     * 获取短信验证码,718496
     * @param view
     */
    @OnClick(R.id.bt_getVerNUM)
    public void getVerNum(View view){

        /*
        这里补充获取验证码的内容
        TODO
        */
        String verNum=et_register_num.getText().toString().trim()+"";
        if(verNum==""){
            tv_Tip.setText("请填写手机号");
            tv_Tip.setVisibility(View.VISIBLE);
            return;
        }
        if(!Tools.isMobileNO( verNum )){
            tv_Tip.setText("请填写11位手机号");
            tv_Tip.setVisibility(View.VISIBLE);
            return;
        }
        String URL=Api.GET_VERNUM_API+verNum;
        //获取Request
        RequestParams params=RequestParamsBuilder.buildRequestParams( this);
        //发起请求
        HttpRequest.get(URL,params,new JsonHttpRequestCallback(){
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                String message=jsonObject.getString("code")+"";
                RequestParamsBuilder.setToken(jsonObject.getString("result"));
                if (jsonObject.getBoolean("success")){
                    Toast.makeText(Register.this, "请等待接受验证码\n若未获取，请1分钟后重试", Toast.LENGTH_SHORT).show();
                    //验证码获取成功，1分钟后可再次获取
                    new CountDownTimer(1000*60, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            bt_getVerNum.setEnabled(false);
                            bt_getVerNum.setText(String.format("%ds后重试",millisUntilFinished/1000));
                        }
                        @Override
                        public void onFinish() {
                            bt_getVerNum.setEnabled(true);
                            bt_getVerNum.setText("获取验证码");
                        }
                    }.start();
                    Log.d("Register_Success",message);
                }else{
                    Log.d("Register_Failure",message);
                    tv_Tip.setText(message);
                    tv_Tip.setVisibility(View.VISIBLE);
                }
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText(Register.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity",errorCode+":"+msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d("RegisterActivity","请求结束");
            }
        });
    }
    /**
     * 打开平台协议页面
     */
    @OnClick(R.id.tv_read_protocol)
    public void readProtocolContent(){
        Intent intent=new Intent(Register.this,RegisterProtocol.class);
        Register.this.startActivity(intent);
    }

    /**
     * 注册提交
     */
    @OnClick(R.id.bt_Register)
    public void submitRegister(View view){
        //1.调用服务端，生成用户信息
        final String username=et_register_num.getText().toString();
        final String password=et_register_PWD.getText().toString();
        final String code=et_register_verNUM.getText().toString();
        if("".equals( username ) || "".equals( password ) || "".equals( code )){
            Toast.makeText(Register.this, "用户名和密码、验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!cb_protocol.isChecked()){
            Toast.makeText(Register.this, "请同意协议内容！", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取Request
        RequestParams params=RequestParamsBuilder.buildRequestParams( this);
        //添加请求参数
        params.addFormDataPart("mobile",username);
        params.addFormDataPart("password",password);
        params.addFormDataPart("code",code);
        //发起请求
        HttpRequest.post(Api.REGISTER_API,params,new JsonHttpRequestCallback(){

            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                String message=jsonObject.getString("msg")+"";
                if (jsonObject.getBoolean( "success" )) {
                    //注册成功自动登陆，登陆成功跳转到基本信息填报页面
                    JSONObject putExtra=new JSONObject(  );
                    putExtra.put( "fromtohere",Api.REGISTER_ROUTER );
                    JSONObject loginInfo=new JSONObject(  );
                    loginInfo.put( "username",username );
                    loginInfo.put( "password",password );
                    LoginAuto.loginAuto( loginInfo, Register.this, tv_Tip,TeacherBaseInfo.class,putExtra,false,true);
                    Toast.makeText( Register.this, "注册成功，完善信息", Toast.LENGTH_SHORT ).show();
                    Log.d( "Register_Success", message );
                }else{
                    Log.d("Register_Failure",message);
                    tv_Tip.setText(message);
                    tv_Tip.setVisibility(View.VISIBLE);
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText(Register.this, "网络异常，登陆失败", Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity",errorCode+":"+msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d("RegisterActivity","请求结束");
            }
        });

    }


    //加载Banner布局视图
    private void initBannerView(){
        ArrayList<Integer> imagePath;
        ArrayList<String> imageTitle;
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.banner);
        imageTitle.add("黑板");
        Tools.initBannerView( register_banner,imagePath,imageTitle);
    }

}
