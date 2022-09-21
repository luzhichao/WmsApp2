package com.linkingwin.education.studyonline.common.base.interfaces;

import com.alibaba.fastjson.JSONObject;

/**
 * @Desc
 * @Author Willowgao
 * @Version V1.0
 * @Date 2018/1/5
 */

public interface IMainView extends IBaseView {

    void showVersionUpdate(final JSONObject version);
    int getAppVersionCode();
    void toMain();
}
