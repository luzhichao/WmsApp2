package com.linkingwin.education.studyonline.common.http.impl;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.consts.Constants;
import com.linkingwin.education.studyonline.common.http.HttpManager;
import com.linkingwin.education.studyonline.common.http.IHttpService;
import com.linkingwin.education.studyonline.common.http.JsonParams;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @描述 http请求实现
 * @作者 gsh
 * @时间 2017/3/28 17:53
 */

public class HttpServiceImpl implements IHttpService {
//
//    private Context mContext;
//
//    public HttpServiceImpl(Context mContext) {
//        this.mContext = mContext;
//    }
//
//
//    @Override
//    public void postMultipartBody(String systemId, String url, JSONObject requestBody, RequestCallback requestCallback) throws Exception {
//        if (Constants.HttpConsts.getServerUrl(systemId) == null) {
//            return;
//        }
//        RequestParams params = new RequestParams(Constants.HttpConsts.getServerUrl(systemId) + url);//参数是路径地址
//        // 添加公共参数
//        params.addHeader(Constants.HttpConsts.SYSTTEM_ID, systemId);
//        params.addHeader(Constants.HttpConsts.TOKEN_ID, MyApplication.getInstance().getUser().getToken());
//        //创建List<KeyValue>对象
//        List<KeyValue> list = new ArrayList<>();
//        if (requestBody.get("picurl") != null) {
//            ArrayList<String> picurl = requestBody.getObject("picurl", ArrayList.class);
//            //给list中添加数据，filePah是上传的文件路径，比如sd卡中图片
//            for (String filePah : picurl) {
//                list.add(new KeyValue("file", new File(filePah.toString())));//文件流数据
//            }
//        }
//        //其它参数，根据项目而定，比如我的项目中要传入的参数是json格式的
//        list.add(new KeyValue("parameters", requestBody.toJSONString()));
//        //创建MultipartBody
//        MultipartBody body = new MultipartBody(list, "UTF-8");
//        //添加请求参数
//        params.setRequestBody(body);
//        HttpManager.postMultipartBody(mContext, params, requestCallback);
//
//    }
//
//
//    @Override
//    public void post(String url, JSONObject requestBody, RequestCallback requestCallback) throws Exception {
//        if (Constants.HttpConsts.getServerUrl(systemId) == null) {
//            return;
//        }
//        JsonParams params = new JsonParams(url);
//        params.setBodyContent(requestBody.toJSONString());
//        HttpManager.post(mContext, params, requestCallback);
//    }
//
//    @Override
//    public void login(JSONObject requestBody, IHttpService.RequestCallback requestCallback) throws Exception {
//        //校验
//        if (!requestBody.containsKey("uUserCode")) {
//            throw new Exception("用户名必填!");
//        }
//        if (!requestBody.containsKey("uUserPwd")) {
//            throw new Exception("密码必填!");
//        }
//        JsonParams params = new JsonParams(Constants.SystemID.SAASOP, Constants.HttpConsts.PerfectUrls.OP_URL_LOGIN);
//        params.setBodyContent(requestBody.toJSONString());
//        HttpManager.post(mContext, params, requestCallback);
//    }
//
//    @Override
//    public void selectDic(JSONObject requestBody, RequestCallback requestCallback) throws Exception {
//        String systemId = requestBody.getString("systemId");
//        String url = requestBody.getString("URL");
//        JsonParams params = new JsonParams(systemId, url);
//        params.setBodyContent(requestBody.toJSONString());
//        HttpManager.post(mContext, params, requestCallback);
//    }

}
