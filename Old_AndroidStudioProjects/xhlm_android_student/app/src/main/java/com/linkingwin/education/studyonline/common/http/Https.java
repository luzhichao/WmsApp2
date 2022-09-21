package com.linkingwin.education.studyonline.common.http;

import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.consts.Constants;

import org.apache.http.params.HttpConnectionParams;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.Map;

public class Https {
    /**
     * 异步POST请求, 带自定义请求头
     * @param uri 请求地址
     * @param mediaType content-Type
     * @param headers 自定义请求头
     * @param params 参数
     * @param callback 回调函数
     */
    public static <T> void request(HttpMethod method, String uri, MediaType mediaType, Map<String, String> headers, Map<String, Object> params, final HttpCallback<T> callback) {
        RequestParams _params = new RequestParams();
        _params.addHeader("content-Type", mediaType.getValue ());
        if (null != headers && !headers.isEmpty ()) {
            for (Map.Entry<String, String> entry : headers.entrySet ()) {
                _params.addHeader(entry.getKey (), entry.getValue ());
            }
        }
        if (null != params && !params.isEmpty ()) {
            for (Map.Entry<String, Object> entry : params.entrySet ()) {
                _params.addParameter (entry.getKey (), entry.getValue ());
            }
        }
        _params.setUri (uri);
        // 添加公共参数
        _params.addHeader(Constants.HttpConsts.TOKEN_ID, MyApplication.getInstance().getUser().getToken());
        // 将post请求的body参数以json形式提交
        if (MediaType.APPLICATION_JSON_BODY.equals (mediaType)) {
            _params.setAsJsonContent(true);
        }
        XHttps.request (method, _params, callback);
    }

    /**
     * 同步POST请求, 带自定义请求头
     * @param uri 请求地址
     * @param mediaType content-Type
     * @param headers 自定义请求头
     * @param params 参数
     * @param resultType 返回的对象类型
//     * @param <T>
     * @return 返回的对象
     * @throws Throwable
     */
    public static <T> T requestSync(HttpMethod method, String uri, MediaType mediaType, Map<String, String> headers, Map<String, Object> params, Class<T> resultType) throws Throwable {
        RequestParams _params = new RequestParams();
        _params.addHeader("content-Type", mediaType.getValue ());
        if (null != headers && !headers.isEmpty ()) {
            for (Map.Entry<String, String> entry : headers.entrySet ()) {
                _params.addHeader(entry.getKey (), entry.getValue ());
            }
        }
        if (null != params && !params.isEmpty ()) {
            for (Map.Entry<String, Object> entry : params.entrySet ()) {
                _params.addParameter (entry.getKey (), entry.getValue ());
            }
        }
        _params.setUri (uri);
        // 添加公共参数
        _params.addHeader(Constants.HttpConsts.TOKEN_ID, MyApplication.getInstance().getUser().getToken());
        // 将post请求的body参数以json形式提交
        if (MediaType.APPLICATION_JSON_BODY.equals (mediaType)) {
            _params.setAsJsonContent(true);
        }
        return XHttps.requestSync (method, _params, resultType);
    }
}
