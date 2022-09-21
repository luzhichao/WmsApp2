package com.linkingwin.education.studyonline.common.http;

import org.xutils.common.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class HttpCallback<ResultType> {

    public Class<?> returnClass;

    {
        Type type = this.getClass ().getGenericSuperclass ();
        returnClass = findReturnType(this.getClass ());
    }

    /**
     * 对象创建时递归查找返回的对象类型
     *
     * @param httpCallback
     * @return
     */
    private Class<?> findReturnType(Class<? extends HttpCallback> httpCallback) {
        Type type = httpCallback.getGenericSuperclass ();
        if (type instanceof ParameterizedType) {
            Type returnType = ((ParameterizedType) type).getActualTypeArguments ()[0];
            if (returnType instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) returnType).getRawType ();
            } else if (returnType instanceof TypeVariable) {
                throw new IllegalArgumentException (
                        "not support callback type " + returnType.toString ());
            } else {
                return (Class<?>) returnType;
            }
        }
        return findReturnType((Class<? extends HttpCallback>) httpCallback.getSuperclass ());
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public abstract void onSuccess(ResultType result);

    public abstract void onError(Throwable ex, boolean isOnCallback);

    public abstract void onCancelled(Callback.CancelledException cex);

    public abstract void onFinished();
}
