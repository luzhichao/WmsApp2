package com.linkingwin.education.studyonline.student.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;
import com.linkingwin.education.studyonline.common.base.model.ResetPassword;
import com.linkingwin.education.studyonline.common.base.presenter.LoginPresenter;
import com.linkingwin.education.studyonline.common.http.D;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.utils.Asserts;
import com.linkingwin.education.studyonline.common.utils.RegexUtils;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.linkingwin.education.studyonline.common.utils.UiUtils;
import com.linkingwin.education.studyonline.student.api.Api;
import com.qmuiteam.qmui.alpha.QMUIAlphaTextView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_reset_password)
public class ResetPasswordActivity extends BaseActivity {

    //用户登录处理类
    private LoginPresenter loginPresenter = new LoginPresenter(this);

    @ViewInject(R.id.regist_login_username)
    EditText etUsername;

    @ViewInject (R.id.login_btn_sendcaptcha)
    QMUIRoundButton sendCaptachaBtn;

    @ViewInject (R.id.login_count_down)
    QMUIAlphaTextView showCountDownText;

    @ViewInject (R.id.login_captcha)
    EditText etCaptcha;

    @ViewInject (R.id.login_password)
    EditText etPassword;

    @ViewInject (R.id.login_password_again)
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
                public void onSucceed(D d) {
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
                public void onFail(D d) {
                    ToastUtils.show ("验证码已发送失败：" + d.getErrorMsg ());
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
        final ResetPassword resetPassword = new ResetPassword();
        resetPassword.setCode (etCaptcha.getText ().toString ().trim ());
        resetPassword.setMobile (etUsername.getText ().toString ().trim ());
        resetPassword.setPassword (etPassword.getText ().toString ().trim ());
        resetPassword.setPassStrength (RegexUtils.checkStregthValue (resetPassword.getPassword ()).getName ());
        String passwordAgain = etPasswordAgain.getText ().toString ().trim ();

        if (Asserts.notBlank (resetPassword.getMobile (), "手机号不能为空")
                && Asserts.notBlank (resetPassword.getCode (), "请填写验证码")
                && Asserts.notBlank (resetPassword.getPassword (), "请填写密码")
                && Asserts.notBlank (passwordAgain, "请填写再次输入密码")
                && Asserts.eq (resetPassword.getPassword (), passwordAgain, "两次密码不一致，请重新输入")) {
            Api.RESETPWD.request (resetPassword, new DefaultHttpCallback (){
                @Override
                public void onSucceed(D d) {
                    ToastUtils.show ("密码重设成功，请牢记您的新密码");
                    LoginUserVO user = new LoginUserVO();
                    user.setUsername (resetPassword.getMobile ());
                    user.setPassword (resetPassword.getPassword ());
                    loginPresenter.login(user);
                }

                @Override
                public void onFail(D d) {
                    ToastUtils.show ("密码重设失败:" + d.getErrorMsg ());
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ToastUtils.show ("密码重设失败:" + ex.getMessage ());
                }
            });
        }
    }
}