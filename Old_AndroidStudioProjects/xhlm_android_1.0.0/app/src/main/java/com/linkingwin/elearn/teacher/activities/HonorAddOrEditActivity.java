package com.linkingwin.elearn.teacher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jaiky.imagespickers.ImageConfig;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.constant.XhlmConstant;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.ImageSelectorUtil;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.HonorVO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class HonorAddOrEditActivity extends BaseActivity {

    @BindView(R.id.et_honor_intro)
    EditText et_honor_intro;
    @BindView(R.id.tv_title)
    TitleView titleView;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindView(R.id.del_honor)
    Button del_honor;
    private ImageConfig imageConfig;

    public static final int REQUEST_CODE = XhlmConstant.HONOR_UPLOAD_REQUEST_CODE;//APP应用内唯一
    private ArrayList<HonorVO> allData;//所有数据
    private HonorVO currData;//当前行数据
    private ArrayList<String> pictureUrls = new ArrayList<>();//选择的图片
    private boolean isChange = false;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honor_add_or_edit);
        setTranslucentStatusBar(true);
        ButterKnife.bind( this );
        /*直接在main Thread 进行网络操作*/
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());

        allData = (ArrayList<HonorVO>)this.getIntent().getSerializableExtra("allData");
        currData = (HonorVO)this.getIntent().getSerializableExtra("currData");
        isEdit = this.getIntent().getBooleanExtra("isEdit", false);

        if(isEdit){
            del_honor.setVisibility(View.VISIBLE);
        }
        //init title and listener
        titleView.setTitleText("奖励荣誉", R.color.titleWhite).setSubTitle("保存", R.color.titleWhite, v -> {
            boolean b = submitHonor();
        });
        //init data
        initHonor();
    }

    /**
     * 选择图片后处理方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            isChange = true;
        }
    }

    /**
     * 保存信息
     */
    public boolean submitHonor(){
        List<String> paths = imageConfig.getContainerAdapter().getmDatas();//获取图片
        if (StringUtils.isBlank(et_honor_intro.getText().toString()) || paths == null || paths.size() <= 0){
            makeText( HonorAddOrEditActivity.this, "信息不全", LENGTH_SHORT ).show();
            return false;
        } else {
            if (isChange){
                ArrayList<File> newFiles = new ArrayList<>();
                pictureUrls.clear();
                for(String path : paths){
                    if(path.startsWith("http:")){
                        pictureUrls.add(path);
                    } else {
                        newFiles.add(new File(path));
                    }
                }
                batchUploadHonor(newFiles);
            } else {
                currData.setHonorIntro(et_honor_intro.getText().toString());
                currData.setPictureUrls(pictureUrls);
                allData.add(currData);
                String honorStr = JSONObject.toJSONString(allData);
                saveHonor(honorStr);
            }
        }
        return true;
    }

    /**
     * 删除该条记录
     * @param view
     */
    @OnClick(R.id.del_honor)
    public void delCurrHonor(View view){
        if (isEdit){
            allData.remove(currData);
            String honorStr = JSONObject.toJSONString(allData);
            saveHonor(honorStr);
        }
    }

    /**
     * 选择图片
     */
    @OnClick(R.id.select_image)
    public void selectImage(){
        ImageSelectorUtil.openMutiSelect(imageConfig, HonorAddOrEditActivity.this);
    }

    /**
     * 上传图片
     * @param files
     */
    public void batchUploadHonor(List<File> files){
        RequestParams params = RequestParamsBuilder.buildRequestParams(HonorAddOrEditActivity.this);
        params.addFormDataPartFiles("files" , files);
        params.addFormDataPart("merchantId", XhlmConstant.UPLOAD_MERCHANTID);
        params.addFormDataPart("channel", XhlmConstant.UPLOAD_CHANNEL);
        HttpRequest.post(Api.POST_BATCH_UPLOAD_FILE, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String msg = jsonObject.getString( "message" );
                if (jsonObject.getBoolean( "success" )) {
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i=0;i<result.size();i++){
                        pictureUrls.add(result.getString(i));
                    }
                    currData.setHonorIntro(et_honor_intro.getText().toString());
                    currData.setPictureUrls(pictureUrls);
                    allData.add(currData);
                    final String honorStr = JSONObject.toJSONString(allData);
                    saveHonor(honorStr);
                }
                makeText( HonorAddOrEditActivity.this, msg, LENGTH_SHORT ).show();
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( HonorAddOrEditActivity.this, "网络原因，提交失败", LENGTH_SHORT ).show();
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
     * 保存数据
     */
    public void saveHonor(final String honorStr){
        RequestParams params = RequestParamsBuilder.buildRequestParams(HonorAddOrEditActivity.this);
        params.addFormDataPart("honor", honorStr);
        HttpRequest.post(Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
            //请求成功
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String msg = jsonObject.getString( "msg" );
                if (jsonObject.getBoolean( "success" )) {
                    //更新全局变量,提交成功更新本地的shared数据
                    ElearnApplication.teachBusinessInfo.setHonor(honorStr);
                    Tools.setShareInfo( getContext(),"TeachBusinessInfo",ElearnApplication.teachBusinessInfo);
                    makeText( HonorAddOrEditActivity.this, msg, LENGTH_SHORT ).show();
                    finish();
                }
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( HonorAddOrEditActivity.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                Log.d( "submit_auth_fail", errorCode + ":" + msg );
            }
            //请求网络结束
            @Override
            public void onFinish() { Log.d( "submit_auth_end", "请求结束" );
            }
        });

    }


    public void initHonor(){
        ArrayList<String> paths = new ArrayList<>();
        if(currData != null){
//            pictureUrls = currData.getPictureUrls();
            paths = currData.getPictureUrls();
            et_honor_intro.setText(currData.getHonorIntro());
        } else {
            currData = new HonorVO();
        }
        //初始化图片选择器
        imageConfig = ImageSelectorUtil.initImageMutiSelector(REQUEST_CODE, HonorAddOrEditActivity.this, 5, 3, true, paths, llContainer);
        ImageSelectorUtil.show(imageConfig, paths);
    }



}
