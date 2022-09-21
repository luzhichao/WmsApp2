package com.linkingwin.education.studyonline.common.base.presenter;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.linkingwin.education.studyonline.common.base.BasePresenter;
import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.interfaces.Callback;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.http.IHttpService;
import com.linkingwin.education.studyonline.common.http.D;
import com.linkingwin.education.studyonline.common.utils.RegexUtils;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.linkingwin.education.studyonline.student.activity.MainActivity;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.util.EMClientUtils;

import org.apache.commons.lang3.StringUtils;
import org.xutils.x;

/**
 * @Desc 登录login
 * @Author Willowgao
 * @Version V1.0
 * @Date 2017/12/26
 */

public class LoginPresenter extends BasePresenter {

    private FragmentActivity activity;
    private IHttpService httpService;
    public LoginPresenter(FragmentActivity activity) {
        this.activity = activity;
    }

    public void login(final LoginUserVO user) {
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
            public void onSucceed(D d) {
                user.setToken ((String) d.getResult ());
                //保存用户信息
                MyApplication.getInstance ().setUser (user);
                //环信用户登陆
                EMClientUtils.login (user.getUsername (), EMClientUtils.EMC_COMMON_PWD,
                        new Callback.OnSuccess () {
                            public void run(Object result) {
                                Log.d ("main", "登录聊天服务器成功！");
                                RouterUtils.activityGoActivity (activity, MainActivity.class, null);
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
            public void onFail(D d) {
                Toast.makeText( x.app(), d.getErrorMsg (), Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show ("网络异常登陆失败,请稍后再试");
            }
        });
    }

//
//    /**
//     * 用户登录
//     */
//    public void login() {
//        //检查网络
//        if (!isNetWorkConnected()) {
//            Toast.makeText(mContext, "网络未连接，请检查您的网络!", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        rememberPwd();//记住密码
//        JSONObject loginObj = new JSONObject();
//        if (loginView.getUserName().trim().equals("")) {
//            loginView.showToast("用户名未填写!", Toast.LENGTH_SHORT);
//            return;
//        }
//        if (loginView.getUserPwd().trim().equals("")) {
//            loginView.showToast("登录密码未填写!", Toast.LENGTH_SHORT);
//            return;
//        }
//        loginObj.put("uUserCode", loginView.getUserName());
//        loginObj.put("uUserPwd", loginView.getUserPwd());
//        try {
//            loginView.showLoading("登录中...");
//            httpService.login(loginObj, new RequestCallback() {
//                @Override
//                public void onSuccess(Object result) {
//                    JSONObject obj = JSON.parseObject(result.toString());
//                    LoginUserVO user = MyApplication.getInstance().getUser();
//                    user.setToken(obj.getJSONObject("result").getString("token"));
//                    user.setUserNick(obj.getJSONObject("result").getJSONObject("userinfo").getString("uName"));
//                    user.setServices(obj.getJSONArray("services"));
//                    MyApplication.getInstance().setUser(user);
//                    loginView.toMainActivity();
//                    queryAllUserSys();
//                    syncSysData(Constants.SystemID.SAASOP);
//                }
//
//                @Override
//                public void onFail(String errMsg) {
//                    if (errMsg != null)
//                        loginView.showToast("登录失败:" + errMsg, Toast.LENGTH_SHORT);
//                    else
//                        loginView.showToast("登录异常!", Toast.LENGTH_SHORT);
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//                    loginView.hideLoading();
//                }
//
//                @Override
//                public void onFinished() {
//                    loginView.hideLoading();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 记住密码
//     */
//    public void rememberPwd() {
//        LoginUserVO user = MyApplication.getInstance().getUser();
//
//        if (loginView.isRememberPwd()) {
//            user.setUserName(loginView.getUserName());
//            user.setUserPwd(loginView.getUserPwd());
//            MyApplication.getInstance().setUser(user);
//        } else {
//            SharedPreUtil.getInstance().deleteUser();
//        }
//    }
//
//    public void queryAllUserSys() {
//        loginView.showLoading("正在同步基础数据...");
//        loginView.showSyncSysDataProgress("正在同步用户数据...");
//        JSONObject params = new JSONObject();
//        try {
//            httpService.post(Constants.SystemID.SAASOP, Constants.HttpConsts.PerfectUrls.OP_QUERY_ALL_USER, params, new RequestCallback() {
//                @Override
//                public void onSuccess(final Object result) {
//                    JSONObject obj = JSON.parseObject(result.toString());
//                    JSONArray objs = obj.getJSONArray("result");
//                    if (objs.size() > 0) {
//                        MyApplication.getInstance().setUserMap(objs);
//                    }
//
//                }
//
//                @Override
//                public void onFail(String errMsg) {
//                    loginView.showSyncSysDataProgress("用户数据同步失败!");
//                    loginView.syncSysDataFail();
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    loginView.showSyncSysDataProgress("用户数据同步失败!");
//                    loginView.syncSysDataFail();
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//                    loginView.hideLoading();
//                }
//            });
//        } catch (
//                Exception e)
//
//        {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     * 同步系统基础数据
//     */
//    public void syncSysData(final String systemId) {
//        loginView.showSyncSysDataProgress("正在同步用户系统数据...");
//        JSONObject params = new JSONObject();
//        params.put("systemId", systemId);
//        params.put("URL", Constants.HttpConsts.SYS_DIC);
//        if (systemId.equals(Constants.SystemID.SAASWECHART)) {
//            return;
//        }
//        try {
//            httpService.selectDic(params, new RequestCallback() {
//                @Override
//                public void onSuccess(final Object result) {
//                    try {
//                        syncDic2Sp(result, systemId);
//                        loginView.showSyncSysDataProgress("数据字典同步完成!");
//
//                    } catch (Exception e) {
//                        loginView.showSyncSysDataProgress("数据字典同步失败!");
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFail(String errMsg) {
//                    loginView.showSyncSysDataProgress("数据字典同步失败!");
//
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    loginView.showSyncSysDataProgress("数据字典同步失败!");
//
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//                    loginView.hideLoading();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
