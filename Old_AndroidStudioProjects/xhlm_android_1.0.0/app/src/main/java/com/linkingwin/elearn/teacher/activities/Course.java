package com.linkingwin.elearn.teacher.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.adapter.CourseAdapter;
import com.linkingwin.elearn.teacher.adapter.SchoolAdapter;
import com.linkingwin.elearn.teacher.model.CourseVO;
import com.linkingwin.elearn.teacher.model.SchoolVO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

public class Course extends BaseActivity {
    @BindView(R.id.title_course)
    TitleView titleView;
    @BindView(R.id.lv_course)
    ListView lv_course;
    @BindView(R.id.tv_tip)
    TextView tv_tip;

    //三个页签，依次是小学、中学、高中
    @BindView( R.id.rl_primary )
    RelativeLayout rl_primary;
    @BindView( R.id.tv_primary )
    TextView tv_primary;
    @BindView( R.id.tv_line_primary )
    TextView tv_line_primary;

    @BindView( R.id.rl_middle )
    RelativeLayout rl_middle;
    @BindView( R.id.tv_middle )
    TextView tv_middle;
    @BindView( R.id.tv_line_middle )
    TextView tv_line_middle;

    @BindView( R.id.rl_high )
    RelativeLayout rl_high;
    @BindView( R.id.tv_high )
    TextView tv_high;
    @BindView( R.id.tv_line_high )
    TextView tv_line_high;

    private List<CourseVO> mDatas;
    private CourseAdapter myAdapter;
    //默认的学部过滤条件，默认的打开方式
    private String selectedCourseGrade="小学";
    private String action="open";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.list_course );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        initTitle();
        //初始化页签的监听
        initTabListener();
    }

    /**
     * 小学、中学、高中三个页签的监听
     */
    private void initTabListener(){
        rl_primary.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.设置页签状态
                rl_primary.setEnabled( false );
                tv_line_primary.setVisibility( View.VISIBLE );

                rl_middle.setEnabled( true );
                tv_line_middle.setVisibility( View.GONE );

                rl_high.setEnabled( true );
                tv_line_high.setVisibility( View.GONE );
                //选中的页签
                selectedCourseGrade=tv_primary.getText().toString();
                //2.加载数据
                initListCourses(selectedCourseGrade);
            }
        } );

        rl_middle.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.设置页签状态
                rl_primary.setEnabled( true );
                tv_line_primary.setVisibility( View.GONE );

                rl_middle.setEnabled( false );
                tv_line_middle.setVisibility( View.VISIBLE );

                rl_high.setEnabled( true );
                tv_line_high.setVisibility( View.GONE );
                //选中的页签
                selectedCourseGrade=tv_middle.getText().toString();
                //2.加载数据
                initListCourses(selectedCourseGrade);
            }
        } );

        rl_high.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //1.设置页签状态
                rl_primary.setEnabled( true );
                tv_line_primary.setVisibility( View.GONE );

                rl_middle.setEnabled( true );
                tv_line_middle.setVisibility( View.GONE );

                rl_high.setEnabled( false );
                tv_line_high.setVisibility( View.VISIBLE );
                //选中的页签
                selectedCourseGrade=tv_high.getText().toString();
                //2.加载数据
                initListCourses(selectedCourseGrade);
            }
        } );
    }

    /**
     * 实现返回刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        initListCourses(selectedCourseGrade);
    }

    /**
     * 获取数据，组装list<CourseVO>
     */
    private void initListCourses(String courseGrade){
        //courseGrade,小学\初中\高中
        String userID=ElearnApplication.teachBusinessInfo.getUserId();
//        String username=ElearnApplication.getJsonLoinInfo().getString("username");
        String URL=Api.GET_COURSES_FIND_API+userID;
        RequestParams params=RequestParamsBuilder.buildRequestParams( this );
        //发起请求
        HttpRequest.get( URL, params, new JsonHttpRequestCallback() {
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String message = jsonObject.getString( "msg" );
                String result=jsonObject.getString("result");
                if(!StringUtils.isEmpty(result) && jsonObject.getBoolean("success") ){
                    initListView(result,courseGrade);
                }else{
                    Toast.makeText( getContext(), "获取数据失败", Toast.LENGTH_SHORT ).show();
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                Toast.makeText( getContext(), "网络异常，登陆失败", Toast.LENGTH_SHORT ).show();
                Log.d( "Course_GetCourses", errorCode + ":" + msg );
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "Course_GetCourses", "请求结束" );
            }
        } );
    }

    /**
     * 方法:初始化Adapter
     */
    private void initListView(String courseJsonArray,String courseGrade) {
        if(StringUtils.isEmpty( courseJsonArray )){
            //mDatas =Tools.getSharedInfoList( "courses",Tools.getSharedPre( this ),CourseVO.class );
            mDatas=new ArrayList<>(  );
            CourseVO courseVO;
            //todo,测试代码要去掉
            courseVO = new CourseVO("1", "新概念英语", "高中", "高一",
                    "一对一", "1", "英语", "108", "22", "20", "1" );
            mDatas.add( courseVO );
            courseVO= new CourseVO("2", "奥数", "小学", "1年级",
                    "一对多小班课", "5", "数学", "108", "20", "18", "2" );
            mDatas.add( courseVO );
             courseVO = new CourseVO("3", "语文", "初中", "初一",
                    "一对多小班课", "5", "数学", "108", "20", "18", "2" );
            mDatas.add( courseVO );
        }else{
            try{
                mDatas=JSONArray.parseArray( courseJsonArray,CourseVO.class );
            }catch (Exception e){
                Toast.makeText( getContext(), e.toString(), Toast.LENGTH_SHORT ).show();
            }
        }

        //获取全部的课程列表，需要把未选中页签的学部过滤掉，并且以删除标记的过滤掉
        List<CourseVO> tmp=new ArrayList();
        for( int i = 0 ; mDatas!=null && i < mDatas.size() ; i++) {
            CourseVO tmpVO=mDatas.get( i );
            if(courseGrade.equals( tmpVO.getCourseGrade() ) && courseGrade!=null && "0".equals(tmpVO.getDelFlag())){
                tmp.add( tmpVO );
            }
        }
        mDatas=tmp;

        if (mDatas != null) {
            myAdapter=null;
            myAdapter = new CourseAdapter( this, mDatas, R.layout.list_item_course );
            lv_course.setVisibility( View.VISIBLE );
            tv_tip.setVisibility( View.GONE );
            lv_course.setAdapter( myAdapter );
        } else {
            tv_tip.setVisibility( View.VISIBLE );
            lv_course.setVisibility( View.GONE );
        }
    }

    /**
     * 初始化title，添加副标题和监听
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "课程管理", R.color.titleWhite )
                .setSubTitle( "新增", R.color.titleWhite, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tools.toOtherPage( getContext(), CourseEdit.class, false, null );
                    }
                } );
    }
}
