package com.linkingwin.education.studyonline.student.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseFragment;
import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.annotation.Authority;
import com.linkingwin.education.studyonline.common.base.annotation.ToolbarContent;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.student.activity.LoginActivity;
import com.linkingwin.education.studyonline.student.activity.MineBillsActivity;
import com.linkingwin.education.studyonline.student.activity.PayActivity;
import com.linkingwin.education.studyonline.student.enums.FilterBillStatus;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 *
 */
@ToolbarContent(resId = -1, title = "")
@ContentView(R.layout.fragment_mine)
@Authority(Authority.Permission.LOGIN_USER)
public class MineFragment extends BaseFragment {

    @ViewInject(R.id.lable_account)
    public TextView tvAccount;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment ();
        Bundle args = new Bundle ();
        fragment.setArguments (args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
        init();
    }

    private void init() {
        // 设置当前用户账号信息
        tvAccount.setText (MyApplication.getInstance ().getUser ().getUsername ());
    }

    @Event(value = {R.id.mine_bills, R.id.mine_wallet},type = View.OnClickListener.class)
    private void openBillView(View view){
        switch (view.getId()) {
            case R.id.mine_bills :
                    Map<String, Serializable> params = new HashMap<> ();
                    params.put ("filterBillStatus", FilterBillStatus.WATIING_FOR_PAY);
                    RouterUtils.fragmentGoActivity (this, MineBillsActivity.class, params);
                break;
            case R.id.mine_wallet :
                    RouterUtils.fragmentGoActivity(this, PayActivity.class, null);
                break;
        }

    }

    @Event(value = R.id.logout_account,type = View.OnClickListener.class)
    private void loginout(View view) {
        MyApplication.getInstance ().deleteUser();
        RouterUtils.fragmentGoActivity (this, LoginActivity.class, null , true);
    }
}
