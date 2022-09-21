package com.linkingwin.education.studyonline.student.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;
import com.linkingwin.education.studyonline.common.base.presenter.LoginPresenter;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


/**
 * 登录页
 */
@ContentView (R.layout.login_activity)
public class LoginActivity extends BaseActivity {

    //用户登录处理类
    private LoginPresenter loginPresenter = new LoginPresenter(this);

    @ViewInject (R.id.login_username)
    EditText etUsername;

    @ViewInject (R.id.login_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginUserVO user = MyApplication.getInstance().getUser();
        user.setUsername ("17786515027");
        user.setPassword ("1");
        etUsername.setText(user.getUsername ());
        etPassword.setText(user.getPassword ());
    }

    /**
     * 用户登录
     */
    @Event(value = R.id.btn_login, type = View.OnClickListener.class)
    private void login(View view) {
        LoginUserVO user = new LoginUserVO ();
        user.setUsername (etUsername.getText ().toString().trim());
        user.setPassword (etPassword.getText ().toString().trim());
        loginPresenter.login(user);
    }

    /**
     * 注册新用户
     */
    @Event(value =R.id.login_btn_regist, type = View.OnClickListener.class)
    private void regist(View view) {
        RouterUtils.activityGoActivity (LoginActivity.this, RegistActivity.class, null);
    }

    /**
     * 重设密码
     */
    @Event(value =R.id.login_btn_forgetPwd, type = View.OnClickListener.class)
    private void resetpassword(View view) {
        RouterUtils.activityGoActivity (LoginActivity.this, ResetPasswordActivity.class, null);
    }
}
