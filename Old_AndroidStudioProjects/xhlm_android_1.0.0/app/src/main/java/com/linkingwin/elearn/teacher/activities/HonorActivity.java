package com.linkingwin.elearn.teacher.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.adapter.HonorAdapter;
import com.linkingwin.elearn.teacher.model.HonorVO;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.toolsfinal.StringUtils;

public class HonorActivity extends BaseActivity {

    @BindView(R.id.title_honor)
    TitleView titleView;
    @BindView(R.id.lv_honor)
    ListView lv_honor;

    private ArrayList<HonorVO> allData = new ArrayList<>();//所有数据
    private HonorVO currData;//当前行数据
    private HonorAdapter honorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.list_honor );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );

        //TODO init Title and listener
        //新增
        titleView.setTitleText("奖励荣誉", R.color.titleWhite).setSubTitle("新增", R.color.titleWhite, v -> {
            //TODO 跳转到新增页面
            Intent intent = new Intent(HonorActivity.this, HonorAddOrEditActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("allData", (Serializable)allData);
            bundle.putBoolean("isEdit", false);
            intent.putExtras(bundle);
            HonorActivity.this.startActivity(intent);
        });
    }


    /**
     * 实现返回刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        //init data
        initListHonor();
    }

    public void initListHonor(){
        allData = new ArrayList<>();
        String honorStr = ElearnApplication.teachBusinessInfo.getHonor();
        if(!StringUtils.isBlank(honorStr)){
            JSONArray jsonArray = JSONObject.parseArray(honorStr);
            for(int i=0;i<jsonArray.size();i++){
                HonorVO parse = JSONObject.parseObject(jsonArray.getString(i), HonorVO.class);
                allData.add(parse);
            }
        }
        honorAdapter = new HonorAdapter(this, allData, R.layout.list_honor_item);
        lv_honor.setAdapter(honorAdapter);
    }

}

