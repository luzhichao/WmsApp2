package com.linkingwin.education.studyonline.student.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseFragment;
import com.linkingwin.education.studyonline.common.base.annotation.ToolbarContent;

import org.xutils.view.annotation.ContentView;

/**
 * Created by afanbaby on 2018/2/2.
 */
@ContentView(R.layout.fragment_test)
@ToolbarContent(resId = -1, title = "")
public class TestFragment extends BaseFragment {

    private TextView tv;

    public static TestFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = (TextView) view.findViewById(R.id.fragment_test_tv);
        Bundle bundle = getArguments();
        if (bundle != null) {
             tv.setText(bundle.get("name").toString());
        }
    }

}
