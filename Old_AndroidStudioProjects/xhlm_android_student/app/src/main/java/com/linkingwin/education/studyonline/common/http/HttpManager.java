package com.linkingwin.education.studyonline.common.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.consts.Constants;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import static org.xutils.x.http;

/**
 *
 *
 */

public class HttpManager {

    public static void postMultipartBody(Context mContext, final RequestParams params, final IHttpService.RequestCallback requestCallback) {
        final ProgressDialog dia = new ProgressDialog (mContext);
        dia.setMessage ("正在进行数据上传，请稍后....");
        dia.show ();

        Log.i ("网络请求", params.getUri () + "/" + params.toJSONString () + "/" + MyApplication.getInstance ().getUser ().getToken ());
        if (!isNetworkConnected (x.app ())) {// 网络请求之前先检查网络是否可用
            Toast.makeText (x.app (), "网络连接异常,请检查网络连接!", Toast.LENGTH_SHORT).show ();
            requestCallback.onFinished ();
            return;
        }
        http ().post (params, new Callback.CommonCallback<String> () {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = JSON.parseObject (result);
                    if (jsonObject.getString ("state").equals ("ok")) {
                        requestCallback.onSuccess (jsonObject);
                    } else {
                        requestCallback.onFail (jsonObject.getString ("errMsg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                requestCallback.onError (ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
                dia.dismiss ();//加载完成
            }
        });

    }

    /**
     * 发送post请求
     */
    public static void post(Context mContext, final JsonParams params, final IHttpService.RequestCallback requestCallback) {

        Log.i ("网络请求", params.getUri () + "/" + params.toJSONString () + "/" + MyApplication.getInstance ().getUser ().getToken ());
        if (!isNetworkConnected (x.app ())) {// 网络请求之前先检查网络是否可用
            Toast.makeText (x.app (), "网络连接异常,请检查网络连接!", Toast.LENGTH_SHORT).show ();
            requestCallback.onFinished ();
            return;
        }

        Callback.Cancelable cancelable
                = http ().post (params,
                new Callback.CommonCallback<JsonResponse> () {
                    @Override
                    public void onSuccess(JsonResponse result) {
                        Log.e ("onSuccess", result.toString ());
//                        if (result.getState ().equals (Constants.HttpConsts.RESULT_OK)) {
//                            requestCallback.onSuccess (result.getResultMap ());
//                        } else {
//                            requestCallback.onFail (result.getErrMsg ());
//                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.e ("onError", params.getUri () + "/" + params.toJSONString ());
                        ex.printStackTrace ();
                        requestCallback.onError (ex, isOnCallback);
                        if (ex instanceof HttpException) { // 网络错误
                            HttpException httpEx = (HttpException) ex;
                            int responseCode = httpEx.getCode ();
                            String responseMsg = httpEx.getMessage ();
                            String errorResult = httpEx.getResult ();
                            Toast.makeText (x.app (), "网络连接异常,错误码：" + responseCode, Toast.LENGTH_LONG).show ();
                        } else { // 其他错误
                            Toast.makeText (x.app (), "请求错误,请稍后重试!", Toast.LENGTH_LONG).show ();
                        }
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        requestCallback.onCancelled (cex);
                    }

                    @Override
                    public void onFinished() {
                        requestCallback.onFinished ();
                    }
                });
    }

    /**
     * 同步post请求
     *
     * @param params
     * @return
     */
    public static JsonResponse postSync(JsonParams params) {
        JsonResponse result = new JsonResponse ();
        try {
            result = x.http ().postSync (params, JsonResponse.class);
            return result;
        } catch (Throwable throwable) {
//            throwable.printStackTrace ();
//            result.setState (Constants.HttpConsts.RESULT_ERR);
//            result.setExceptionDetails (throwable.getMessage ());
        }
        return result;
    }


    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService (Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo ();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable ();
            }
        }
        return false;
    }
}
