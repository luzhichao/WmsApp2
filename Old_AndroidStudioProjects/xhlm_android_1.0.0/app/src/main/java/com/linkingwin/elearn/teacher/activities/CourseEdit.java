package com.linkingwin.elearn.teacher.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.linkingwin.elearn.teacher.model.CourseVO;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

public class CourseEdit extends BaseActivity {
    @BindView(R.id.title_course_edit)
    TitleView titleView; //title
    @BindView(R.id.tv_course_name)
    EditText tv_course_name; //课程名称
    @BindView(R.id.sp_grade)
    Spinner sp_grade;  //学部
    @BindView(R.id.sp_class)
    Spinner sp_class;  //年级
    @BindView(R.id.sp_type)
    Spinner sp_type;   //类型
    @BindView(R.id.sp_persons)
    Spinner sp_persons; //人数
    @BindView(R.id.sp_subjects)
    Spinner sp_subjects; //科目
    @BindView(R.id.tv_min_subject)
    EditText tv_min_subject; //最小课节
    @BindView(R.id.tv_max_subject)
    EditText tv_max_subject; //最大课节
    @BindView(R.id.tv_prices)
    EditText tv_prices;//价格
    @BindView(R.id.et_duration)
    EditText et_duration; //每节课时长
    @BindView(R.id.bt_del_item)
    Button bt_del_item; //删除按钮


    View.OnClickListener saveCourseListener;


    //定义下拉列表数组内容
    String[] gradeList;
    String[] classList;
    String[] courseTypeList;
    String[] personList;
    String[] subjectList;

    final static int  minPerson=1,maxPerson=12;

    String action = "";
    private CourseVO courseVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_course_edit );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        initSaveCousreListener();
        initTitle();
        initExtra();
    }

    public void initSaveCousreListener(){
        saveCourseListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID=ElearnApplication.teachBusinessInfo.getUserId();
                //1.组装参数
                if (StringUtils.isEmpty(courseVO.getId())){
                    courseVO.setId(Tools.createID());
                    courseVO.setPublisher(userID);
                }
                courseVO.setCourseName(tv_course_name.getText().toString());
                courseVO.setCourseGrade(sp_grade.getSelectedItem().toString());
                courseVO.setCourseClass(sp_class.getSelectedItem().toString());
                courseVO.setType(sp_type.getSelectedItem().toString());
                courseVO.setPeopleNum(sp_persons.getSelectedItem().toString());
                courseVO.setSubject(sp_subjects.getSelectedItem().toString());
                courseVO.setSourcePrice(tv_prices.getText().toString());
                courseVO.setLessons(tv_max_subject.getText().toString());
                courseVO.setMinBuy(tv_min_subject.getText().toString());
                courseVO.setDuration(et_duration.getText().toString());

                //2.提交保存请求
                RequestParams params = RequestParamsBuilder.buildRequestParams( CourseEdit.this );
//                params.setRequestBody( "application/json; charset=utf-8",JSONObject.toJSONString(courseVO) );
                params.addFormDataPart("id", courseVO.getId());
                params.addFormDataPart("publisher", courseVO.getPublisher());
                params.addFormDataPart("courseName", courseVO.getCourseName());
                params.addFormDataPart("courseGrade", courseVO.getCourseGrade());
                params.addFormDataPart("courseClass", courseVO.getCourseClass());
                params.addFormDataPart("type", courseVO.getType());
                params.addFormDataPart("peopleNum", courseVO.getPeopleNum());
                params.addFormDataPart("subject", courseVO.getSubject());
                params.addFormDataPart("sourcePrice", courseVO.getSourcePrice());
                params.addFormDataPart("lessons", courseVO.getLessons());
                params.addFormDataPart("minBuy", courseVO.getMinBuy());
                params.addFormDataPart("duration", courseVO.getDuration());
                //发起请求
                HttpRequest.post(Api.POST_COURSES_SAVE_API, params, new JsonHttpRequestCallback() {

                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess( jsonObject );
                        String message = "保存成功";
                        Toast.makeText( getContext(), message, Toast.LENGTH_SHORT ).show();
                        //保存成功此条目可以被删除
                        bt_del_item.setVisibility(View.VISIBLE);
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        Toast.makeText( getContext(), "网络异常，登陆失败", Toast.LENGTH_SHORT ).show();
                        Log.d( "CourseEdit", errorCode + ":" + msg );
                    }

                    //请求网络结束
                    @Override
                    public void onFinish() {
                        Log.d( "CourseEdit", "请求结束" );
                    }
                } );

            }
        };
    }

    /**
     * 初始化学部选择下拉列表
     */
    public void openGradePicker() {
        gradeList=new String[]{"小学","初中","高中"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>( this,R.layout.custom_spinner,gradeList );
        arrayAdapter.setDropDownViewResource( R.layout.custom_spinner_dialog_picker );
        sp_grade.setAdapter( arrayAdapter );
        sp_grade.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                openClassPicker(parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

    }

    /**
     * 打开年级dialog窗口
     */
    public void openClassPicker(@Nullable String item) {
        if (item == null) item=sp_grade.getSelectedItem().toString();
        switch(item){
            case "小学":
                classList= new String[]{"一年级", "二年级", "三年级", "四年级", "五年级", "六年级"};
                break;
            case "初中":
                classList= new String[]{"初一", "初二", "初三"};
                break;
            case "高中":
                classList= new String[]{"高一", "高二", "高三"};
                break;
        }
        if (classList!=null){
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>( this,R.layout.custom_spinner,classList );
            arrayAdapter.setDropDownViewResource( R.layout.custom_spinner_dialog_picker );
            sp_class.setAdapter( arrayAdapter );
        }
    }

    /**
     * 打开开课类型dialog窗口
     */
    public void openCourseTypePicker() {
        courseTypeList=new String[]{"一对一","一对多小班课"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>( this,R.layout.custom_spinner,courseTypeList );
        arrayAdapter.setDropDownViewResource( R.layout.custom_spinner_dialog_picker );
        sp_type.setAdapter( arrayAdapter );
        sp_type.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem().toString()=="一对一"){
                    openPersonsPicker(minPerson);
                }else{
                    openPersonsPicker(maxPerson);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
    }

    /**
     * 打开人数选择dialog窗口
     */
    public void openPersonsPicker(int size) {
        personList=new String[size];
        for(int i=1 ;i<size+1;i++){
            personList[i-1]=i+"";
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>( this,R.layout.custom_spinner,personList );
        arrayAdapter.setDropDownViewResource( R.layout.custom_spinner_dialog_picker );
        sp_persons.setAdapter( arrayAdapter );
    }

    /**
     * 打开授课科目dialog窗口
     */
    public void openSubjectsPicker() {
        JSONObject subjects=null;
        if(StringUtils.isEmpty(ElearnApplication.teachBusinessInfo.getTeachSubject())){
             subjects= (JSONObject) JSONObject.parse( ElearnApplication.teachBusinessInfo.getTeachSubject());
        }else{
            subjects=ElearnApplication.getSubjects();
        }
        Set<String> set=subjects.keySet();
        Iterator<String> iterator=set.iterator();
        int  size=set.size();
        subjectList=new String[size];
        for(int i=0;i<size && iterator.hasNext();i++){
            String key=(String) iterator.next();
            subjectList[i]= (String) subjects.get( key );
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>( this,R.layout.custom_spinner,subjectList );
        arrayAdapter.setDropDownViewResource( R.layout.custom_spinner_dialog_picker );
        sp_subjects.setAdapter( arrayAdapter );
    }


    /**
     * 获取传入参数，并初始化对象内容
     */
    private void initExtra() {
        //2.获取操作类型,edit编辑，add新增
        action = getIntent().getStringExtra( "action" );
        //3.判断生成vo和List对象
        String tmpCourse = getIntent().getStringExtra( "courseVO" );
        if (StringUtils.isEmpty( tmpCourse )) {
            //没有传递数据
            courseVO = new CourseVO();
        } else {
            courseVO = JSONObject.parseObject( tmpCourse, CourseVO.class );
        }
        if ("edit".equals( action )) {
            //4.初始化界面数据
            initData();
            //5.初始化删除按钮的监听
            initDelListener();
        } else {
            initLists();
            bt_del_item.setVisibility( View.GONE );
        }
    }

    /**
     * 初始化删除按钮
     */
    private void initDelListener() {
        bt_del_item.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
            }
        } );
    }

    /**
     * 初始化下拉列表
     */
    private void initLists(){
        openGradePicker();
        openClassPicker(null);
        openCourseTypePicker();
        openPersonsPicker(maxPerson);
        openSubjectsPicker();
    }

    /**
     * 初始化对象
     */
    private void initData() {
        initLists();
        tv_course_name.setText( courseVO.getCourseName()+"" );
        tv_prices.setText( courseVO.getSourcePrice() == null ? "" : courseVO.getSourcePrice().toString() );
        tv_min_subject.setText( courseVO.getMinBuy()+"" );
        tv_max_subject.setText( courseVO.getLessons()+"" );
        et_duration.setText(courseVO.getDuration());

        //找到学部的位置
        for(int i=0;i<gradeList.length;i++){
            if(gradeList[i].equals( courseVO.getCourseGrade() )){
                sp_grade.setSelection( i );
                break;
            }
        }
        //找到年级的位置
        for(int i=0;i<classList.length;i++){
            if(classList[i].equals( courseVO.getCourseClass() )){
                sp_class.setSelection( i );
                break;
            }
        }
        //找到课程类型的位置
        for(int i=0;i<courseTypeList.length;i++){
            if(courseTypeList[i].equals( courseVO.getType() ) ){
                sp_type.setSelection( i );
                break;
            }
        }
        //找到学员数量的位置
        for(int i=0;i<personList.length;i++){
            if(personList[i].equals( courseVO.getPeopleNum() )){
                sp_persons.setSelection( i );
                break;
            }
        }

        //找到学院数量的位置
        for(int i=0;i<subjectList.length;i++){
            if(courseVO.getPeopleNum()==subjectList[i]){
                sp_subjects.setSelection( i );
                break;
            }
        }
    }


    /**
     * 初始化title，添加副标题，并给副标题添加监听
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "课程编辑", R.color.titleWhite )
                .setSubTitle( "保存", R.color.titleWhite, saveCourseListener);
    }
}
