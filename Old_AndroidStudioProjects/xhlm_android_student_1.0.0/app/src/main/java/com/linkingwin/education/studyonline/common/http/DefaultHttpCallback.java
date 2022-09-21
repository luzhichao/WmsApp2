package com.linkingwin.education.studyonline.common.http;

import com.linkingwin.education.studyonline.common.utils.ToastUtils;

import org.xutils.common.Callback;

public abstract class DefaultHttpCallback extends HttpCallback<Response> {

    @Override
    public void onSuccess(Response response) {
        if (response.isSuccess ()) {
            onSucceed(response);
        } else {
            onFail(response);
        }
    }

    /**
     * 执行成功
     * @param response
     */
    public abstract void onSucceed(Response response);

    /**
     * 执行失败
     * @param response
     */
    public abstract void onFail(Response response);

    @Override
    public void onCancelled(Callback.CancelledException cex) {
        ToastUtils.show ("请求已被取消");
    }

    @Override
    public void onFinished() {

    }
}
