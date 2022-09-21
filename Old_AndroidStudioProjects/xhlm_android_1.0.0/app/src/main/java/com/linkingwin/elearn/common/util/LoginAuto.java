package com.linkingwin.elearn.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.linkingwin.elearn.MainActivity;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.model.Params;
import com.linkingwin.elearn.teacher.LoginActivity;
import com.linkingwin.elearn.teacher.activities.HomePage;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

public class LoginAuto {
    /**
     * @param loginInfo
     * @param context
     * @param tv_Tip
     * @param T
     * @param Extra
     */
    public static void loginAuto(JSONObject loginInfo,
                                 final HttpCycleContext context, @Nullable final TextView tv_Tip,
                                 @Nullable final Class T, @Nullable final JSONObject Extra,final boolean loader, final boolean finish) {
        //获取Request
        RequestParams params = RequestParamsBuilder.buildRequestParams( context );
        final String username = loginInfo.getString( "username" );
        final String password = loginInfo.getString( "password" );
        //添加请求参数
        params.addFormDataPart( "username", username);
        params.addFormDataPart( "password", password);
        //发起请求
        HttpRequest.post( Api.LOGIN_API, params, new JsonHttpRequestCallback() {

            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String message = jsonObject.getString( "message" );
                RequestParamsBuilder.setToken( jsonObject.getString( "result" ) );
                if (jsonObject.getBoolean( "success" )) {
                    //1.登陆成功后保存User和Password
                    HashMap map = new HashMap();
                    map.put( "username", username );
                    map.put( "password", password );
                    Tools.saveAccount( map, (Context) context );
                    //2.加载资源，加载成功则跳转到主页
                    if(loader) MainActivity.loaderRes(context,T,Extra);
                    Log.d( "Login_Success", message );
                } else {
                    Log.d( "Login_Failure", message );
                    if(tv_Tip!=null){
                        tv_Tip.setText( message );
                        tv_Tip.setVisibility( View.VISIBLE );
                    }
                    Activity activity=Tools.findActivity( (Context) context );
                    ElearnApplication.setIsLogin( false );
                    if(activity.getComponentName().getClassName().equals( MainActivity.class.getName() )){
                       MainActivity.LoaderResSuccess( activity );
                    }else if (!activity.getComponentName().getClassName().equals( LoginActivity.class.getName() )){
                        Tools.toOtherPage( (Context) context,LoginActivity.class,finish,null );
                    }
                    Toast.makeText( (Context) context,message,Toast.LENGTH_SHORT );
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText( (Context) context, "网络异常，登陆失败", Toast.LENGTH_SHORT ).show();
                Log.d( "LoginActivity", errorCode + ":" + msg );
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "LoginActivity", "请求结束" );
            }
        } );
    }
}
