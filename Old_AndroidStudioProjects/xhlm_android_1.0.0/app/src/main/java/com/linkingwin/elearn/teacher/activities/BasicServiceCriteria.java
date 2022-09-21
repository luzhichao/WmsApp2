package com.linkingwin.elearn.teacher.activities;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class BasicServiceCriteria extends BaseActivity {
    @BindView(R.id.title_basic)
    TitleView titleView;
    @BindView(R.id.tv_basic_content)
    TextView tv_basic_content;
    @BindView( R.id.bt_confirm )
    Button bt_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_basic_service_criteria );
        setTranslucentStatusBar( true );
        ButterKnife.bind(this);  //必须要加，注解才会生效否则报Null错误
        initTitle();
        initServiceCriteria();
    }

    @OnClick(R.id.bt_confirm)
    public void confirmCriteria(View view){
        RequestParams params=RequestParamsBuilder.buildRequestParams(this);
        params.addFormDataPart( "basicService","1" );
        HttpRequest.post(Api.POST_SAVE_TEACH_INFO,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                JSONObject result=jsonObject.getJSONObject( "result" );
                ElearnApplication.teachBusinessInfo.setBasicService( "1" );
                Tools.setShareInfo( getContext(), "TeachBusinessInfo", ElearnApplication.teachBusinessInfo );
                makeText( BasicServiceCriteria.this,"您已经同意基准服务准则",LENGTH_SHORT ).show();
            }
            @Override
            public void onFailure(int errorCode, String msg) {
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void initServiceCriteria(){
        if(ElearnApplication.teachBusinessInfo!=null) {
            String tmp=ElearnApplication.teachBusinessInfo.getBasicService();
            if("0".equals( tmp ) || tmp==null){
                bt_confirm.setEnabled( true );
            }else{
                bt_confirm.setEnabled( false );
                bt_confirm.setText( "已确认基本服务准则" );
            }
        }
        RequestParams builder=RequestParamsBuilder.buildRequestParams(this);
        HttpRequest.get(Api.GET_BASIC_SERVICE_API,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                ////todo
                JSONObject result=jsonObject.getJSONObject( "result" );
                if(result!=null){
                    tv_basic_content.setText(Html.fromHtml(result.getString( "configValue" )));//获取返回的内容，并格式化放到TextView中
                }else{
                    makeText( getContext(),"未配置协议内容",LENGTH_SHORT );
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

    /**
     * 初始化title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "基本服务协议", R.color.titleWhite);
    }
}
