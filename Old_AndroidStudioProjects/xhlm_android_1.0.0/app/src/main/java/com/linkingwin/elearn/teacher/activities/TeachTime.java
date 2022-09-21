package com.linkingwin.elearn.teacher.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.MainActivity;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.LoginActivity;
import com.linkingwin.elearn.teacher.adapter.SchoolAdapter;
import com.linkingwin.elearn.teacher.adapter.TeachTimeAdapter;
import com.linkingwin.elearn.teacher.model.SchoolVO;
import com.linkingwin.elearn.teacher.model.TeachTimeVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.RequestBody;

public class TeachTime extends BaseActivity {


    /**
     * GisrView适配
     */
    private TeachTimeAdapter myAdapter;

    @BindView(R.id.gv_teach_time)
    GridView gv_teach_time;
    @BindView(R.id.title_teach_time)
    TitleView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_teaching_time );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        initTitle();
        initAdapterVO();
    }

    /**
     * 保存提交
     */
    @OnClick(R.id.bt_save)
    public void saveAll(View view) {
        List<TeachTimeVO> allVO = myAdapter.getAllItem();
        List<TeachTimeVO> tmpVO = new ArrayList<>(  );
        for (TeachTimeVO vo : allVO) {
            if (!"1".equals( vo.getType() ) && !"0".equals( vo.getIsConfirm() )) {
                vo.setTeachId( ElearnApplication.teachBusinessInfo.getUserId() );
                tmpVO.add( vo );
            }
        }
        String teachTimeLisJSON = JSONArray.toJSONString( tmpVO );
//        //老师的账号
//        String ACCOUNT = ElearnApplication.teachBusinessInfo.getUsername();
        //获取Request
        RequestParams params = RequestParamsBuilder.buildRequestParams( this );
        params.setRequestBody( "application/json; charset=utf-8",teachTimeLisJSON );
        //发起请求
        HttpRequest.post( Api.POST_SAVE_TEACH_TIME, params, new JsonHttpRequestCallback() {

            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String message = jsonObject.getString( "msg" );
                Tools.saveInfo2SharedForStr( getContext(),"times", teachTimeLisJSON);
                Toast.makeText( getContext(), message, Toast.LENGTH_SHORT ).show();
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText( getContext(), "网络异常，登陆失败", Toast.LENGTH_SHORT ).show();
                Log.d( "TeachTime", errorCode + ":" + msg );
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "TeachTime", "请求结束" );
            }
        } );

    }


    /**
     * 初始化listVO对象
     */
    public void initAdapterVO() {
        List<TeachTimeVO> allVO = new ArrayList<>();//教学时间面板初始化
        List<TeachTimeVO> isConfirmVO = new ArrayList<>();//已选中教学时间的初始化
        //1.服务端获取对象todo，并赋值给isConfirmVO
        //todo,获取数据
        TeachTimeVO model = new TeachTimeVO();
        //2.从本地获取对象
        isConfirmVO=Tools.getSharedInfoList( "times",Tools.getSharedPre( this ),TeachTimeVO.class );
        //3.初始化所有的时间
        for (int i = 1; i < 8; i++) {
            for (int j = 1; j < 5; j++) {
                TeachTimeVO tmpVO = new TeachTimeVO();
                tmpVO.setTeachWeek( i + "" );
                tmpVO.setIsConfirm( "0" );
                if (j == 1) {
                    tmpVO.setType( "1" );
                } else {
                    tmpVO.setType( "0" );
                }
                tmpVO.setTeachTime( j - 1 + "" );
                allVO.add( tmpVO );
            }
        }
        //4.已经选中的条目设置状态
        for (TeachTimeVO conVO : isConfirmVO) {
            for (TeachTimeVO vo : allVO) {
                if ((conVO.getTeachWeek() + "" + conVO.getTeachTime()).equals( vo.getTeachWeek() + "" + vo.getTeachTime() )) {
                    vo.setIsConfirm( "1" );
                    vo.setId( conVO.getId() );
                }
            }
        }
        //5.绑定gridview与adapter，在gridview中展示list中的内容
        initListView( allVO );
    }

    /**
     * 方法:初始化ListViewAdapter
     */
    private void initListView(List<TeachTimeVO> allVO) {
        myAdapter = new TeachTimeAdapter( this, allVO, R.layout.grid_item_teach_time );
        gv_teach_time.setAdapter( myAdapter );
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "设置授课时间", R.color.titleWhite );
    }
}
