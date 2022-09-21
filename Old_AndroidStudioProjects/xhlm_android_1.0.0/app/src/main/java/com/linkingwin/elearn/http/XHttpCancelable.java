package com.linkingwin.elearn.http;

import android.util.Log;
import android.widget.Toast;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

public class XHttpCancelable<T> implements Callback.CommonCallback<T> {

    private RequestParams params;
    private HttpCallback<T> callback;

    public XHttpCancelable(RequestParams params, HttpCallback<T> callback) {
        this.params = params;
        this.callback = callback;
    }

    public void onSuccess(T result) {
        Log.e("onSuccess", result.toString());
        callback.onSuccess(result);
    }

    public void onError(Throwable ex, boolean isOnCallback) {
        Log.e("onError", params.getUri () + "/" + params.toJSONString());
        ex.printStackTrace();
        callback.onError(ex, isOnCallback);
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
        callback.onCancelled(cex);
    }

    public void onFinished() {
        callback.onFinished();
    }
}
