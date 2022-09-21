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
import com.linkingwin.elearn.teacher.adapter.StudentAdapter;
import com.linkingwin.elearn.teacher.model.OrderVO;
import com.linkingwin.elearn.teacher.model.StudentVO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MySudentActivity extends BaseActivity {

    @BindView(R.id.tv_student_title)
    TitleView titleView;
    //在授学生
    @BindView(R.id.rl_teaching_student)
    RelativeLayout rl_teaching_student;
    @BindView(R.id.tv_teaching_student)
    TextView tv_teaching_student;
    @BindView(R.id.tv_line_teaching_student)
    TextView tv_line_teaching_student;
    //完课学生
    @BindView(R.id.rl_finish_student)
    RelativeLayout rl_finish_student;
    @BindView(R.id.tv_finish_student)
    TextView tv_finish_student;
    @BindView(R.id.tv_line_finish_student)
    TextView tv_line_finish_student;

    @BindView(R.id.lv_my_student)
    ListView lv_my_student;
    @BindView(R.id.tv_tip)
    TextView tv_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sudent);

        ButterKnife.bind(this);
        setTranslucentStatusBar( true );
        titleView.setTitleText("我的学生", R.color.titleBlack);

        //初始化页签的监听
        initTabListener();
        //加载数据
        initListMyOrder("0");
    }

    /**
     * 初始化页签监听
     */
    private void initTabListener(){
        //点击在授学生
        rl_teaching_student.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.设置页签状态
                rl_teaching_student.setEnabled( false );
                tv_line_teaching_student.setVisibility( View.VISIBLE );

                rl_finish_student.setEnabled( true );
                tv_line_finish_student.setVisibility( View.GONE );

                //2.加载数据
                initListMyOrder("1");
            }
        } );
        //点击完课学生
        rl_finish_student.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.设置页签状态
                rl_teaching_student.setEnabled( true );
                tv_line_teaching_student.setVisibility( View.GONE );

                rl_finish_student.setEnabled( false );
                tv_line_finish_student.setVisibility( View.VISIBLE );

                //2.加载数据
                initListMyOrder("3");
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
        params.addFormDataPart("teachId", userID);
        params.addFormDataPart("status", tag);
        HttpRequest.post(Api.POST_TEACH_MY_STUDENT, params, new JsonHttpRequestCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                JSONArray result = jsonObject.getJSONArray("result");
                if(result != null && result.size() > 0){
                    List<StudentVO> datas = new ArrayList<>();
                    for(int i=0;i<result.size();i++){
                        StudentVO studentVO = new StudentVO();
                        studentVO.setAvatar(result.getObject(i, StudentVO.class).getAvatar());
                        studentVO.setNickName(result.getObject(i, StudentVO.class).getNickName());
                        studentVO.setCourseName(result.getObject(i, StudentVO.class).getCourseName());
                        studentVO.setBuyCourses(result.getObject(i, StudentVO.class).getBuyCourses());
                        studentVO.setStatus(result.getObject(i, StudentVO.class).getStatus());

                        datas.add(studentVO);
                    }
                    lv_my_student.setVisibility( View.VISIBLE );
                    tv_tip.setVisibility(View.GONE);
                    StudentAdapter studentAdapter = new StudentAdapter(MySudentActivity.this, datas, R.layout.list_item_order);
                    lv_my_student.setAdapter(studentAdapter);
                } else {
                    lv_my_student.setVisibility( View.GONE );
                    tv_tip.setVisibility(View.VISIBLE);
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                lv_my_student.setVisibility( View.GONE );
                tv_tip.setVisibility(View.VISIBLE);
                makeText(MySudentActivity.this, "网络原因，提交失败", LENGTH_SHORT).show();
                Log.d("initListMyOrder", errorCode + ":" + msg);
            }

            //请求网络结束
            @Override
            public void onFinish() {
                lv_my_student.setVisibility( View.GONE );
                tv_tip.setVisibility(View.VISIBLE);
                Log.d("initListMyOrder", "请求结束");
            }
        });

    }
}
