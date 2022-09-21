package com.linkingwin.elearn.common.http;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.RequestParams;

public class RequestParamsBuilder {
    private static String token = "";
    public static String getToken(){
        return token;
    }

    public static RequestParams buildRequestParams(HttpCycleContext context) {

        RequestParams params = new RequestParams(context);
        /**
         * 公共请求头
         */
        params.addHeader("Content-Type", "application/x-www-form-urlencoded");

        /**
         * 登录状态的token
         */
        params.addHeader("accessToken",token);
        return params;
    }

    //没有请求头的request,baiduMap的请求
    public  static RequestParams buildRequestParams2(HttpCycleContext context){
        RequestParams params = new RequestParams(context);
        return params;
    }

    public static void setToken(String s) {
        token = s;
    }

}
