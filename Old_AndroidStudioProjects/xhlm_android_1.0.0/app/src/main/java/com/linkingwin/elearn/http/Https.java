package com.linkingwin.elearn.http;

import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.constant.XhlmConstant;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.Map;

public class Https {
    /**
     * 异步POST请求, 带自定义请求头
     * @param uri 请求地址
     * @param mediaType content-Type
     * @param headers 自定义请求头
     * @param params 参数
     * @param callback 回调函数
     * @param <T> 返回的对象类型
     */
    public static <T> void request(HttpMethod method, String uri, MediaType mediaType, Map<String, String> headers, Map<String, Object> params, final HttpCallback<T> callback) {
        RequestParams _params = new RequestParams();
        _params.addHeader("content-Type", mediaType.getValue ());
        if (null != headers && !headers.isEmpty ()) {
            for (Map.Entry<String, String> entry : headers.entrySet ()) {
                _params.addHeader(entry.getKey (), entry.getValue ());
            }
        }

        _params.setUri (uri);
        // 添加公共参数
        _params.addHeader(XhlmConstant.HTTP_ACCESS_TOKEN, ElearnApplication.accessToken);
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
     * @param <T>
     * @return 返回的对象
     * @throws Throwable
     */
    public static <T> T postSync(HttpMethod method, String uri, MediaType mediaType, Map<String, String> headers, Map<String, Object> params, Class<T> resultType) throws Throwable {
        RequestParams _params = new RequestParams();
        _params.addHeader("content-Type", mediaType.getValue ());
        if (null != headers && !headers.isEmpty ()) {
            for (Map.Entry<String, String> entry : headers.entrySet ()) {
                _params.addHeader(entry.getKey (), entry.getValue ());
            }
        }
        _params.setUri (uri);
        // 添加公共参数
        _params.addHeader(XhlmConstant.HTTP_ACCESS_TOKEN, ElearnApplication.accessToken);
        // 将post请求的body参数以json形式提交
        if (MediaType.APPLICATION_JSON_BODY.equals (mediaType)) {
            _params.setAsJsonContent(true);
        }
        return XHttps.requestSync (method, _params, resultType);
    }


    /**
     * 异步POST请求
     *
     * @param uri 请求地址
     * @param mediaType content-Type
     * @param params 参数
     * @param callback 回调函数
     * @param <T> 返回的对象类型
     */
    public static <T> void post(String uri, MediaType mediaType, Map<String, Object> params, final HttpCallback<T> callback) {
        request(HttpMethod.POST, uri, mediaType, null, params, callback);
    }

    /**
     * 同步POST请求
     * @param uri 请求地址
     * @param mediaType content-Type
     * @param params 参数
     * @param resultType 返回的对象类型
     * @param <T>
     * @return 返回的对象
     * @throws Throwable
     */
    public static <T> T postSync(String uri, MediaType mediaType, Map<String, Object> params, Class<T> resultType) throws Throwable {
        return postSync(HttpMethod.POST, uri, mediaType, null, params, resultType);
    }


    /**
     * 异步GET请求
     *
     * @param uri 请求地址
     * @param mediaType content-Type
     * @param params 参数
     * @param callback 回调函数
     * @param <T> 返回的对象类型
     */
    public static <T> void get(String uri, MediaType mediaType, Map<String, Object> params, final HttpCallback<T> callback) {
        request(HttpMethod.GET, uri, mediaType, null, params, callback);
    }

    /**
     * 同步GET请求
     * @param uri 请求地址
     * @param mediaType content-Type
     * @param params 参数
     * @param resultType 返回的对象类型
     * @param <T>
     * @return 返回的对象
     * @throws Throwable
     */
    public static <T> T getSync(String uri, MediaType mediaType, Map<String, Object> params, Class<T> resultType) throws Throwable {
        return postSync(HttpMethod.GET, uri, mediaType, null, params, resultType);
    }

}
