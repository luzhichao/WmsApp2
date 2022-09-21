package com.linkingwin.education.studyonline.common.http;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

public class D extends HashMap<String, Object> {

    /**
     * 请求执行是否成功
     */
    public boolean isSuccess() {
        return (boolean) this.get("success");
    }
    /**
     * 得到返回的异常信息
     */
    public String getErrorMsg() {
        String msg = (String) this.get ("msg");
        return StringUtils.isEmpty (msg) ?  (String) this.get ("message") : msg;
    }

    /**
     * 获得返回代码
     */
    public String getCode() {
        return (String) this.get("code");
    }

    /**
     * 获得返回的内容
     */
    public <T> T getResult() {
        return (T) (null == get("result") ? get("content") : get("result"));
    }
}
