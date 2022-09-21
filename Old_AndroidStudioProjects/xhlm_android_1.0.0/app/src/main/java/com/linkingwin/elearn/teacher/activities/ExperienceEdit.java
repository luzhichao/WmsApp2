package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.ExperienceVO;
import com.linkingwin.elearn.teacher.model.SchoolVO;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.linkingwin.elearn.teacher.widget.EnumDialog;

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

public class ExperienceEdit extends BaseActivity {
    @BindView(R.id.title_experience_edit)
    TitleView titleView;
    @BindView(R.id.et_describe)
    EditText et_describe;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.tv_begin_date)
    TextView tv_begin_date;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.bt_del_item)
    Button bt_del_item;
    //保存按钮监听
    private View.OnClickListener onClickListener;
    private EnumDialog enumDialog;
    private String whichOne ="";
    //经历传参对象
    private List<ExperienceVO> allExperience;
    private ExperienceVO experienceVO;
    //执行的操作
    private String action;//add新增操作，edit编辑操作

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_experience_edit );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        //1.初始化保存按钮监听
        initSaveListener();
        //2.初始化title
        initTitle();
        //3.获取参数并初始化
        initExtra();
    }

    /**
     * 结束日期点击
     */
    @OnClick(R.id.rl_end_date)
    public void openEndDate(View view){
        //选择的哪个日期控件
        whichOne ="end";
        //1.初始化bottomeDialog的相关视图
        initDialog(  );
        //2.设置dialog的title
        enumDialog.setTitle( "选择结束日期" );
        //3.点击选择开始日期控件，打开开始日期的控件，隐藏结束日期的控件
        enumDialog.setVisiable( R.id.ll_date, View.VISIBLE );
        //4.初始化显示的位置
        String date = tv_end_date.getText().toString();
        enumDialog.setDatePosition( date );
    }

    /**
     * 开始日期点击
     * @param v
     */
    @OnClick(R.id.rl_begin_date)
    public void openStartDate(View v) {
        //选择的哪个日期控件
        whichOne ="begin";
        //1.初始化bottomeDialog的相关视图
        initDialog(  );
        //2.设置dialog的title
        enumDialog.setTitle( "选择开始日期" );
        //3.点击选择开始日期控件，打开开始日期的控件，隐藏结束日期的控件
        enumDialog.setVisiable( R.id.ll_date, View.VISIBLE );
        //4.初始化显示的位置
        String date = tv_begin_date.getText().toString();
        enumDialog.setDatePosition( date );
    }

    /**
     * 初始化底部页面
     */
    private void initDialog() {
        //初始化
        enumDialog =new EnumDialog();
        enumDialog.initDialog(this);
        //监听
        enumDialog.setCancelOnclickListener(new ClickAction());
        enumDialog.setConfirmOnclickListener(new ClickAction());
    }

    /**
     * 获取传入参数，并初始化对象内容
     */
    private void initExtra() {
        //2.获取操作类型,edit编辑，add新增
        action = getIntent().getStringExtra( "action" );
        //3.判断生成vo和List对象
        String exp = getIntent().getStringExtra( "experienceVO" );
        String allExp = getIntent().getStringExtra( "allExperience" );
        if (exp == null || allExp == null) {
            String expJSONStr="";
            if(ElearnApplication.teachBusinessInfo!=null)
              expJSONStr = ElearnApplication.teachBusinessInfo.getExperience();
            experienceVO = new ExperienceVO();
            allExperience = JSONArray.parseArray( (expJSONStr == null || "".equals( expJSONStr )) ? "[]" : expJSONStr, ExperienceVO.class );
        } else {
            experienceVO = JSONObject.parseObject( exp, ExperienceVO.class );
            allExperience = JSONObject.parseArray( allExp, ExperienceVO.class );
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
     * 初始化保存监听
     */
    private void initSaveListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.组装参数
                experienceVO.setBeginDate( tv_begin_date.getText().toString() );
                experienceVO.setEndDate( tv_end_date.getText().toString() );
                experienceVO.setTitle( et_title.getText().toString() );
                experienceVO.setDescribe( et_describe.getText().toString() );
                if (StringUtils.isEmpty( experienceVO.getId())) {
                    experienceVO.setId( Tools.createID() );
                }
                final List<ExperienceVO> tmpList = allExperience;
                //在allSchool中找到已经被修改过的schoolVO对象，并移除
                int size = tmpList.size();
                if (size > 0) {
                    String id = experienceVO.getId() + "";
                    for (int i = 0; i < size; i++) {
                        ExperienceVO tmpVO = tmpList.get( i );
                        if (!StringUtils.isBlank( id ) && id.equals( tmpVO.getId() )) {
                            tmpList.remove( i );
                            break;
                        }
                    }
                }
                tmpList.add( experienceVO );//更新
                final String allExperienceJson = JSONObject.toJSONString( tmpList );
                //2.获取请求参数
                RequestParams params = RequestParamsBuilder.buildRequestParams( ExperienceEdit.this );
                params.addFormDataPart( "experience", allExperienceJson );

                //3.发起请求
                HttpRequest.post( Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess( jsonObject );
                        String msg = jsonObject.getString( "msg" + "" );
                        if (jsonObject.getBoolean( "success" )) {
                            allExperience = tmpList;//保存成功赋值变量内容
                            TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                            teachBusinessInfo.setExperience( allExperienceJson );
                            //1.提交成功更新本地的shared数据
                            Tools.setShareInfo( getContext(), "TeachBusinessInfo", ElearnApplication.teachBusinessInfo );
                            ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                            //2.提交成功提示
                            makeText( ExperienceEdit.this, "保存成功", LENGTH_SHORT ).show();
                            //3.打印日志
                            Log.d( "ExperienceEdit_Success", msg + "" );
                        } else {
                            makeText( ExperienceEdit.this, "提交失败", LENGTH_SHORT ).show();
                            Log.d( "ExperienceEdit_Fail", msg + "" );
                        }
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        makeText( ExperienceEdit.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                        Log.d( "ExperienceEdit_Fail", errorCode + ":" + msg );
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
     * 删除条目，并提交
     */
    private void initDelListener() {
        bt_del_item.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.剔除被删除的School
                int size = allExperience.size();
                final List<ExperienceVO> tmpList = allExperience;
                if(size>0){
                    String id = experienceVO.getId();
                    for (int i = 0; i < size; i++) {
                        ExperienceVO tmpVO = tmpList.get( i );
                        if (!StringUtils.isBlank( id )&&id.equals( tmpVO.getId() )) {
                            allExperience.remove( i );
                            break;
                        }
                        if(StringUtils.isBlank( id )){
                            allExperience.remove( experienceVO );
                            break;
                        }
                    }
                }
                //2.需要修改的内容
                final String allExperienceJson = JSONObject.toJSONString( allExperience );
                //3.获取请求参数
                RequestParams params = RequestParamsBuilder.buildRequestParams( ExperienceEdit.this );
                params.addFormDataPart( "experience", allExperienceJson );
                HttpRequest.post( Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess( jsonObject );
                        String msg = jsonObject.getString( "msg" + "" );
                        if (jsonObject.getBoolean( "success" )) {
                            TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                            teachBusinessInfo.setExperience( allExperienceJson );
                            //1.提交成功更新本地的shared数据
                            Tools.setShareInfo( getContext(), "TeachBusinessInfo", ElearnApplication.teachBusinessInfo );
                            //2.更新全局变量
                            ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                            //3.提交成功提示
                            makeText( ExperienceEdit.this, "删除成功", LENGTH_SHORT ).show();
                            //4.返回上一个界面
                            finish();
                            //5.打印日志
                            Log.d( "ExperienceEdit_Success", msg + "" );
                        } else {
                            makeText( ExperienceEdit.this, "删除失败", LENGTH_SHORT ).show();
                            allExperience.add( experienceVO );//没有删除成功则新增对象
                            Log.d( "ExperienceEdit_Fail", msg + "" );
                        }
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        makeText( ExperienceEdit.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                        allExperience.add( experienceVO );//没有删除成功则新增对象
                        Log.d( "ExperienceEdit_Fail", errorCode + ":" + msg );
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
     * 初始化数据
     */
    private void initData() {
        et_describe.setText( experienceVO.getDescribe() );
        tv_end_date.setText( experienceVO.getEndDate() );
        tv_begin_date.setText( experienceVO.getBeginDate() );
        et_title.setText( experienceVO.getTitle() );
    }

    /**
     * 初始化Title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "教学经历", R.color.titleWhite )
        .setSubTitle(  "保存",R.color.titleWhite,onClickListener);
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
                    enumDialog =null;
                    whichOne ="";
                    break;
                case R.id.confirm:
                    //todo
                    if (whichOne =="begin") {
                        tv_begin_date.setText( enumDialog.getYear().getSelectionItem() + "-" + enumDialog.getMonth().getSelectionItem() );
                    }
                    if(whichOne =="end"){
                        tv_end_date.setText( enumDialog.getYear().getSelectionItem() + "-" + enumDialog.getMonth().getSelectionItem() );
                    }
                    enumDialog.dismissDialog();
                    enumDialog =null;
                    whichOne ="";
                    break;
            }
        }
    }
}
