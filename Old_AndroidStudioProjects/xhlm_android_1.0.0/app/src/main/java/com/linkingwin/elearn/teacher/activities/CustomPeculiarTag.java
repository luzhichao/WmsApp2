package com.linkingwin.elearn.teacher.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.adapter.LabelAdapter;
import com.linkingwin.elearn.teacher.model.LabelVO;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class CustomPeculiarTag extends BaseActivity {
    @BindView(R.id.title_custom_tag)
    TitleView titleView;
    @BindView(R.id.et_custom_tag)
    EditText et_custom_tag;
    private View.OnClickListener saveListener;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_peculiar_tag);
        ButterKnife.bind(this);
        setTranslucentStatusBar(true);
        initListener();
        titleView.setTitleText("自定义标签", R.color.titleWhite)
                .setSubTitle("确认", R.color.titleWhite, saveListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListener() {
        saveListener=v -> {
            String newTag = et_custom_tag.getText().toString();
            if (StringUtils.isEmpty(newTag)) {
                Toast.makeText(CustomPeculiarTag.this, "输入标签名称不可为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Intent intent = this.getIntent();
                Bundle bundle = intent.getExtras();
                bundle.putString("newTag", newTag);//添加要返回给页面的数据
                intent.putExtras(bundle);
                this.setResult(Activity.RESULT_OK, intent);//返回页面
                this.finish();
            }
        };
    }
}
