package com.linkingwin.elearn.teacher.activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.constant.XhlmConstant;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.common.util.ImageSelectorUtil;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.JobLevelVO;
import com.linkingwin.elearn.teacher.widget.EnumDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class JobLevel extends BaseActivity {
    @BindView(R.id.title_level)
    TitleView titleView;
    @BindView(R.id.iv_container)
    ImageView iv_container;
    @BindView( R.id.tv_level )
    TextView tv_level;

    public static final int REQUEST_CODE = XhlmConstant.JOB_LEVEL_UPLOAD_REQUEST_CODE;//APP应用内唯一
    private EnumDialog enumDialog;
    private GlideLoader glideImageLoader = new GlideLoader();//图片展示工具
    private String pictureUrl = "";//图片地址
    private boolean isChange = false;//是否修改
    private View.OnClickListener onClickListener;//保存按钮监听
    private boolean progressShow;//进度展示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_job_level );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        saveJoblevel();//初始化副标题监听
        initTitle();//初始化title和副标题
        initJobLevel();
//        iv_container.setOnClickListener((View v) -> {
//                ArrayList<String> urlPath = new ArrayList<>();
//                urlPath.add(pictureUrl);
//                Intent intent = new Intent(getContext(), MultiImgShowActivity.class);
//                intent.putStringArrayListExtra("photos", urlPath);
//                intent.putExtra("position", 0);
//                Activity ac = (Activity) getContext();
//                ac.startActivity(intent);
//                ac.overridePendingTransition(com.jaiky.imagespickers.R.anim.zoom_in, 0);
//        });
    }

    /**
     * 选择职称
     */
    @OnClick(R.id.rl_level)
    public void openEducation(View view) {
        //1.初始化bottomeDialog的相关视图
        initDialog();
        //2.设置dialog的title
        enumDialog.setTitle( "选择职称" );
        //4.生成职级枚举和初始化位置
        JSONObject jobLevels = ElearnApplication.jobLevels;
        JSONObject joblevel=JSONObject.parseObject( ElearnApplication.teachBusinessInfo.getJobLevel());
        String selected=joblevel.getString("levelName");

        List<String> listJobLevels = new ArrayList<>();
        int position = 0;
        for (Map.Entry<String, Object> entry : jobLevels.entrySet()) {
            String value= (String) entry.getValue();
            listJobLevels.add( value + "" );
            if(!StringUtils.isEmpty( selected) && selected.equals(value)){
                position = Integer.parseInt( entry.getKey() ) - 1;
            }
        }
        enumDialog.setWheelData( R.id.wv_job_level, listJobLevels );
        enumDialog.setWheelPosition( R.id.wv_job_level, position );
        //5.点击职级控件，职称图层显示显示
        enumDialog.setVisiable( R.id.ll_job_level, View.VISIBLE );
    }

    /**
     * 加载底部职称dialog
     */
    private void initDialog() {
        //初始化
        enumDialog = new EnumDialog();
        enumDialog.initDialog( this );
        //监听
        enumDialog.setCancelOnclickListener( new ClickAction() );
        enumDialog.setConfirmOnclickListener( new ClickAction() );
    }

    /**
     * 照片选择上传
     * @param view
     */
    @OnClick(R.id.iv_level)
    public void openImgPicker(View view) {
        ImageSelectorUtil.initImageSelector(REQUEST_CODE, this);
    }


    /**
     * 初始化title
     */
    private void initTitle() {
        //定义title的样式
        titleView.setTitleText( "职称",R.color.titleWhite )
                .setSubTitle( "保存",R.color.titleWhite,onClickListener );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> path = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            pictureUrl = path.get(0);
            isChange = true;
            glideImageLoader.displayImage( getContext(), pictureUrl, iv_container);
        }
    }

    /**
     * bottomDialog点击取消、确认的监听实现
     */
    private class ClickAction implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel:
                    Log.d( "dialogCancel", "=========这里执行取消操作=========" );
                    enumDialog.dismissDialog();
                    enumDialog = null;
                    break;
                case R.id.confirm:
                    //todo
                    tv_level.setText( enumDialog.getJobLevel().getSelectionItem()+"" );
                    enumDialog.dismissDialog();
                    enumDialog = null;
                    break;
            }
        }
    }

    /**
     * 保存
     */
    public void saveJoblevel(){
        onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //图片更新先上传图片再保存信息，没有更新直接保存
                if(isChange){
                    uploadImage(new File(pictureUrl));
                } else {
                    submitJobLevel();
                }
            }
        };
    }


    /**
     * 上传图片
     * @param file
     */
    public void uploadImage(File file){
        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("JobLevel", "Save_onCancel");
                progressShow = false;
            }
        });
        pd.setMessage("图片上传中");
        pd.show();
        RequestParams params = RequestParamsBuilder.buildRequestParams( this );
        params.addFormDataPart( "file", file);
        params.addFormDataPart( "merchantId", XhlmConstant.UPLOAD_MERCHANTID );
        params.addFormDataPart( "channel", XhlmConstant.UPLOAD_CHANNEL );
        HttpRequest.post( Api.POST_UPLOAD_FILE, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                if (jsonObject.getBoolean( "success" )) {
                    pictureUrl = jsonObject.getString("result");
                    submitJobLevel();
                    if(pd.isShowing()){
                        pd.dismiss();
                    }
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( JobLevel.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                Log.d( "submit_auth_fail", errorCode + ":" + msg );
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "submit_auth_end", "请求结束" );
            }
        });
    }

    /**
     * 提交信息
     */
    public void submitJobLevel(){
        RequestParams params = RequestParamsBuilder.buildRequestParams(JobLevel.this);
        JobLevelVO jobLevelVO = new JobLevelVO();
        jobLevelVO.setLevelName(tv_level.getText().toString());
        jobLevelVO.setPictureUrl(pictureUrl);
        final String jobJson = JSONObject.toJSONString(jobLevelVO);
        params.addFormDataPart("jobLevel", jobJson);
        HttpRequest.post(Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
            //请求成功
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String msg = jsonObject.getString( "msg" );
                if (jsonObject.getBoolean( "success" )) {
                    //更新全局变量,提交成功更新本地的shared数据
                    ElearnApplication.teachBusinessInfo.setJobLevel(jobJson);
                    Tools.setShareInfo( getContext(),"TeachBusinessInfo",ElearnApplication.teachBusinessInfo);
                    makeText( JobLevel.this, msg, LENGTH_SHORT ).show();
                }
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( JobLevel.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                Log.d( "submit_auth_fail", errorCode + ":" + msg );
            }
            //请求网络结束
            @Override
            public void onFinish() { Log.d( "submit_auth_end", "请求结束" );
            }
        });
    }

    /**
     * 页面初始化
     */
    public void initJobLevel(){
        String jobStr = ElearnApplication.teachBusinessInfo.getJobLevel();
        if(!StringUtils.isBlank(jobStr)){
            JSONObject jobJSON = JSONObject.parseObject(jobStr);
            tv_level.setText(jobJSON.getString("levelName"));
            pictureUrl = jobJSON.getString("pictureUrl");
            glideImageLoader.displayImage( this, pictureUrl, iv_container );
        }
    }
}
