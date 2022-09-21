package com.linkingwin.education.studyonline.common.http;



import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.consts.Constants;

import org.xutils.http.RequestParams;

/**
 * @描述 接口请求参数实体类
 * @作者 gsh
 * @时间 2017/4/2 10:14
 */
public class JsonParams extends RequestParams {

    /**
     * @param URL      请求url
     */
    public JsonParams(String URL) {
        super( URL, null, null, null);

        addHeader("content-Type", "application/json");
        // 添加公共参数
        addHeader(Constants.HttpConsts.TOKEN_ID, MyApplication.getInstance().getUser().getToken());
        // 将post请求的body参数以json形式提交
        setAsJsonContent(true);
    }
}
