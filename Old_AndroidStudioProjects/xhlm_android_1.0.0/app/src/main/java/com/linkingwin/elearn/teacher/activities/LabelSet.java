package com.linkingwin.elearn.teacher.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.adapter.LabelAdapter;
import com.linkingwin.elearn.teacher.model.LabelVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;

public class LabelSet extends BaseActivity {
    @BindView(R.id.title_label_set)
    TitleView titleView;

    @BindView(R.id.tv_peculiar_tip)
    TextView tv_peculiar_tip;
    @BindView(R.id.tv_school_tip)
    TextView tv_school_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_set);
        ButterKnife.bind(this);
        setTranslucentStatusBar(true);
        titleView.setTitleText("标签设置", R.color.titleWhite);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTag("peculiar");
        initTag("school");
    }

    //初始化已选择学校标签和教学特点
    private void initTag(String tagType) {
        //1.读取Tag并转换为ListVO
        List<String> tagList = null;
        List<LabelVO> labelVOS = new ArrayList<>();
        GridView gridView = null;
        LabelAdapter tagAdapter = null;
        String tagString = "";
        //peculiar,教学特点,school,学校标签
        if ("peculiar".equals(tagType)) {
            tagString = ElearnApplication.teachBusinessInfo.getPeculiarTag();
            gridView = findViewById(R.id.gv_peculiar);
        }
        if ("school".equals(tagType)) {
            tagString = ElearnApplication.teachBusinessInfo.getSchoolTag();
            gridView = findViewById(R.id.gv_school);
        }

        if (!StringUtils.isEmpty(tagString) && gridView != null && !tagString.equals("@")) {
            tagList = new ArrayList<>(Arrays.asList(tagString.split(",")));
            for (String str : tagList) {
                LabelVO vo = new LabelVO();
                vo.setValue(str);
                vo.setTagType(tagType);
                vo.setSelected(true);
                labelVOS.add(vo);
            }
            tagAdapter = new LabelAdapter(this, labelVOS, R.layout.layout_label_set)
                    .setDeleteButton(true)
                    .setDeleteSubmitHttp(true);
            gridView.setAdapter(tagAdapter);
            if ("peculiar".equals(tagType)) {
                tv_peculiar_tip.setVisibility(View.GONE);
            }
            if ("school".equals(tagType)) {
                tv_school_tip.setVisibility(View.GONE);
            }
        } else {
            if ("peculiar".equals(tagType)) {
                tv_peculiar_tip.setVisibility(View.VISIBLE);
            }
            if ("school".equals(tagType)) {
                tv_school_tip.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.tv_add_teach_label)
    public void addPeculiarTag() {
        Tools.toOtherPage(this, TeachTraitTag.class, false, null);
    }

    @OnClick(R.id.tv_add_school_tag)
    public void addSchoolTag() {
        JSONObject extra;
        String extraStr=ElearnApplication.teachBusinessInfo.getSchoolTag();

        if(StringUtils.isEmpty(extraStr)){
            extra=null;
        }else{
            extra=new JSONObject();
            extra.put("schoolTag", extraStr);
        }
        //需要把扩展参数传递到编辑页面
        Tools.toOtherPage(this,SchoolTag.class ,false , extra);
    }
}