package com.linkingwin.education.studyonline.common.base.presenter;

import android.util.Log;
import android.widget.Toast;

import com.linkingwin.education.studyonline.common.base.BasePresenter;
import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.interfaces.Callback;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;
import com.linkingwin.education.studyonline.common.http.Response;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.utils.RegexUtils;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.util.EMClientUtils;

import org.apache.commons.lang3.StringUtils;
import org.xutils.x;

import java.util.concurrent.Callable;

/**
 * @Desc 登录login
 * @Author Willowgao
 * @Version V1.0
 * @Date 2017/12/26
 */

public class LoginPresenter extends BasePresenter {

    public static LoginPresenter newInstance() {
        return new LoginPresenter();
    }

    public void login(final LoginUserVO user, final Callable callable) {
        if (StringUtils.isEmpty (user.getUsername ())) {
            ToastUtils.show ("用户名未填写!");
            return;
        }
        if (!RegexUtils.matches (RegexUtils.Regex.MOBILE, user.getUsername ())) {
            ToastUtils.show ("请填写正确的手机号!");
            return;
        }
        if (StringUtils.isEmpty (user.getPassword ())) {
            ToastUtils.show ("登录密码未填写!");
            return;
        }

        Api.LOGIN.request (user, new DefaultHttpCallback () {
            @Override
            public void onSucceed(Response response) {
                user.setToken ((String) response.getResult ());
                //保存用户信息
                MyApplication.getInstance ().setUser (user);
                //环信用户登陆
                EMClientUtils.login (user.getUsername (), EMClientUtils.EMC_COMMON_PWD,
                        new Callback.OnSuccess () {
                            public void run(Object result) {
                                Log.d ("main", "登录聊天服务器成功！");
                                try {
                                    callable.call();
                                } catch (Exception e) {
                                    ToastUtils.show(e.getMessage());
                                }
                                ToastUtils.show("登录成功");
                            }
                        },
                        new Callback.OnFail () {
                            @Override
                            public void run(String errorMessage, Exception re) {
                                Log.d ("main", errorMessage);
                                ToastUtils.show(errorMessage);
                            }
                        }
                );
            }

            @Override
            public void onFail(Response response) {
                Toast.makeText( x.app(), response.getErrorMsg (), Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show ("网络异常登陆失败,请稍后再试");
            }
        });
    }
}
