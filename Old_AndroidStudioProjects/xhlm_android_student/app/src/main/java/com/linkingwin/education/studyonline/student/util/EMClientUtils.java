package com.linkingwin.education.studyonline.student.util;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.linkingwin.education.studyonline.common.base.interfaces.Callback;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.student.activity.MainActivity;

public class EMClientUtils {

    public static final String EMC_COMMON_PWD = "linkingwin.123";

    public static void createAccount(String username, String password,
                                     final Callback.OnSuccess onSuccess,
                                     final Callback.OnFail onFail
                                     ) {
        try {
            // 环信用户信息注册
            EMClient.getInstance().createAccount(username, password);
        } catch (HyphenateException e) {
            onFail.run(e.getMessage (), e);
        }
    }

    public static void login(String username, String password,
                             final Callback.OnSuccess onSuccess,
                             final Callback.OnFail onFail) {
        EMClient.getInstance().login(username, password,new EMCallBack () {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                onSuccess.run (null);
            }
            @Override
            public void onProgress(int progress, String status) {

            }
            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录服务器失败！");
                onFail.run(message, null);
            }
        });
    }



}
