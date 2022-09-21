package com.linkingwin.elearn.teacher.activities;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.constant.XhlmConstant;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.GlideImageLoader;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.common.util.ImageSelectorUtil;
import com.linkingwin.elearn.common.util.LoginAuto;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.RealNameAuth;
import com.linkingwin.elearn.teacher.model.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class RealAuth extends BaseActivity {

    @BindView(R.id.title_real_auth)
    TitleView titleView;

    @BindView(R.id.et_real_name)
    EditText et_real_name;
    @BindView(R.id.et_auth_number)
    EditText et_auth_number;

    @BindView(R.id.front_auth)
    RelativeLayout front_auth;
    @BindView(R.id.back_auth)
    RelativeLayout back_auth;
    @BindView(R.id.hold_auth)
    RelativeLayout hold_auth;
    @BindView(R.id.iv_front_auth)
    ImageView iv_front_auth;
    @BindView(R.id.iv_back_auth)
    ImageView iv_back_auth;
    @BindView(R.id.iv_hold_auth)
    ImageView iv_hold_auth;

    private static Map<String, String> files = new HashMap<>();
    private static ImageView currIView;//当前选择上传的imageview
    private GlideLoader glideImageLoader = new GlideLoader();//图片展示工具
    public static final int REQUEST_CODE = XhlmConstant.REAL_AUTH_UPLOAD_REQUEST_CODE;//APP应用内唯一


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_auth);
        ButterKnife.bind(this);
        setTranslucentStatusBar( true );
        //页面初始画
        initRealAuth();
        titleView.setTitleText("实名认证", R.color.titleWhite);
    }

    @OnClick(R.id.front_auth)
    public void setFrontAuth(View v){
        currIView = findViewById(R.id.iv_front_auth);
        ImageSelectorUtil.initImageSelector(REQUEST_CODE, RealAuth.this);
    }
    @OnClick(R.id.back_auth)
    public void setBackAuth(View v){
        currIView = findViewById(R.id.iv_back_auth);
        ImageSelectorUtil.initImageSelector(REQUEST_CODE, RealAuth.this);
    }
    @OnClick(R.id.hold_auth)
    public void setHoldAuth(View v){
        currIView = findViewById(R.id.iv_hold_auth);
        ImageSelectorUtil.initImageSelector(REQUEST_CODE, RealAuth.this);
    }


    /**
     * 选择图片后处理方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            glideImageLoader.displayImage( getContext(), pathList.get(0), currIView);

            RequestParams params = new RequestParams(RealAuth.this);
            params.addHeader("accessToken", RequestParamsBuilder.getToken());
            params.addFormDataPart("file" , new File(pathList.get(0)));
            params.addFormDataPart("merchantId", XhlmConstant.UPLOAD_MERCHANTID);
            params.addFormDataPart("channel", XhlmConstant.UPLOAD_CHANNEL);
            HttpRequest.post(Api.POST_UPLOAD_FILE, params, new JsonHttpRequestCallback() {
                @Override
                protected void onSuccess(JSONObject jsonObject) {
                    super.onSuccess( jsonObject );
                    String msg=jsonObject.getString( "message" );
                    if (jsonObject.getBoolean( "success" )) {
                        files.put(Integer.toString(currIView.getId()), jsonObject.getString("result"));
                    }
                    makeText( RealAuth.this,msg , LENGTH_SHORT ).show();
                }
                //请求失败（服务返回非法JSON、服务器异常、网络异常）
                @Override
                public void onFailure(int errorCode, String msg) {
                    makeText( RealAuth.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                    Log.d( "submit_auth_fail", errorCode + ":" + msg );
                }
                //请求网络结束
                @Override
                public void onFinish() {
                    Log.d( "submit_auth_end", "请求结束" );
                }
            });

        }
    }

    /**
     * 提交实名认证
     * @param view
     */
    @OnClick(R.id.submit_auth)
    public void submitAuth(View view){
        RequestParams params = RequestParamsBuilder.buildRequestParams(RealAuth.this);
        RealNameAuth realNameAuth = new RealNameAuth();
        realNameAuth.setRealName(et_real_name.getText().toString());
        realNameAuth.setAuthType("身份证");
        realNameAuth.setAuthNumber(et_auth_number.getText().toString());
        realNameAuth.setAuthPicture(files);
        final String realNameAuthJson = JSONObject.toJSONString(realNameAuth);
        params.addFormDataPart("realNameAuth", realNameAuthJson);
        HttpRequest.post(Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
            //请求成功
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String msg = jsonObject.getString( "msg" );
                if (jsonObject.getBoolean( "success" )) {
                    //更新全局变量,提交成功更新本地的shared数据
                    ElearnApplication.teachBusinessInfo.setRealNameAuth(realNameAuthJson);
                    Tools.setShareInfo( getContext(),"TeachBusinessInfo",ElearnApplication.teachBusinessInfo);
                    makeText( RealAuth.this, msg, LENGTH_SHORT ).show();
                    finish();
                }
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( RealAuth.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                Log.d( "submit_auth_fail", errorCode + ":" + msg );
            }
            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "submit_auth_end", "请求结束" );
            }
        });
    }

    /**
     * 初始化实名认证
     */
    public void initRealAuth(){
//        et_real_name.setText(ElearnApplication.teachBusinessInfo.getRealName());
        String realNameAuth = ElearnApplication.teachBusinessInfo.getRealNameAuth();
        if (!StringUtils.isBlank(realNameAuth)){
            JSONObject realAuthJson = JSONObject.parseObject(realNameAuth);
            et_real_name.setText(realAuthJson.getString("realName"));
            et_auth_number.setText(realAuthJson.getString("authNumber"));
            Map<String, String> authPicture = (Map<String, String>) realAuthJson.get("authPicture");
            if(authPicture != null && authPicture.size() > 0){
                files = authPicture;
                for(Map.Entry<String, String> e : authPicture.entrySet()){
                    if(Integer.valueOf(e.getKey()).compareTo(this.iv_front_auth.getId()) == 0){
                        glideImageLoader.displayImage( getContext(), e.getValue(), this.iv_front_auth);
                    } else if (Integer.valueOf(e.getKey()).compareTo(this.iv_back_auth.getId()) == 0){
                        glideImageLoader.displayImage( getContext(), e.getValue(), this.iv_back_auth);
                    } else if (Integer.valueOf(e.getKey()).compareTo(this.iv_hold_auth.getId()) == 0){
                        glideImageLoader.displayImage( getContext(), e.getValue(), this.iv_hold_auth);
                    }
                }
            } else {
                files = new HashMap<>();
            }
        } else {
            files = new HashMap<>();
        }
    }

}
