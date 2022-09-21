package com.linkingwin.elearn.http;

import org.xutils.common.Callback;

public interface HttpCallback<T> {
    void onSuccess(T result);

    void onError(Throwable ex, boolean isOnCallback);

    void onCancelled(Callback.CancelledException cex);

    void onFinished();
}
