package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Peculiar extends BaseActivity {
    @BindView(R.id.title_peculiar)
    TitleView titleView;
    @BindView( R.id.et_exp_content )
    EditText et_exp_content;
    private View.OnClickListener saveListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_peculiar );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        //初始化监听
        initSaveListener();
        //初始化标题、副标题和增加副标题的监听
        initTitle();
        initData();
    }

    private void initSaveListener() {
        saveListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePeculiar(v);
            }
        };
    }

    /***
     * 初始化数据
     */
    private void initData(){
        et_exp_content.setText( ElearnApplication.teachBusinessInfo.getPeculiar() );
    }

    /**
     * 保存按钮
     * @param view
     */
    public void savePeculiar(View view){
        //组装参数
        final String peculiar=et_exp_content.getText().toString();
        //1.获取请求参数
        RequestParams params = RequestParamsBuilder.buildRequestParams( this );
        params.addFormDataPart( "peculiar", peculiar );

        //3.发起请求
        HttpRequest.post( Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String msg = jsonObject.getString( "msg" + "" );
                if (jsonObject.getBoolean( "success" )) {
                    TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                    teachBusinessInfo.setPeculiar( peculiar );
                    //1.提交成功更新本地的shared数据
                    Tools.setShareInfo( getContext(), "TeachBusinessInfo", teachBusinessInfo );
                    ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                    //2.提交成功提示
                    makeText( getContext(), "保存成功", LENGTH_SHORT ).show();
                    //3.打印日志
                    Log.d( "Peculiar_Success", msg + "" );
                } else {
                    makeText( getContext(), "提交失败", LENGTH_SHORT ).show();
                    Log.d( "Peculiar_Fail", msg + "" );
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( getContext(), "网络原因，提交失败", LENGTH_SHORT ).show();
                Log.d( "Peculiar_Fail", errorCode + ":" + msg );
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "Peculiar_End", "请求结束" );
            }
        } );
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "教学特点", R.color.titleWhite)
        .setSubTitle( "保存",R.color.titleWhite,saveListener );
    }
}
