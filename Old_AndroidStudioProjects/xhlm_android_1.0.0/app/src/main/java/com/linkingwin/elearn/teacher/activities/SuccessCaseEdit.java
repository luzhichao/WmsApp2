package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.ExperienceVO;
import com.linkingwin.elearn.teacher.model.SchoolVO;
import com.linkingwin.elearn.teacher.model.SuccessCaseVO;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class SuccessCaseEdit extends BaseActivity {
    @BindView(R.id.title_success)
    TitleView titleView;
    @BindView(R.id.et_title)
    TextView et_title;
    @BindView(R.id.et_describe)
    EditText et_describe;
    @BindView(R.id.bt_del_item)
    Button bt_del_item;

    private String action;
    //传参
    private SuccessCaseVO successCaseVO;
    private List<SuccessCaseVO> allSuccess;
    private View.OnClickListener saveListener; //保存按钮监听

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_success_case_edit );
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
     * 获取传入参数，并初始化对象内容
     */
    private void initExtra() {
        //2.获取操作类型,edit编辑，add新增
        action = getIntent().getStringExtra( "action" );
        //3.判断生成vo和List对象
        String exp = getIntent().getStringExtra( "successCaseVO" );
        String allExp = getIntent().getStringExtra( "allSuccess" );
        if (exp == null || allExp == null) {
            String expJSONStr = "";
            if (ElearnApplication.teachBusinessInfo != null)
                expJSONStr = ElearnApplication.teachBusinessInfo.getExperience();
            successCaseVO = new SuccessCaseVO();
            allSuccess = JSONArray.parseArray( (expJSONStr == null || "".equals( expJSONStr )) ? "[]" : expJSONStr, SuccessCaseVO.class );
        } else {
            successCaseVO = JSONObject.parseObject( exp, SuccessCaseVO.class );
            allSuccess = JSONObject.parseArray( allExp, SuccessCaseVO.class );
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
        saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.组装参数
                successCaseVO.setTitle( et_title.getText().toString() );
                successCaseVO.setDescribe( et_describe.getText().toString() );
                if (StringUtils.isEmpty( successCaseVO.getId() )) {
                    successCaseVO.setId( Tools.createID() );
                }
                final List<SuccessCaseVO> tmpList = allSuccess;
                //在allSchool中找到已经被修改过的schoolVO对象，并移除
                int size = tmpList.size();
                if (size > 0) {
                    String id = successCaseVO.getId() + "";
                    for (int i = 0; i < size; i++) {
                        SuccessCaseVO tmpVO = tmpList.get( i );
                        if (!StringUtils.isBlank( id ) && id.equals( tmpVO.getId() )) {
                            tmpList.remove( i );
                            break;
                        }
                    }
                }
                tmpList.add( successCaseVO );//更新
                final String allSuccessJson = JSONObject.toJSONString( tmpList );
                //2.获取请求参数
                RequestParams params = RequestParamsBuilder.buildRequestParams( SuccessCaseEdit.this );
                params.addFormDataPart( "successCase", allSuccessJson );

                //3.发起请求
                HttpRequest.post( Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess( jsonObject );
                        String msg = jsonObject.getString( "msg" + "" );
                        if (jsonObject.getBoolean( "success" )) {
                            allSuccess = tmpList;//保存成功赋值变量内容
                            TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                            teachBusinessInfo.setSuccessCase( allSuccessJson );
                            //1.提交成功更新本地的shared数据
                            Tools.setShareInfo( getContext(), "TeachBusinessInfo", ElearnApplication.teachBusinessInfo );
                            ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                            //2.提交成功提示
                            makeText( SuccessCaseEdit.this, "保存成功", LENGTH_SHORT ).show();
                            //3.打印日志
                            Log.d( "SuccessCaseEdit_Success", msg + "" );
                        } else {
                            makeText( SuccessCaseEdit.this, "提交失败", LENGTH_SHORT ).show();
                            Log.d( "SuccessCaseEdit_Fail", msg + "" );
                        }
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        makeText( SuccessCaseEdit.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                        Log.d( "SuccessCaseEditt_Fail", errorCode + ":" + msg );
                    }

                    //请求网络结束
                    @Override
                    public void onFinish() {
                        Log.d( "SuccessCaseEdit_End", "请求结束" );
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
                //1.剔除被删除的successCase
                int size = allSuccess.size();
                final List<SuccessCaseVO> tmpList = allSuccess;
                if (size > 0) {
                    String id = successCaseVO.getId();
                    for (int i = 0; i < size; i++) {
                        SuccessCaseVO tmpVO = tmpList.get( i );
                        if (!StringUtils.isBlank( id ) && id.equals( tmpVO.getId() )) {
                            tmpList.remove( i );
                            break;
                        }
                        if (StringUtils.isBlank( id )) {
                            tmpList.remove( successCaseVO );
                            break;
                        }
                    }
                }
                //2.需要修改的内容
                final String allSuccessJson = JSONObject.toJSONString( tmpList );
                //3.获取请求参数
                RequestParams params = RequestParamsBuilder.buildRequestParams( SuccessCaseEdit.this );
                params.addFormDataPart( "successCase", allSuccessJson );
                HttpRequest.post( Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess( jsonObject );
                        String msg = jsonObject.getString( "msg" + "" );
                        if (jsonObject.getBoolean( "success" )) {
                            TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                            teachBusinessInfo.setSuccessCase( allSuccessJson );
                            //1.提交成功更新本地的shared数据
                            Tools.setShareInfo( getContext(), "TeachBusinessInfo", ElearnApplication.teachBusinessInfo );
                            //2.更新全局变量和局部变量
                            ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                            //3.提交成功提示
                            makeText( SuccessCaseEdit.this, "删除成功", LENGTH_SHORT ).show();
                            //4.返回上一个界面
                            finish();
                            //5.打印日志
                            Log.d( "SuccessCaseEdit_Success", msg + "" );
                        } else {
                            makeText( SuccessCaseEdit.this, "删除失败", LENGTH_SHORT ).show();
                            allSuccess.add( successCaseVO );//没有删除成功则新增对象
                            Log.d( "SuccessCaseEdit_Fail", msg + "" );
                        }
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        makeText( SuccessCaseEdit.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                        allSuccess.add( successCaseVO );//没有删除成功则新增对象
                        Log.d( "SuccessCaseEdit_Fail", errorCode + ":" + msg );
                    }

                    //请求网络结束
                    @Override
                    public void onFinish() {
                        Log.d( "SuccessCaseEdit_End", "请求结束" );
                    }
                } );
            }
        } );
    }

    /**
     * 初始化数据
     */
    private void initData() {
        et_describe.setText( successCaseVO.getDescribe() );
        et_title.setText( successCaseVO.getTitle() );
    }

    /**
     * 初始化Title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "教学经历", R.color.titleWhite )
                .setSubTitle( "保存", R.color.titleWhite, saveListener );
    }
}
