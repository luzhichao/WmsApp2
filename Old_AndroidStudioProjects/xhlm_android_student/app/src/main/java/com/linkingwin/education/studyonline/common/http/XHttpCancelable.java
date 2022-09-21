package com.linkingwin.education.studyonline.common.http;

import android.util.Log;
import android.widget.Toast;

import com.linkingwin.education.studyonline.common.utils.BeanUtils;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class XHttpCancelable implements Callback.CommonCallback<JsonResponse> {

    private RequestParams params;
    private HttpCallback httpCallback;

    public XHttpCancelable(RequestParams params, HttpCallback httpCallback) {
        this.params = params;
        this.httpCallback = httpCallback;
    }
    

    public void onSuccess(JsonResponse result) {
        Log.e("onSuccess", result.toString());
        httpCallback.onSuccess(BeanUtils.map2JavaBean (result, httpCallback.getReturnClass()));
    }

    public void onError(Throwable ex, boolean isOnCallback) {
        Log.e("onError", params.getUri () + "/" + params.toJSONString());
        ex.printStackTrace();
        httpCallback.onError(ex, isOnCallback);
        if (ex instanceof HttpException) { // 网络错误
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            String responseMsg = httpEx.getMessage();
            String errorResult = httpEx.getResult();
            Toast.makeText(x.app(), "网络连接异常,错误码：" + responseCode, Toast.LENGTH_LONG).show();
        } else { // 其他错误
            Toast.makeText(x.app(), "请求错误,请稍后重试!", Toast.LENGTH_LONG).show();
        }
    }

    public void onCancelled(Callback.CancelledException cex) {
        httpCallback.onCancelled(cex);
    }

    public void onFinished() {
        httpCallback.onFinished();
    }
}
