package com.linkingwin.education.studyonline.common.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.consts.Constants;
import com.linkingwin.education.studyonline.common.http.HttpCallback;
import com.linkingwin.education.studyonline.common.http.XHttpCancelable;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import static org.xutils.x.http;

public class XHttps {

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private static final <T> boolean checkNetworkConnected(HttpCallback<T> callback) {
        if (!isNetworkConnected(x.app())) {// 网络请求之前先检查网络是否可用
            Toast.makeText(x.app(), "网络连接异常,请检查网络连接!", Toast.LENGTH_SHORT).show();
            callback.onFinished();
            return false;
        }
        return true;
    }

    private static final <T> boolean checkNetworkConnected() {
        if (!isNetworkConnected(x.app())) {// 网络请求之前先检查网络是否可用
            Toast.makeText(x.app(), "网络连接异常,请检查网络连接!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /**
     * 异步REQUEST请求
     *
     * @param method 请求方式
     * @param params 参数
     * @param callback 回调
//     * @param <T>
     * @return
     */
    static <T> void request(HttpMethod method, final RequestParams params, final HttpCallback<T> callback) {
        Log.i("网络请求", params.getUri() + "/" + params.toJSONString() + "/" + MyApplication.getInstance().getUser().getToken());
        if (checkNetworkConnected(callback)) {
            http ().request (method, params, new XHttpCancelable (params, callback));
        }
    }
    /**
     * 同步REQUEST请求
     *
     * @param method 请求方式
     * @param params 参数
     * @param resultType
//     * @param <T>
     * @return
     * @throws Throwable
     */
    static <T> T requestSync(HttpMethod method, RequestParams params, Class<T> resultType) throws Throwable {
        if (checkNetworkConnected()) {
            return http().requestSync (method, params, resultType);
        }
        return null;
    }

}
