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
        //???????????????bar,????????????
        setTranslucentStatusBar(true);
        //1.??????????????????

        //2.???????????????????????????????????????????????????????????????????????????????????????????????????
        if (ElearnApplication.isLogin()) {
            JSONObject json = ElearnApplication.getJsonLoinInfo();
            LoginAuto.loginAuto(json, this, null, null, null, true, false);
        } else {
            //???????????????????????????????????????????????????????????????????????????????????????
            LoaderResSuccess(this);
        }
        initBannerView();
        //?????????????????????
        requestPermissions();
    }

    //??????Banner??????
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
     * ???????????????????????????????????????????????????
     *
     * @param context
     * @param T
     */
    public static void loaderRes(final HttpCycleContext context, final Class T, @Nullable final JSONObject Extra) {
        //??????Request
        RequestParams params = RequestParamsBuilder.buildRequestParams(context);
        JSONObject loginInfo = ElearnApplication.getJsonLoinInfo();
        String username = loginInfo.getString("username");
        String password = loginInfo.getString("password");
        //????????????
        HttpRequest.get(Api.GET_TEACH_BUSINESS_INFO + username, params, new JsonHttpRequestCallback() {
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                String message = jsonObject.getString("message");
                if (jsonObject.getBoolean("success")) {
                    //1.??????teachBusinessInfo??????
                    JSONObject result = jsonObject.getJSONObject("result");
                    String teacherInfo = result.getString("teachInfo");//??????????????????
                    String teachTimes = result.getString("times");    //????????????????????????
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
                    //2.??????????????????
                    ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                    //3.????????????
                    Tools.setShareInfo((Context) context, "TeachBusinessInfo", teachBusinessInfo);
                    Tools.saveInfo2SharedForStr((Context) context, "times", teachTimes);
                    //5.?????????????????????????????????????????????????????????
                    if (T != null) Tools.toOtherPage((Context) context, T, true, Extra);
                    //????????????
                    EMCLogin(username, password);
                    Log.d("loaderRes_Success", message + "");
                } else {
                    Log.d("loaderRes_Failure", message + "");
                }
                //4.???????????????????????????
                Activity activity = Tools.findActivity((Context) context);
                if (activity.getComponentName().getClassName().equals(MainActivity.class.getName())) {
                    LoaderResSuccess(activity);
                }
                if (!StringUtils.isEmpty(message))
                    Toast.makeText((Context) context, message, Toast.LENGTH_SHORT).show();
            }

            //?????????????????????????????????JSON????????????????????????????????????
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText((Context) context, "???????????????????????????", Toast.LENGTH_SHORT).show();
                Log.d("loaderRes_failure", errorCode + ":" + msg);
            }

            //??????????????????
            @Override
            public void onFinish() {
                Log.d("loaderRes_finish", "????????????");
            }
        });
    }

    /**
     * ????????????????????????
     * todo,??????
     */
    private static void EMCLogin(String username, String password) {
        //??????????????????,???????????????????????????????????????????????????
        //EMClientUtils.EMC_COMMON_PWD ???????????????????????????????????? linkingwin.123
        EMClient.getInstance().login(username, "1", new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("EMCLogin", "login: onSuccess");
                // ??????????????????????????????????????????????????????????????????????????? load ??????
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
            bt_loader.setText("????????????");
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
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
