package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class PlatformRules extends BaseActivity {
    @BindView(R.id.title_plat_rule)
    TitleView titleView;
    @BindView(R.id.tv_plat_rule)
    TextView tv_plat_rule;
    @BindView( R.id.bt_confirm )
    Button bt_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_platform_rules );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        //1.初始化titile的样式
        initTitle();
        initPlatformRules();
    }

    @OnClick(R.id.bt_confirm)
    public void confirmCriteria(View view){
        RequestParams params=RequestParamsBuilder.buildRequestParams(this);
        params.addFormDataPart( "platformRuls","1" );
        HttpRequest.post(Api.POST_SAVE_TEACH_INFO,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                //todo
                JSONObject result=jsonObject.getJSONObject( "result" );
                ElearnApplication.teachBusinessInfo.setPlatformRuls( "1" );
                Tools.setShareInfo( getContext(), "TeachBusinessInfo", ElearnApplication.teachBusinessInfo );
                makeText( PlatformRules.this,"您已经同意学习平台准则",LENGTH_SHORT ).show();
            }
            @Override
            public void onFailure(int errorCode, String msg) {
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void initPlatformRules(){
        if(ElearnApplication.teachBusinessInfo!=null) {
            String tmp=ElearnApplication.teachBusinessInfo.getPlatformRuls();
            if("0".equals( tmp ) || tmp==null){
                bt_confirm.setEnabled( true );
            }else{
                bt_confirm.setEnabled( false );
                bt_confirm.setText( "已确认基本服务准则" );
            }
        }
        RequestParams builder=RequestParamsBuilder.buildRequestParams(this);
        HttpRequest.get(Api.GET_PLATFORM_RULES_API,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                ////todo
                JSONObject result=jsonObject.getJSONObject( "result" );
                if(result!=null){
                    tv_plat_rule.setText(Html.fromHtml(result.getString( "configValue" )));//获取返回的内容，并格式化放到TextView中
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
     * 初始化Title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "学习平台规则", R.color.titleWhite);
    }
}
