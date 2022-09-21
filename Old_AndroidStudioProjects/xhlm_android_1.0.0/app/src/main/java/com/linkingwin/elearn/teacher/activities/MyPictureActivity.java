package com.linkingwin.elearn.teacher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MyPictureActivity extends BaseActivity {

    @BindView(R.id.title_my_picture)
    TitleView titleView;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;

    ImageConfig imageConfig;
//    private ArrayList<String> paths = new ArrayList<>();
    private ArrayList<String> oldPaths = new ArrayList<>();
    public static final int REQUEST_CODE = XhlmConstant.MY_PICTURE_UPLOAD_REQUEST_CODE;//APP应用内唯一
    private boolean isChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_picture);
        ButterKnife.bind( this );  //必须要加，注解才会生效否则报Null错误
        setTranslucentStatusBar( true );
        //定义title的样式
        titleView.setTitleText( "个人照片", R.color.titleWhite );

        initMyPicture();
    }

    /**
     * 选择个人照片
     * @param view
     */
    @OnClick(R.id.select_picture)
    public void selectPicture(View view){
        ImageSelectorUtil.openMutiSelect(imageConfig, this);
    }

    /**
     * 提交照片
     * @param view
     */
    @OnClick(R.id.submit_picture)
    public void submitPicture(View view){
        if(isChange){
            List<String> paths = imageConfig.getContainerAdapter().getmDatas();
            ArrayList<File> files = new ArrayList<>();
            oldPaths.clear();
            for(String path : paths){
                if(path.startsWith("http:")){
                    oldPaths.add(path);
                }else{
                    files.add(new File(path));
                }
            }
            if(files.size() > 0){
                uploadImage(files, oldPaths);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            isChange = true;
        }
    }

    /**
     * 选择照片
     * @param files
     * @param oldPaths
     */
    public void uploadImage(ArrayList<File> files, ArrayList<String> oldPaths){
        RequestParams params = RequestParamsBuilder.buildRequestParams(MyPictureActivity.this);
        params.addFormDataPartFiles("files" , files);
        params.addFormDataPart("merchantId", XhlmConstant.UPLOAD_MERCHANTID);
        params.addFormDataPart("channel", XhlmConstant.UPLOAD_CHANNEL);
        HttpRequest.post(Api.POST_BATCH_UPLOAD_FILE, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String msg = jsonObject.getString( "message" );
                if (jsonObject.getBoolean( "success" )) {
                    String result = jsonObject.getString("result");
                    for(String old : oldPaths){
                        result += old;
                    }
                    //更新全局变量,提交成功更新本地的shared数据
                    ElearnApplication.teachBusinessInfo.setPhoto(result);
                    Tools.setShareInfo( getContext(),"TeachBusinessInfo",ElearnApplication.teachBusinessInfo);
                    ImageSelectorUtil.show(imageConfig, oldPaths);
                }
                makeText( MyPictureActivity.this, msg, LENGTH_SHORT ).show();
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( MyPictureActivity.this, "网络原因，提交失败", LENGTH_SHORT ).show();
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
     * 初始化个人照片
     */
    public void initMyPicture(){
        String myPhoto = ElearnApplication.teachBusinessInfo.getPhoto();
        ArrayList<String> paths = new ArrayList<>();
        if(!StringUtils.isBlank(myPhoto)){
            JSONArray jsonArray = JSONArray.parseArray(myPhoto);
            for(int i=0;i<jsonArray.size();i++){
                oldPaths.add(jsonArray.getString(i));
                paths.add(jsonArray.getString(i));
            }
        }
        imageConfig = ImageSelectorUtil.initImageMutiSelector(REQUEST_CODE, this, 6, 3, true, paths, llContainer);
        ImageSelectorUtil.show(imageConfig, paths);
    }
}
