package com.linkingwin.elearn.teacher.activities;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
import com.linkingwin.elearn.common.widget.DialogFromBottom;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;

import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class TeacherApplySubject extends BaseActivity {
    @BindView( R.id.title_apply_subject )
    TitleView titleView;

    @BindView (R.id.apply_tv_realyname)
    TextView realyname;

    @BindView (R.id.apply_tv_otherauth)
    TextView myOtherAuth;

    @BindView (R.id.apply_tv_personRes)
    TextView personRes;

    @BindView (R.id.apply_tv_gradeSubject)
    TextView gradeSubject;

    @BindView (R.id.apply_tv_subjectTime)
    TextView subjectTime;

    @BindView (R.id.tv_apply_baserule)
    TextView baseRule;


    @BindView (R.id.tv_apply_studyplatformrule)
    TextView studyplatformrule;

    @BindView (R.id.tv_apply_studyvideo)
    TextView studyvideo;

    @BindView(R.id.bt_submit)
    Button btSubmit;

    private JSONObject json_TeachGrade;
    private JSONObject json_TeachSubject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_teacher_apply_subject);
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        //1.?????????titile?????????
        initTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }


    /**
     * ?????????Title
     */
    private void initTitle() {
        //??????title?????????
        titleView.setTitleText( "????????????",  R.color.titleWhite  );
    }

    private void loadData() {
        boolean t1,t2,t3,t4,t5,t6,t7;
        TeachBusinessInfo teachBizInfo = ElearnApplication.teachBusinessInfo;
        if(StringUtils.isEmpty (teachBizInfo.getRealNameAuth ()) ){
            realyname.setText ("????????????????????????");
            t1=false;
        }else{
            realyname.setText ("?????????????????????");
            t1=true;
        }

        if(StringUtils.isEmpty (teachBizInfo.getOtherAuth ())){
            myOtherAuth.setText ( "????????????????????????" );
            t2=false;
        }else{
            myOtherAuth.setText ( "?????????????????????");
            t2=true;
        }

        if(StringUtils.isEmpty (teachBizInfo.getNickName ()) ){
            personRes.setText ("????????????");
            t3=false;
        }else{
            personRes.setText ( "?????????");
            t3=true;
        }

        JSONArray jTimes= (JSONArray)JSONArray.parse(Tools.getSharedInfo("times", Tools.getSharedPre(this), String.class));
        if(jTimes.size()==0){
            //????????????
            subjectTime.setText( "????????????");
            t4=false;
        }else{
            //????????????
            subjectTime.setText( "?????????");
            t4=true;
        }

        if(StringUtils.isEmpty( teachBizInfo.getSubDep() )){
            gradeSubject.setText ("?????????????????????");
            t5=false;
        }else{
            gradeSubject.setText ("?????????");
            t5=true;
        }

        if("0".equals(teachBizInfo.getBasicService())){
            baseRule.setText("???????????????????????????" );
            t6=false;
        }else{
            baseRule.setText( "?????????" );
            t6=true;
        }

        if( "0".equals(teachBizInfo.getPlatformRuls())){
            studyplatformrule.setText("???????????????????????????");
            t7=false;
        }else{
            studyplatformrule.setText("?????????" );
            t7=true;
        }
        if(t1&&t2&&t3&&t4&&t5&&t6&&t7){
            btSubmit.setOnClickListener(v -> {
                submitApplySubject();
            });
        }else{
            btSubmit.setOnClickListener(v -> {
                Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT);
            });
        }
    }

    private void submitApplySubject(){
        RequestParams params=RequestParamsBuilder.buildRequestParams(this);
        params.addFormDataPart( "applyTeachStatus","1" );
        params.addFormDataPart( "platformRuls","1" );
        params.addFormDataPart( "basicService","1" );
        HttpRequest.post(Api.POST_SAVE_TEACH_INFO,new JsonHttpRequestCallback(){
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                JSONObject result=jsonObject.getJSONObject( "result" );
                ElearnApplication.teachBusinessInfo.setApplyTeachStatus( "1" );
                Tools.setShareInfo( getContext(), "TeachBusinessInfo", ElearnApplication.teachBusinessInfo );
                    makeText( TeacherApplySubject.this,"????????????",LENGTH_SHORT ).show();
                    finish();
            }
            @Override
            public void onFailure(int errorCode, String msg) {
            }

            @Override
            public void onFinish() {
            }
        });
    }

    //????????????
    @OnClick(R.id.rl_basic_service)
    public void openBasicService(View view){
        Tools.toOtherPage(this,BasicServiceCriteria.class,false,null);
    }

    @OnClick(R.id.rl_teach_time)
    public void openTeachTime(View view){
        Tools.toOtherPage(this,TeachTime.class,false,null);
    }

    @OnClick(R.id.ll_about_me)
    public void openAboutMe(View view){
        Tools.toOtherPage(this,AboutMe.class,false,null);
    }

    @OnClick(R.id.rl_platform_rule)
    public void openPlatformRules(View view){
        Tools.toOtherPage(this,PlatformRules.class,false,null);
    }

    @OnClick(R.id.apply_tv_realyname)
    public void openRealAuth(View view){
        Tools.toOtherPage(this,RealAuth.class,false,null);
    }

    @OnClick(R.id.apply_tv_otherauth)
    public void openOtherAuth(View view){
        Tools.toOtherPage(this,OtherAuth.class,false,null);
    }

    @OnClick(R.id.bt_submit)
    public void applySubject(View view){

    }



    /**
     * ???????????????????????????dialog
     */
    @OnClick(R.id.rl_teach_exp)
    public void openSubject(View view) {
        json_TeachGrade= JSONObject.parseObject( ElearnApplication.teachBusinessInfo.getTeachGrade() );
        json_TeachSubject= JSONObject.parseObject( ElearnApplication.teachBusinessInfo.getTeachSubject() );
        DialogSubjectAge dialogSubjectAge=new DialogSubjectAge(this,json_TeachGrade,json_TeachSubject);
        //?????????bottomeDialog???????????????
        dialogSubjectAge.initDialog().setSubjectView(gradeSubject).setTitle("subject").setSubmitSubjects(true);
    }
}
