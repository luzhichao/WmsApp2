package com.linkingwin.elearn.common.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

public class CityAddress {
    private HttpCycleContext context;
    private JSONObject result;
    public CityAddress(HttpCycleContext context){
        this.context=context;
    }

    /**
     * 获取Location对应的City地址信息，使用BAIDU_API
     */
    public void getCityAddress(Location location){
                //获取Request
        RequestParams params=RequestParamsBuilder.buildRequestParams2( context);
        /**
         *添加请求URL必须以此访问
         * http://api.map.baidu.com/geocoder?output=json&ak=esNPFDwwsXWtsQfw4NMNmur1&location=30.561198,114.2314
         */
        String URL=Api.BAIDU_MAP_API+"&location="+location.getLatitude()+","+location.getLongitude();
        //发起请求
        HttpRequest.get(URL,new JsonHttpRequestCallback(){
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                //请求的结果存储为全局变量
                result=jsonObject.getJSONObject( "result" );
                Log.d("CityAddress","请求成功");
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Log.d("CityAddress",errorCode+":"+msg);
            }
            //请求网络结束
            @Override
            public void onFinish() {
                Log.d("CityAddress","请求结束");
            }
        });
    }

    /**
     * 获取Location对应的City地址信息，使用安卓API
     */
    public List<Address> getAndroidAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder( (Context) context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject getResult() {
        return result;
    }
}
