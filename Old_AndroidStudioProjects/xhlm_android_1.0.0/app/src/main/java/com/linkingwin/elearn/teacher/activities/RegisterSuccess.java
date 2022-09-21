package com.linkingwin.elearn.teacher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterSuccess extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_resgister_success );
        ButterKnife.bind( this );
    }

    @OnClick(R.id.tv_to_homepage)
    public void toHomePage() {
        Tools.toOtherPage( this, HomePage.class, true, null );
    }

    @OnClick(R.id.bt_apply_subject)
    public void toApplySubject() {
        Tools.toOtherPage( this, TeacherApplySubject.class, true, null );
    }
}
