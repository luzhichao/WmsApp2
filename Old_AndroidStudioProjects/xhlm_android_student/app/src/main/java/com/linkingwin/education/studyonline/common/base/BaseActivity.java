package com.linkingwin.education.studyonline.common.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.Toast;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.annotation.Authority;
import com.linkingwin.education.studyonline.common.base.interfaces.IBaseView;
import com.linkingwin.education.studyonline.common.utils.AuthorityUtils;
import com.linkingwin.education.studyonline.common.utils.BeanUtils;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.common.utils.ToolbarUtils;
import com.linkingwin.education.studyonline.student.activity.LoginActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;

/**
 * @描述 基础activity
 * @作者 gsh
 * @时间 2017/3/29 14:04
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final int CHOICE_RESULT_CODE = 500;
    public static final String RESULT_KEY = "msg";

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    //进度条
    private ProgressDialog progressDialog;
//    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication application = MyApplication.getInstance().addActivity(this);
        boolean hasAuth = AuthorityUtils.auth(application, BeanUtils.getAnnotation(this.getClass (), Authority.class));
        if (!hasAuth) {
            RouterUtils.activityGoActivity (this, LoginActivity.class, null, true);
        }
        x.view().inject(this);
//        unbinder = ButterKnife.bind(this);
        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        ToolbarUtils.initToolbar (this, toolbar, ToolbarUtils.findToolbarContent (this.getClass ()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            MyApplication.getInstance().finishActivity(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *  获得页面参数
     * @param paramKey
     * @param <T>
     * @return
     */
    public <T extends Serializable> T getParam(String paramKey) {
        return (T) getIntent().getSerializableExtra (paramKey);
    }
}
