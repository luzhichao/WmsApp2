package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.adapter.ExperienceAdapter;
import com.linkingwin.elearn.teacher.adapter.SchoolAdapter;
import com.linkingwin.elearn.teacher.model.ExperienceVO;
import com.linkingwin.elearn.teacher.model.SchoolVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Experience extends BaseActivity {
    @BindView(R.id.title_experience)
    TitleView titleView;
    @BindView(R.id.lv_experience)
    ListView lv_experience;
    private ArrayList<ExperienceVO> mDatas;
    private ExperienceAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.list_experience );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        //1.初始化title
        initTitle();
        //2.初始化列表
        initListView();
    }

    /**
     * 实现返回刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        initListView();
    }

    /**
     * 初始化list的界面数据
     */
    private void initListView() {
        mDatas = new ArrayList<ExperienceVO>();
        String exp="";
        if(ElearnApplication.teachBusinessInfo!=null){
        exp=ElearnApplication.teachBusinessInfo.getExperience();}
        if(exp==null || "".equals( exp )) exp="[]";
        JSONArray jsonArray = JSONArray.parseArray( exp );
        int size = -1;
        if (jsonArray != null) size = jsonArray.size();
        //将数据装到集合中去
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                ExperienceVO model = jsonArray.getObject( i, ExperienceVO.class );
                mDatas.add( model );
            }
        }
        myAdapter = new ExperienceAdapter( this, mDatas, R.layout.list_item_experience );
        lv_experience.setAdapter( myAdapter );
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        titleView.setTitleText( "教学经历", R.color.titleWhite)
        .setSubTitle( "新增", R.color.titleWhite, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.toOtherPage( getContext(), ExperienceEdit.class, false, null );
            }
        } );
    }
}
