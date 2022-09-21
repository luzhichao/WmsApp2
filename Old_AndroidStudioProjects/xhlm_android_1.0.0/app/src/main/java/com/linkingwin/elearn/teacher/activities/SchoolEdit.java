package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.SnowFlakeUtil;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.DialogFromBottom;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.ExperienceVO;
import com.linkingwin.elearn.teacher.model.SchoolVO;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.linkingwin.elearn.teacher.widget.EnumDialog;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class SchoolEdit extends BaseActivity {
    @BindView(R.id.title_school_edit)
    TitleView titleView;
    @BindView(R.id.tv_school_name)
    TextView tv_school_name;
    @BindView(R.id.tv_begin_date)
    TextView tv_begin_date;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.tv_education)
    TextView tv_education;
    @BindView(R.id.tv_major)
    TextView tv_major;
    @BindView(R.id.bt_del_item)
    Button bt_del_item;

    private EnumDialog enumDialog;
    private List<SchoolVO> allSchool;
    private SchoolVO schoolVO;
    private String whichOne;
    private String action;
    private View.OnClickListener saveListener;//保存按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_school_edit );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        //1.初始化保存按钮监听
        initSaveListener();
        //2.初始化删除按钮的监听
        initDelListener();
        //3.初始化titile的样式
        initTitle();
        //4.获取从毕业院校界面跳转的schoolVO的参数
        initExtra();
        //5.初始化界面数据
        initData();
    }

    /**
     * 获取传入参数，并初始化对象内容
     */
    private void initExtra() {
        //2.获取操作类型,edit编辑，add新增
        action = getIntent().getStringExtra( "action" );
        //3.判断生成vo和List对象
        String tmpSchool = getIntent().getStringExtra( "schoolVO" );
        String tmpAllSchool = getIntent().getStringExtra( "allSchool" );
        if (tmpSchool == null || tmpAllSchool == null) {
            String schoolJSONStr = "";
            if (ElearnApplication.teachBusinessInfo != null)
                schoolJSONStr = ElearnApplication.teachBusinessInfo.getGradSchool();
            schoolVO = new SchoolVO();
            allSchool = JSONArray.parseArray( (schoolJSONStr == null || "".equals( schoolJSONStr )) ? "[]" : schoolJSONStr, SchoolVO.class );
        } else {
            schoolVO = JSONObject.parseObject( tmpSchool, SchoolVO.class );
            allSchool = JSONObject.parseArray( tmpAllSchool, SchoolVO.class );
        }
        if ("edit".equals( action )) {
            //4.初始化界面数据
            initData();
            //5.初始化删除按钮的监听
            initDelListener();
        } else {
            bt_del_item.setVisibility( View.INVISIBLE );
        }
    }

    /**
     * 开始日期点击
     *
     * @param v
     */
    @OnClick(R.id.rl_begin_date)
    public void openStartDate(View v) {
        //选择的哪个日期控件
        whichOne = "begin";
        //1.初始化bottomeDialog的相关视图
        initDialog();
        //2.设置dialog的title
        enumDialog.setTitle( "选择开始日期" );
        //3.点击选择开始日期控件，打开开始日期的控件，隐藏结束日期的控件
        enumDialog.setVisiable( R.id.ll_date, View.VISIBLE );
        //4.初始化显示的位置
        String date = tv_begin_date.getText().toString();
        enumDialog.setDatePosition( date );
    }

    /**
     * 结束日期点击
     *
     * @param v
     */
    @OnClick(R.id.rl_end_date)
    public void openEndtDate(View v) {
        //选择的哪个日期控件
        whichOne = "end";
        //1.初始化bottomeDialog的相关视图
        initDialog();
        //2.设置dialog的title
        enumDialog.setTitle( "选择结束日期" );
        //3.点击选择开始日期控件，打开开始日期的控件，隐藏结束日期的控件
        enumDialog.setVisiable( R.id.ll_date, View.VISIBLE );
        //4.初始化显示的位置
        String date = tv_end_date.getText().toString();
        enumDialog.setDatePosition( date );
    }

    /**
     * 选择学历
     */
    @OnClick(R.id.rl_education)
    public void openEducation(View view) {
        //选择的哪个控件
        whichOne = "education";
        //1.初始化bottomeDialog的相关视图
        initDialog();
        //2.设置dialog的title
        enumDialog.setTitle( "选择学历" );
        //4.生成学历枚举和初始化位置
        JSONObject educations = ElearnApplication.educations;
        List<String> listEducation = new ArrayList<>();
        int position = 0;
        for (Map.Entry<String, Object> entry : educations.entrySet()) {
            listEducation.add( entry.getValue() + "" );
            if (entry.getValue() == tv_education.getText().toString()) {
                position = Integer.parseInt( entry.getKey() ) - 1;
            }
        }
        enumDialog.setWheelData( R.id.wv_education, listEducation );
        enumDialog.setWheelPosition( R.id.wv_education, position );
        //5.点击学历控件，学历dialog显示
        enumDialog.setVisiable( R.id.ll_education, View.VISIBLE );
    }

    /**
     * 初始化底部页面
     */
    private void initDialog() {
        //初始化
        enumDialog = new EnumDialog();
        enumDialog.initDialog( this );
        //监听
        enumDialog.setCancelOnclickListener( new ClickAction() );
        enumDialog.setConfirmOnclickListener( new ClickAction() );
    }


    /**
     * 初始化删除条目按钮的监听
     */
    private void initDelListener() {
        bt_del_item.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //1.剔除被删除的School
                int size = allSchool.size();
                String tmpID=schoolVO.getId();
                for (int i = 0; i < size; i++) {
                    SchoolVO tmpVO=allSchool.get( i );
                    if (!StringUtils.isBlank( tmpID ) && tmpID.equals( tmpVO.getId() )) {
                        allSchool.remove( i );
                        break;
                    }
                    if(StringUtils.isBlank( tmpID )){
                        allSchool.remove( schoolVO );
                        break;
                    }
                }
                //2.需要修改的内容
                final String allSchoolJson = JSONObject.toJSONString( allSchool );
                //3.获取请求参数
                RequestParams params = RequestParamsBuilder.buildRequestParams( SchoolEdit.this );
                params.addFormDataPart( "gradSchool", allSchoolJson );
                HttpRequest.post( Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess( jsonObject );
                        String msg = jsonObject.getString( "msg" + "" );
                        if (jsonObject.getBoolean( "success" )) {
                            TeachBusinessInfo teachBusinessInfo = Tools.getSharedInfo( "TeachBusinessInfo", Tools.getSharedPre( getContext() ), TeachBusinessInfo.class );
                            teachBusinessInfo.setGradSchool( allSchoolJson );
                            //1.提交成功更新本地的shared数据
                            Tools.setShareInfo( getContext(), "TeachBusinessInfo", teachBusinessInfo );
                            //2.更新全局变量
                            ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                            //3.提交成功提示
                            makeText( SchoolEdit.this, "删除成功", LENGTH_SHORT ).show();
                            //4.返回上一个界面
                            finish();
                            //5.打印日志
                            Log.d( "SchoolEdit_Success", msg + "" );
                        } else {
                            makeText( SchoolEdit.this, "删除失败", LENGTH_SHORT ).show();
                            allSchool.add( schoolVO );//没有删除成功则新增对象
                            Log.d( "SchoolEdit_Fail", msg + "" );
                        }
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        makeText( SchoolEdit.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                        allSchool.add( schoolVO );//没有删除成功则新增对象
                        Log.d( "SchoolEdit_Fail", errorCode + ":" + msg );
                    }

                    //请求网络结束
                    @Override
                    public void onFinish() {
                        Log.d( "SchoolEdit_End", "请求结束" );
                    }
                } );


            }
        } );
    }


    /**
     * 初始化保存按钮监听
     */
    private void initSaveListener() {
        saveListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.参数组装
                schoolVO.setSchoolName( tv_school_name.getText().toString() );
                schoolVO.setBeginDate( tv_begin_date.getText().toString() );
                schoolVO.setEndDate( tv_end_date.getText().toString() );
                schoolVO.setEducation( tv_education.getText().toString() );
                schoolVO.setMajor( tv_major.getText().toString() );
                schoolVO.setIsOpen( "true" );
                if(StringUtils.isEmpty( schoolVO.getId() )){
                    schoolVO.setId( Tools.createID() );
                }
                final List<SchoolVO> tmpList = allSchool;
                //在allSchool中找到已经被修改过的schoolVO对象，并移除
                int size = tmpList.size();
                if (size > 0) {
                    String id = schoolVO.getId();
                    for (int i = 0; i < size; i++) {
                        SchoolVO tmpVO = tmpList.get( i );
                        if (!StringUtils.isBlank( id ) && id.equals( tmpVO.getId() )) {
                            tmpList.remove( i );
                            break;
                        }
                    }
                }
                tmpList.add( schoolVO );//更新
                final String allSchoolJson = JSONObject.toJSONString( tmpList );
                //2.获取请求参数
                RequestParams params = RequestParamsBuilder.buildRequestParams( SchoolEdit.this );
                params.addFormDataPart( "gradSchool", allSchoolJson );

                //3.发起请求
                HttpRequest.post( Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess( jsonObject );
                        String msg = jsonObject.getString( "msg" + "" );
                        if (jsonObject.getBoolean( "success" )) {
                            allSchool = tmpList;//保存成功赋值变量内容
                            TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                            teachBusinessInfo.setGradSchool( allSchoolJson );
                            //1.提交成功更新本地的shared数据
                            Tools.setShareInfo( getContext(), "TeachBusinessInfo", teachBusinessInfo );
                            ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                            //2.提交成功提示
                            makeText( SchoolEdit.this, "保存成功", LENGTH_SHORT ).show();
                            //3.打印日志
                            Log.d( "SchoolEdit_Success", msg + "" );
                        } else {
                            makeText( SchoolEdit.this, "提交失败", LENGTH_SHORT ).show();
                            Log.d( "SchoolEdit_Fail", msg + "" );
                        }
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        makeText( SchoolEdit.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                        Log.d( "SchoolEdit_Fail", errorCode + ":" + msg );
                    }

                    //请求网络结束
                    @Override
                    public void onFinish() {
                        Log.d( "SchoolEdit_End", "请求结束" );
                    }
                } );
            }
        };
    }

    /**
     * 初始化界面数据
     */
    private void initData() {
        tv_school_name.setText( schoolVO.getSchoolName() );
        tv_begin_date.setText( schoolVO.getBeginDate() );
        tv_end_date.setText( schoolVO.getEndDate() );
        tv_education.setText( schoolVO.getEducation() );
        tv_major.setText( schoolVO.getMajor() );
    }


    /**
     * 初始化Title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "院校编辑", R.color.titleWhite)
        .setSubTitle( "保存",R.color.titleWhite,saveListener );

    }

    /**
     * bottomDialog点击取消、确认的监听实现
     */
    private class ClickAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel:
                    Log.d( "dialogCancel", "=========这里执行取消操作=========" );
                    enumDialog.dismissDialog();
                    enumDialog = null;
                    break;
                case R.id.confirm:
                    //todo
                    if (whichOne == "begin") {
                        tv_begin_date.setText( enumDialog.getYear().getSelectionItem() + "-" + enumDialog.getMonth().getSelectionItem() );
                    } else if (whichOne == "end") {
                        tv_end_date.setText( enumDialog.getYear().getSelectionItem() + "-" + enumDialog.getMonth().getSelectionItem() );
                    } else if (whichOne == "education") {
                        tv_education.setText( enumDialog.getEducation().getSelectionItem() + "" );
                    }
                    enumDialog.dismissDialog();
                    enumDialog = null;
                    break;
            }
        }
    }
}
