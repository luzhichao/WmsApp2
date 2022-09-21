package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.adapter.ExperienceAdapter;
import com.linkingwin.elearn.teacher.adapter.SuccessAdapter;
import com.linkingwin.elearn.teacher.model.ExperienceVO;
import com.linkingwin.elearn.teacher.model.SuccessCaseVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessCase extends BaseActivity {
    @BindView( R.id.lv_success )
    ListView lv_success;
    @BindView( R.id.title_success )
    TitleView titleView;
    private ArrayList<SuccessCaseVO> mDatas;
    private SuccessAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.list_success_case );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        initTitle();
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
        mDatas = new ArrayList<SuccessCaseVO>();
        String exp="";
        if(ElearnApplication.teachBusinessInfo!=null){
            exp=ElearnApplication.teachBusinessInfo.getSuccessCase();}
        if(exp==null || "".equals( exp )) exp="[]";
        JSONArray jsonArray = JSONArray.parseArray( exp );
        int size = -1;
        if (jsonArray != null) size = jsonArray.size();
        //将数据装到集合中去
        if (size>0){
            for(int i=0;i<size;i++){
                SuccessCaseVO model=jsonArray.getObject( i,SuccessCaseVO.class );
                mDatas.add( model);
            }
        }
        myAdapter = new SuccessAdapter(this, mDatas,R.layout.list_item_success );
        lv_success.setAdapter(myAdapter);
    }

    /**
     * 初始化title
     */
    private void initTitle(){
        //定义title的样式
        titleView.setTitleText( "成功案例", R.color.titleWhite)
        .setSubTitle( "新增", R.color.titleWhite, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.toOtherPage(getContext(),SuccessCaseEdit.class,false,null);
            }
        } );
    }
}
