package com.linkingwin.education.studyonline.common.http;

import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;

/**
 * @描述 http请求接口
 * @作者 gsh
 * @时间 2017/3/28 17:32
 */

public interface IHttpService {
//
    public interface RequestCallback<ResultType> extends Callback.CommonCallback {
        void onFail(String errMsg);
    }
//
//    /**
//     * @param url
//     * @param requestBody     如果需要上传图片，需要传入Key为picurl的ArrayList对象
//     * @param requestCallback
//     * @throws Exception
//     */
//    void postMultipartBody(String url, JSONObject requestBody, RequestCallback requestCallback) throws Exception;
//
//
//    /**
//     * 通用请求服务端数据
//     *
//     * @param url             请求url
//     * @param requestBody
//     * @param requestCallback
//     * @throws Exception
//     */
//
//
//    void post(String url, JSONObject requestBody, RequestCallback requestCallback) throws Exception;
}
