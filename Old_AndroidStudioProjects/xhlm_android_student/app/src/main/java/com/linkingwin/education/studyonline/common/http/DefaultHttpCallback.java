package com.linkingwin.education.studyonline.common.http;

import com.linkingwin.education.studyonline.common.utils.ToastUtils;

import org.xutils.common.Callback;

public abstract class DefaultHttpCallback extends HttpCallback<D> {

    @Override
    public void onSuccess(D d) {
        if (d.isSuccess ()) {
            onSucceed(d);
        } else {
            onFail(d);
        }
    }

    /**
     * 执行成功
     * @param d
     */
    public abstract void onSucceed(D d);

    /**
     * 执行失败
     * @param d
     */
    public abstract void onFail(D d);

    @Override
    public void onCancelled(Callback.CancelledException cex) {
        ToastUtils.show ("请求已被取消");
    }

    @Override
    public void onFinished() {

    }
}
