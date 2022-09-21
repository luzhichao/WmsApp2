package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.okhttpfinal.StringHttpRequestCallback;
import qiu.niorgai.StatusBarCompat;

public class RegisterProtocol extends BaseActivity {
    @BindView(R.id.tv_protocol_content)
    TextView protocol_content;
    @BindView(R.id.title_protocol_content)
    TitleView titleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigter_protocol);
        ButterKnife.bind(this);  //必须要加，注解才会生效否则报Null错误
        //加载沉浸式bar,全屏显示
        setTranslucentStatusBar( true );
        initTitle();
        String protocolStr="";
        protocol_content.setText(Html.fromHtml(protocolStr));
        initPropotol();
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( getString(R.string.title_protocol), R.color.titleWhite );
    }

    private void initPropotol(){
        RequestParams builder=RequestParamsBuilder.buildRequestParams(this);
        HttpRequest.get(Api.PROTOCOL_API,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                ////todo
                JSONObject result=jsonObject.getJSONObject( "result" );
                if(result!=null){

                    protocol_content.setText(Html.fromHtml(result.getString( "configValue" )));//获取返回的内容，并格式化放到TextView中
                }else{
                    Toast.makeText( getContext(),"未配置协议内容",Toast.LENGTH_SHORT );
                }
            }
            @Override
            public void onFailure(int errorCode, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
