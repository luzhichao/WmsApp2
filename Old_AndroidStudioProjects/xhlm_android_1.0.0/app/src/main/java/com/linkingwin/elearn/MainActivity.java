package com.linkingwin.elearn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.runtimepermissions.PermissionsManager;
import com.linkingwin.elearn.common.runtimepermissions.PermissionsResultAction;
import com.linkingwin.elearn.common.util.LoginAuto;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.teacher.LoginActivity;
import com.linkingwin.elearn.teacher.activities.HomePage;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

public class MainActivity extends BaseActivity {
    @BindView(R.id.bt_loader)
    Button bt_loader;

    @BindView(R.id.welcome_banner)
    Banner welcomeBanner;
    private static boolean progressShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //设置沉浸式bar,全屏显示
        setTranslucentStatusBar(true);
        //1.加载启动动画

        //2.如果本地记录密码则自动登陆，并初始化所需的资源，否则跳转到登陆页面
        if (ElearnApplication.isLogin()) {
            JSONObject json = ElearnApplication.getJsonLoinInfo();
            LoginAuto.loginAuto(json, this, null, null, null, true, false);
        } else {
            //如果没有记住登陆信息则不需要加载资源，直接可以打开登陆界面
            LoaderResSuccess(this);
        }
        initBannerView();
        //初始化权限检查
        requestPermissions();
    }

    //加载Banner布局
    private void initBannerView() {
        ArrayList<Integer> imagePath;
        ArrayList<String> imageTitle;
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.teach_welcome_banner1);
        imageTitle.add("teach_welcome_banner1");
        imagePath.add(R.drawable.teach_welcome_banner2);
        imageTitle.add("teach_welcome_banner2");
        Tools.initBannerView(welcomeBanner, imagePath, imageTitle);
    }

    @OnClick(R.id.bt_loader)
    public void toHomePage(View view) {
        if (ElearnApplication.isLogin()) {
            Tools.toOtherPage(this, HomePage.class, true, null);
            return;
        }
        Tools.toOtherPage(this, LoginActivity.class, true, null);
    }

    /**
     * 如果记住了登陆信息，则需要加载资源
     *
     * @param context
     * @param T
     */
    public static void loaderRes(final HttpCycleContext context, final Class T, @Nullable final JSONObject Extra) {
        //获取Request
        RequestParams params = RequestParamsBuilder.buildRequestParams(context);
        JSONObject loginInfo = ElearnApplication.getJsonLoinInfo();
        String username = loginInfo.getString("username");
        String password = loginInfo.getString("password");
        //发起请求
        HttpRequest.get(Api.GET_TEACH_BUSINESS_INFO + username, params, new JsonHttpRequestCallback() {
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                String message = jsonObject.getString("message");
                if (jsonObject.getBoolean("success")) {
                    //1.获取teachBusinessInfo对象
                    JSONObject result = jsonObject.getJSONObject("result");
                    String teacherInfo = result.getString("teachInfo");//老师基本信息
                    String teachTimes = result.getString("times");    //老师授课时间信息
                    JSONObject user = result.getJSONObject("user");
                    TeachBusinessInfo teachBusinessInfo;
                    if (teacherInfo != null && teacherInfo + "" != "") {
                        teachBusinessInfo = JSONObject.parseObject(teacherInfo, TeachBusinessInfo.class);
                    } else {
                        teachBusinessInfo = new TeachBusinessInfo();
                    }
                    teachBusinessInfo.setSex(user.getString("sex"));
                    teachBusinessInfo.setAddress(user.getString("address"));
                    teachBusinessInfo.setAvatar(user.getString("avatar"));
                    teachBusinessInfo.setNickName(user.getString("nickName"));
                    teachBusinessInfo.setUsername(user.getString("username"));
                    teachBusinessInfo.setUserId(user.getString("id"));
                    //2.配置全局变量
                    ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                    //3.保存本地
                    Tools.setShareInfo((Context) context, "TeachBusinessInfo", teachBusinessInfo);
                    Tools.saveInfo2SharedForStr((Context) context, "times", teachTimes);
                    //5.跳转到首页，不自动跳转需要点击按钮跳转
                    if (T != null) Tools.toOtherPage((Context) context, T, true, Extra);
                    //登陆环信
                    EMCLogin(username, password);
                    Log.d("loaderRes_Success", message + "");
                } else {
                    Log.d("loaderRes_Failure", message + "");
                }
                //4.打开跳转按钮可点击
                Activity activity = Tools.findActivity((Context) context);
                if (activity.getComponentName().getClassName().equals(MainActivity.class.getName())) {
                    LoaderResSuccess(activity);
                }
                if (!StringUtils.isEmpty(message))
                    Toast.makeText((Context) context, message, Toast.LENGTH_SHORT).show();
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText((Context) context, "网络异常，登陆失败", Toast.LENGTH_SHORT).show();
                Log.d("loaderRes_failure", errorCode + ":" + msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d("loaderRes_finish", "请求结束");
            }
        });
    }

    /**
     * 环信登陆注册服务
     * todo,密码
     */
    private static void EMCLogin(String username, String password) {
        //环信用户登陆,需要在注册文件里面注册环信相关服务
        //EMClientUtils.EMC_COMMON_PWD 所有环信用户密码统一使用 linkingwin.123
        EMClient.getInstance().login(username, "1", new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("EMCLogin", "login: onSuccess");
                // 以上两个方法是为了保证进入主页面后本地会话和群组都 load 完毕
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d("EMCLogin", "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d("EMCLogin", "login: onError: " + code);
            }
        });
    }

    public static void LoaderResSuccess(Activity activity) {
        Button bt_loader = activity.findViewById(R.id.bt_loader);
        if (bt_loader != null) {
            bt_loader.setEnabled(true);
            bt_loader.setText("立即体验");
        }
    }

    /**
     * 权限检查，弹出提醒界面，打开在资源中注册申请的权限
     */
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
