package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
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
import com.linkingwin.elearn.teacher.adapter.SchoolAdapter;
import com.linkingwin.elearn.teacher.model.SchoolVO;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class School extends BaseActivity {
    @BindView( R.id.lv_graduate_school )
    ListView lv_graduate_school;
    @BindView( R.id.title_graduate_school )
    TitleView titleView;
    private SchoolAdapter myAdapter;


    private List<SchoolVO> mDatas;//毕业学校listViewItem
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.list_school );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        initTitle();
//        initListView();
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
     * 初始化title
     */
    private void initTitle(){
        //定义title的样式
        titleView.setTitleText( "毕业院校", R.color.titleWhite)
        .setSubTitle( "新增", R.color.titleWhite, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.toOtherPage(getContext(),SchoolEdit.class,false,null);
            }
        } );
    }

    /**
     * 方法:初始化Data
     */

    private void initListView() {
        mDatas = new ArrayList<SchoolVO>();
        JSONArray jsonArray=null;

        if(ElearnApplication.teachBusinessInfo!=null){
            String gradSchool=ElearnApplication.teachBusinessInfo.getGradSchool();
            jsonArray= JSONArray.parseArray(gradSchool==null?"[]":gradSchool);
        }

        int size=-1;
        if(jsonArray!=null) size=jsonArray.size();
        //将数据装到集合中去
        if (size>0){
            for(int i=0;i<size;i++){
                SchoolVO model=jsonArray.getObject( i,SchoolVO.class );
                mDatas.add( model);
            }
        }
        myAdapter = new SchoolAdapter(this, mDatas,R.layout.list_item_school );
        lv_graduate_school.setAdapter(myAdapter);

    }
}
