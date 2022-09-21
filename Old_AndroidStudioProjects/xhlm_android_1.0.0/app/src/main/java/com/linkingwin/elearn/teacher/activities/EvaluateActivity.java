package com.linkingwin.elearn.teacher.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.adapter.EvaluateAdapter;
import com.linkingwin.elearn.teacher.model.EvaluateVO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

public class EvaluateActivity extends BaseActivity {

    @BindView(R.id.title_evaluate)
    TitleView title_evaluate;
    @BindView(R.id.lv_evaluate)
    ListView lv_evaluate;

    private List<EvaluateVO> evaluateVOS = new ArrayList<>();//所有评价

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        //inti title
        title_evaluate.setTitleText("评价中心", R.color.titleWhite);
        //TODO init data
        initEvaluate();
    }

    /**
     * 初始化数据
     */
    private void initEvaluate() {
        String teachId = ElearnApplication.teachBusinessInfo.getUserId();
        //获取Request
        RequestParams params = RequestParamsBuilder.buildRequestParams( this );
        //发起请求
        HttpRequest.get( Api.GET_EVALUATE_BY_TEACHID + teachId, params, new JsonHttpRequestCallback() {
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String message = jsonObject.getString( "message" );
                if (jsonObject.getBoolean( "success" )) {
                    JSONArray result = jsonObject.getJSONArray("result");
                    if (result != null ){
                        for(int i=0;i<result.size();i++){
                            EvaluateVO evaluateVO = JSONObject.parseObject(result.getString(i), EvaluateVO.class);
                            evaluateVOS.add(evaluateVO);
                        }
                    }
                    //处理数据
                    EvaluateAdapter evaluateAdapter = new EvaluateAdapter(EvaluateActivity.this, evaluateVOS, R.layout.list_evaluate_item);
                    lv_evaluate.setAdapter(evaluateAdapter);
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText( getContext(), "网络异常", Toast.LENGTH_SHORT ).show();
                Log.d( "loaderRes_failure", errorCode + ":" + msg );
            }
            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "loaderRes_finish", "请求结束" );
            }
        } );
    }
}
