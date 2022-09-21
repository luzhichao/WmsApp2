package com.linkingwin.education.studyonline.student.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.base.interfaces.Callback;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;
import com.linkingwin.education.studyonline.common.base.model.RegistUser;
import com.linkingwin.education.studyonline.common.base.presenter.LoginPresenter;
import com.linkingwin.education.studyonline.common.http.Response;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.utils.Asserts;
import com.linkingwin.education.studyonline.common.utils.RegexUtils;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.linkingwin.education.studyonline.common.utils.UiUtils;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.util.EMClientUtils;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.concurrent.Callable;

@ContentView(R.layout.activity_regist)
public class RegistActivity extends BaseActivity {

    //用户登录处理类
    private LoginPresenter loginPresenter = LoginPresenter.newInstance();

    @ViewInject (R.id.regist_login_username)
    EditText etUsername;

    @ViewInject (R.id.login_btn_sendcaptcha)
    QMUIRoundButton sendCaptachaBtn;

    @ViewInject (R.id.login_count_down)
    QMUIAlphaTextView showCountDownText;

    @ViewInject (R.id.login_captcha)
    EditText etCaptcha;

    @ViewInject (R.id.login_password)
    EditText etPassword;

    @ViewInject(R.id.login_password_again)
    EditText etPasswordAgain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    /**
     * 发送验证码
     */
    @Event(value = R.id.login_btn_sendcaptcha,type = View.OnClickListener.class)
    private void sendCaptcha(View view) {
        String username = etUsername.getText ().toString ().trim ();
        if (Asserts.notBlank (username, "手机号不能为空！")
                && Asserts.matche (username, RegexUtils.Regex.MOBILE, "请输入正确的手机号码！")) {
            Api.SEND_CAPTCHA.request (username, new DefaultHttpCallback () {
                @Override
                public void onSucceed(Response response) {
                    ToastUtils.show ("验证码已发送成功");
                    showCountDownText.setVisibility (View.VISIBLE);
                    UiUtils.countDownTimer (sendCaptachaBtn, showCountDownText,
                            1000 * 60, 1000, new UiUtils.CountDownCallback () {
                                public void onTick(long millisUntilFinished) { }
                                public void onFinish() {
                                    showCountDownText.setVisibility (View.INVISIBLE);
                                }
                            });
                }
                @Override
                public void onFail(Response response) {
                    ToastUtils.show ("验证码已发送失败：" + response.getErrorMsg ());
                }
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ToastUtils.show ("验证码已发送失败：" + ex.getMessage ());
                }
            });
        }
    }

    /**
     * 点击注册
     */
    @Event(value = R.id.login_btn_regist,type = View.OnClickListener.class)
    private void regist(View view) {
        final RegistUser registUser = new RegistUser();
        registUser.setCode (etCaptcha.getText ().toString ().trim ());
        registUser.setMobile (etUsername.getText ().toString ().trim ());
        registUser.setPassword (etPassword.getText ().toString ().trim ());
        String passwordAgain = etPasswordAgain.getText ().toString ().trim ();

        if (Asserts.notBlank (registUser.getMobile (), "手机号不能为空")
                && Asserts.notBlank (registUser.getCode (), "请填写验证码")
                && Asserts.notBlank (registUser.getPassword (), "请填写密码")
                && Asserts.notBlank (passwordAgain, "请填写再次输入密码")
                && Asserts.eq (registUser.getPassword (), passwordAgain, "两次密码不一致，请重新输入")) {
            Api.REGISTER.request (registUser, new DefaultHttpCallback (){
                @Override
                public void onSucceed(Response response) {
                    // 环信用户信息注册
                    EMClientUtils.createAccount (registUser.getMobile (), EMClientUtils.EMC_COMMON_PWD,
                            new Callback.OnSuccess () {
                                @Override
                                public void run(Object result) {
                                    ToastUtils.show ("注册成功!");
                                    LoginUserVO user = new LoginUserVO ();
                                    user.setUsername (registUser.getMobile ());
                                    user.setPassword (registUser.getPassword ());
                                    loginPresenter.login(user, new Callable() {
                                        @Override
                                        public Object call() throws Exception {
                                            RouterUtils.activityGoActivity (RegistActivity.this, MainActivity.class, null);
                                            return null;
                                        }
                                    });
                                }
                            },
                            new Callback.OnFail () {
                                @Override
                                public void run(String errorMessage, Exception re) {
                                    ToastUtils.show ("注册失败:" + errorMessage);
                                }
                            });
                }

                @Override
                public void onFail(Response response) {
                    ToastUtils.show ("注册失败:" + response.getErrorMsg ());
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ToastUtils.show ("注册失败:" + ex.getMessage ());
                }
            });
        }
    }
}
