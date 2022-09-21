package com.linkingwin.elearn.teacher.activities;

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
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class SchoolTag extends BaseActivity {
    @BindView(R.id.title_school_tag)
    TitleView titleView;

    @BindView(R.id.et_school_tag)
    EditText et_school_tag;

    private View.OnClickListener saveListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_tag);
        ButterKnife.bind(this);
        setTranslucentStatusBar(true);
        initListener();
        titleView.setTitleText("教学经历", R.color.titleWhite)
                .setSubTitle("保存", R.color.titleWhite, saveListener);

    }

    private String getExtra() {
        return getIntent().getStringExtra("schoolTag");
    }

    private void initListener() {
        saveListener = v -> {
            String newTag = et_school_tag.getText().toString();
            if (StringUtils.isEmpty(newTag)) {
                Toast.makeText(SchoolTag.this, "学校标签不可为空", Toast.LENGTH_SHORT).show();
                return;
            } else {
                String tmp = getExtra();
                saveSchoolTag(StringUtils.isEmpty(tmp) || "@".equals(tmp) ? newTag : tmp + "," + newTag);
            }
        };
    }

    private void saveSchoolTag(String tags) {
        //发起网络请求
        RequestParams params = RequestParamsBuilder.buildRequestParams(this);
        params.addFormDataPart("schoolTag", tags);
        HttpRequest.post(Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                String message = jsonObject.getString("msg");
                if (jsonObject.getBoolean("success")) {
                    TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                    teachBusinessInfo.setSchoolTag(tags);
                    //1.提交成功更新本地的shared数据
                    Tools.setShareInfo(SchoolTag.this, "TeachBusinessInfo", teachBusinessInfo);
                    //2.提交成功更新全局变量
                    ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                    //3.提交成功
                    makeText(SchoolTag.this, "保存成功", LENGTH_SHORT).show();
                    finish();
                    //4.打印日志
                    Log.d("SchoolTagSave", message + "");
                } else {
                    makeText(SchoolTag.this, "提交失败", LENGTH_SHORT).show();
                    Log.d("SchoolTagSave", message + "");
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText(SchoolTag.this, "网络原因，提交失败", LENGTH_SHORT).show();
                Log.d("SchoolTagSave", errorCode + ":" + msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d("SchoolTag", "请求结束");
            }
        });
    }
}
