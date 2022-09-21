package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthCentre extends BaseActivity {

    @BindView(R.id.title_real_centre)
    TitleView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_centre);
        setTranslucentStatusBar( true );
        ButterKnife.bind(this);
        titleView.setTitleText("认证中心", R.color.titleWhite);
    }

    /**
     * 实名认证
     */
    @OnClick(R.id.rl_to_real_auth)
    public void toRealAuth(){
        Tools.toOtherPage( this, RealAuth.class,false,null );
    }

    /**
     * 我的认证
     */
    @OnClick(R.id.rl_to_other_auth)
    public void toOtherAuth(){
        Tools.toOtherPage( this, OtherAuth.class,false,null );
    }
}
