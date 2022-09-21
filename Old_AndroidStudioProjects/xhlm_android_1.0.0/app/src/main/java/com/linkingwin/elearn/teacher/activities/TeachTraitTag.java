package com.linkingwin.elearn.teacher.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class TeachTraitTag extends BaseActivity {
    @BindView(R.id.title_teach_trait)
    TitleView titleView;

    LabelAdapter selectedAdapter;
    LabelAdapter allAdapter;

    View.OnClickListener saveListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_trait_label);
        ButterKnife.bind(this);
        setTranslucentStatusBar(true);
        initListener();
        titleView.setTitleText("教学特点", R.color.titleWhite).setSubTitle("保存", R.color.titleWhite, saveListener);
        initTag();
    }

    @OnClick(R.id.tv_add_custom_label)
    public void addCustomTag(){
        Intent intent = new Intent();
        intent.setClass(TeachTraitTag.this, CustomPeculiarTag.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);//将Bundle添加到Intent,也可以在Bundle中添加相应数据传递
        startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值(可以随便写)
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        //保存
        saveListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                List<String> value = new ArrayList<>();
                for (LabelVO tmp : selectedAdapter.getAllItem()) {
                    value.add(tmp.getValue());
                }
                //发起网络请求
                RequestParams params = RequestParamsBuilder.buildRequestParams(TeachTraitTag.this);
                if (value.size() >= 0) {
                    String peculiarTag = String.join(",", value);
                    params.addFormDataPart("peculiarTag", StringUtils.isEmpty(peculiarTag)?"@" : peculiarTag);
                    HttpRequest.post(Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                        protected void onSuccess(JSONObject jsonObject) {
                            super.onSuccess(jsonObject);
                            String message = jsonObject.getString("msg");
                            if (jsonObject.getBoolean("success")) {
                                TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                                teachBusinessInfo.setPeculiarTag(peculiarTag);
                                //1.提交成功更新本地的shared数据
                                Tools.setShareInfo(TeachTraitTag.this, "TeachBusinessInfo", teachBusinessInfo);
                                //2.提交成功更新全局变量
                                ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                                //3.提交成功
                                makeText(TeachTraitTag.this, "保存成功", LENGTH_SHORT).show();
                                //4.打印日志
                                Log.d("TeachTraitTagSave", message + "");
                            } else {
                                makeText(TeachTraitTag.this, "提交失败", LENGTH_SHORT).show();
                                Log.d("TeachTraitTagSave", message + "");
                            }
                        }

                        //请求失败（服务返回非法JSON、服务器异常、网络异常）
                        @Override
                        public void onFailure(int errorCode, String msg) {
                            makeText(TeachTraitTag.this, "网络原因，提交失败", LENGTH_SHORT).show();
                            Log.d("TeachTraitTagSave", errorCode + ":" + msg);
                        }

                        //请求网络结束
                        @Override
                        public void onFinish() {
                            Log.d("TeachTraitTagSave", "请求结束");
                        }
                    });
                }

            }
        };
    }

    /**
     * 初始化默认标签枚举和已选择标签
     */
    private void initTag() {
        //1.已选择标签
        String selectedTag = ElearnApplication.teachBusinessInfo.getPeculiarTag();
        List<LabelVO> selectedTagList = new ArrayList<>();
        if (!StringUtils.isEmpty(selectedTag) && !selectedTag.equals("@")) {
            List<String> tmp = new ArrayList<>(Arrays.asList(selectedTag.split(",")));
            selectedTagList = new ArrayList<>();
            for (String str : tmp) {
                LabelVO vo = new LabelVO();
                vo.setValue(str);
                vo.setTagType("peculiar");
                vo.setSelected(true);
                selectedTagList.add(vo);
            }
        }
        //2.所有标签值
        String allTag = ElearnApplication.teachTraitTag;
        List<LabelVO> allTagList = new ArrayList<>();
        if (!StringUtils.isEmpty(allTag)) {
            List<String> tmp = new ArrayList<>(Arrays.asList(allTag.split(",")));
            allTagList = new ArrayList<>();
            for (String str : tmp) {
                LabelVO vo = new LabelVO();
                vo.setValue(str);
                vo.setTagType("peculiar");
                vo.setSelected(false);
                allTagList.add(vo);
            }
        }
        //3.设置默认标签枚举的选中状态
        if (allTagList != null && selectedTagList != null) {
            for (LabelVO selected : selectedTagList) {
                for (LabelVO all : allTagList) {
                    if (selected.getValue().equals(all.getValue())) {
                        all.setSelected(true);
                    }
                }
            }
        }
        //4.初始化已选择标签Adapter
        if (selectedTagList != null) {
            selectedAdapter = new LabelAdapter(this, selectedTagList, R.layout.layout_label_set).setDeleteButton(true);
            selectedAdapter.setType("selected");
            GridView gvSelected = findViewById(R.id.gv_selected_label);
            gvSelected.setAdapter(selectedAdapter);
        }
        //5.初始化所有标签Adapter
        if (allTagList != null) {
            allAdapter = new LabelAdapter(this, allTagList, R.layout.layout_label_set).setDeleteButton(false);
            allAdapter.setType("all");
            GridView gvAll = findViewById(R.id.gv_enum_label);
            gvAll.setAdapter(allAdapter);
        }
        //联动处理
        if (allAdapter != null) {
            allAdapter.setSelectedStatus(selectedAdapter);
        }
        if (selectedAdapter != null) {
            selectedAdapter.setAllTagStatus(allAdapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String newTag = bundle.getString("newTag");
            LabelVO vo=new LabelVO();
            vo.setValue(newTag);
            vo.setTagType("peculiar");
            vo.setSelected(true);
            if(selectedAdapter!=null){
                selectedAdapter.add(vo);
            }
        }
    }
}
