package com.linkingwin.education.studyonline.common.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.annotation.Authority;
import com.linkingwin.education.studyonline.common.base.interfaces.IBaseView;
import com.linkingwin.education.studyonline.common.utils.AuthorityUtils;
import com.linkingwin.education.studyonline.common.utils.BeanUtils;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.common.utils.ToolbarUtils;
import com.linkingwin.education.studyonline.student.activity.LoginActivity;

import org.xutils.view.ViewInjectorImpl;

import static org.xutils.view.ViewInjectorImpl.*;


/**
 * @描述
 * @作者 gsh
 * @时间 2017/5/26 16:51
 */

public abstract class BaseFragment extends Fragment implements IBaseView {
    //进度条
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        MyApplication application = MyApplication.getInstance ().addActivity (getActivity ());
        boolean hasAuth = AuthorityUtils.auth(application, BeanUtils.getAnnotation(this.getClass (), Authority.class));
        if (!hasAuth) {
            RouterUtils.fragmentGoActivity (this, LoginActivity.class, null, true);
        }
        ToolbarUtils.initToolbar ((AppCompatActivity) getActivity(),((Toolbar) view.findViewById (R.id.toolbar)), ToolbarUtils.findToolbarContent (this.getClass ()));

        return view;
    }

    @Override
    public void showLoading(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void showLoading(String title, String message) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void showToast(String message, int time) {
        Toast.makeText(this.getContext(),message,time).show();
    }
}
