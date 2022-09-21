package com.linkingwin.elearn.teacher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Wallet extends BaseActivity {

    @BindView(R.id.tv_wallet_title)
    TitleView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ButterKnife.bind(this);
        setTranslucentStatusBar( true );
        titleView.setTitleText("我的钱包", R.color.titleBlack);

    }
}
