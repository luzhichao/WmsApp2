package com.linkingwin.education.studyonline.common.http;

import android.util.Log;
import android.widget.Toast;

import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.consts.Constants;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;
import com.linkingwin.education.studyonline.common.base.presenter.LoginPresenter;
import com.linkingwin.education.studyonline.common.utils.BeanUtils;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.linkingwin.education.studyonline.student.activity.LoginActivity;
import com.linkingwin.education.studyonline.student.activity.MainActivity;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.util.EMClientUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.xutils.http.HttpMethod;
import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.concurrent.Callable;

import static org.xutils.x.http;

public class XHttpCancelable implements Callback.CommonCallback<JsonResponse> {

    private HttpMethod method;
    private RequestParams params;
    private HttpCallback httpCallback;
    private boolean lastRequest = false;

    public XHttpCancelable(HttpMethod method, RequestParams params, HttpCallback httpCallback) {
        this.method = method;
        this.params = params;
        this.httpCallback = httpCallback;
    }
    

    public void onSuccess(JsonResponse result) {
        Log.e("onSuccess", result.toString());
        if (!lastRequest && result.get("success").equals(false) && result.get("code").equals(401)) {
            //如果返回登录失效，则重新登录后，再次执行该请求
            final LoginUserVO user = MyApplication.getInstance().getUser();
            if (null != user && null != user.getUsername() && null != user.getPassword()) {
                LoginPresenter.newInstance().login(user, new java.util.concurrent.Callable() {
                    @Override
                    public Object call() throws Exception {
                        XHttpCancelable cancelable = new XHttpCancelable (method, params, httpCallback);
                        cancelable.setLastRequest(true);
                        params.setHeader(Constants.HttpConsts.TOKEN_ID, MyApplication.getInstance().getUser().getToken());
                        http ().request (method, params, cancelable);
                        return null;
                    }
                });
            } else {
                ToastUtils.show(result.get("message").toString());
                RouterUtils.goActivity(x.app(), LoginActivity.class, null);
            }
        } else {
            httpCallback.onSuccess(BeanUtils.map2JavaBean (result, httpCallback.getReturnClass()));
        }
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

    public void setLastRequest(boolean lastRequest) {
        this.lastRequest = lastRequest;
    }
}
