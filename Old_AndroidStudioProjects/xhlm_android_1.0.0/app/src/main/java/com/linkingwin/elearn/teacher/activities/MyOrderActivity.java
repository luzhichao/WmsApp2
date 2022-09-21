package com.linkingwin.elearn.teacher.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.adapter.OrderAdapter;
import com.linkingwin.elearn.teacher.model.OrderVO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MyOrderActivity extends BaseActivity {

    @BindView(R.id.tv_order_title)
    TitleView titleView;

    @BindView(R.id.rl_unpaid_order)
    RelativeLayout rl_unpaid_order;
    @BindView(R.id.tv_unpaid_order)
    TextView tv_unpaid_order;
    @BindView(R.id.tv_line_unpaid_order)
    TextView tv_line_unpaid_order;

    @BindView(R.id.rl_paid_order)
    RelativeLayout rl_paid_order;
    @BindView(R.id.tv_paid_order)
    TextView tv_paid_order;
    @BindView(R.id.tv_line_paid_order)
    TextView tv_line_paid_order;

    @BindView(R.id.rl_all_order)
    RelativeLayout rl_all_order;
    @BindView(R.id.tv_all_order)
    TextView tv_all_order;
    @BindView(R.id.tv_line_all_order)
    TextView tv_line_all_order;

    @BindView(R.id.lv_my_order)
    ListView lv_my_order;
    @BindView(R.id.tv_tip)
    TextView tv_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        ButterKnife.bind(this);
        setTranslucentStatusBar( true );
        titleView.setTitleText("我的订单", R.color.titleBlack);

        //初始化页签的监听
        initTabListener();

    }

    /**
     * 刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        //init data
        initListMyOrder("0");
    }

    /**
     * 初始化页签监听
     */
    private void initTabListener(){
        //点击未付款
        rl_unpaid_order.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.设置页签状态
                rl_unpaid_order.setEnabled( false );
                tv_line_unpaid_order.setVisibility( View.VISIBLE );

                rl_paid_order.setEnabled( true );
                tv_line_paid_order.setVisibility( View.GONE );

                rl_all_order.setEnabled( true );
                tv_line_all_order.setVisibility( View.GONE );

                //2.加载数据
                initListMyOrder("0");
            }
        } );
        //点击已付款
        rl_paid_order.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.设置页签状态
                rl_unpaid_order.setEnabled( true );
                tv_line_unpaid_order.setVisibility( View.GONE );

                rl_paid_order.setEnabled( false );
                tv_line_paid_order.setVisibility( View.VISIBLE );

                rl_all_order.setEnabled( true );
                tv_line_all_order.setVisibility( View.GONE );

                //2.加载数据
                initListMyOrder("1");
            }
        } );
        //点击全部
        rl_all_order.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.设置页签状态
                rl_unpaid_order.setEnabled( true );
                tv_line_unpaid_order.setVisibility( View.GONE );

                rl_paid_order.setEnabled( true );
                tv_line_paid_order.setVisibility( View.GONE );

                rl_all_order.setEnabled( false );
                tv_line_all_order.setVisibility( View.VISIBLE );

                //2.加载数据
                initListMyOrder("all");
            }
        } );
    }

    /**
     * 获取数据，组装列表
     * @param tag
     */
    private void initListMyOrder(String tag){
        //当前老师ID
        String userID = ElearnApplication.teachBusinessInfo.getUserId();
        RequestParams params = RequestParamsBuilder.buildRequestParams( this );
        params.addFormDataPart("publisher", userID);
        params.addFormDataPart("status", tag);
        HttpRequest.post(Api.POST_TEACH_MY_ORDER, params, new JsonHttpRequestCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                JSONArray result = jsonObject.getJSONArray("result");
                if(result != null && result.size() > 0){
                    List<OrderVO> datas = new ArrayList<>();
                    for(int i=0;i<result.size();i++){
                        OrderVO orderVO = new OrderVO();
                        orderVO.setId(result.getObject(i, OrderVO.class).getId());
                        orderVO.setCourseId(result.getObject(i, OrderVO.class).getCourseId());
                        orderVO.setCourseName(result.getObject(i, OrderVO.class).getCourseName());
                        orderVO.setCurrentPrice(result.getObject(i, OrderVO.class).getCurrentPrice());
                        orderVO.setBuyCourses(result.getObject(i, OrderVO.class).getBuyCourses());
                        orderVO.setStatus(result.getObject(i, OrderVO.class).getStatus());

                        datas.add(orderVO);
                    }
                    lv_my_order.setVisibility( View.VISIBLE );
                    tv_tip.setVisibility(View.GONE);
                    OrderAdapter orderAdapter = new OrderAdapter(MyOrderActivity.this, datas, R.layout.list_item_order);
                    lv_my_order.setAdapter(orderAdapter);
                } else {
                    lv_my_order.setVisibility( View.GONE );
                    tv_tip.setVisibility(View.VISIBLE);
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                lv_my_order.setVisibility( View.GONE );
                tv_tip.setVisibility(View.VISIBLE);
                makeText(MyOrderActivity.this, "网络原因，提交失败", LENGTH_SHORT).show();
                Log.d("initListMyOrder", errorCode + ":" + msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                lv_my_order.setVisibility( View.GONE );
                tv_tip.setVisibility(View.VISIBLE);
                Log.d("initListMyOrder", "请求结束");
            }
        });

        //TODO 测试数据
//        List<OrderVO> datas = new ArrayList<>();
//        for (int i=0; i<10; i++){
//            OrderVO orderVO = new OrderVO();
//            orderVO.setId(i + "");
//            orderVO.setCourseId(i + "");
//            orderVO.setCourseName("testName " + i);
//            orderVO.setCurrentPrice(Integer.toString((199 + i)));
//            orderVO.setBuyCourses(20 + i);
//            orderVO.setStatus(Integer.toString(i % 3));
//
//            datas.add(orderVO);
//        }
//        lv_my_order.setVisibility( View.VISIBLE );
//        OrderAdapter orderAdapter = new OrderAdapter(this, datas, R.layout.list_item_order);
//        lv_my_order.setAdapter(orderAdapter);
    }
}
