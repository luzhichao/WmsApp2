package com.linkingwin.elearn.teacher.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.alibaba.fastjson.JSONObject;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.constant.XhlmConstant;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.DateUtils;
import com.linkingwin.elearn.common.util.FileUtils;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.ImageViewPlay;
import com.linkingwin.elearn.common.widget.TitleView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class VideoActivity extends BaseActivity {

    @BindView(R.id.title_video)
    TitleView title_video;

    @BindView(R.id.iv_show_video)
    ImageViewPlay show_video;
    @BindView(R.id.vv_play_video)
    VideoView play_video;

    ArrayList<Media> select = new ArrayList<>();
    private static final int REQUEST_CODE = XhlmConstant.VIDEO_UPLOAD_REQUEST_CODE;
    String outputDir = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        show_video.setType(ImageViewPlay.TYPE_VIDEO);
        //init title
        title_video.setTitleText("个人视频", R.color.titleWhite);
        //TODO init data
        initVideo();
    }

    /**
     * 选择视频
     */
    @OnClick(R.id.select_video)
    public void selectVideo(){
        Intent intent = new Intent(VideoActivity.this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO);//default image and video (Optional)
        long maxSize = 188743680L;//long long long
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);  //default 40 (Optional)
        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, select); // (Optional)
        VideoActivity.this.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == PickerConfig.RESULT_CODE && data != null){
            ArrayList<Media> selectFiles = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (selectFiles != null && selectFiles.size() > 0){
                //压缩视频
                outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "VID_" + DateUtils.getNowTimeStr() + ".mp4";
                FileUtils.compressFile(selectFiles.get(0).path, outputDir, play_video);
            }
        }
    }

    /**
     * 确认提交
     */
    @OnClick(R.id.submit_video)
    public void submitVideo(){
        uploadVideo();
    }

    /**
     * 上传视频
     */
    private void uploadVideo(){
        RequestParams params = RequestParamsBuilder.buildRequestParams(VideoActivity.this);
        params.addFormDataPart("file" , new File(outputDir));
        params.addFormDataPart("merchantId", XhlmConstant.UPLOAD_MERCHANTID);
        params.addFormDataPart("channel", XhlmConstant.UPLOAD_CHANNEL);
        HttpRequest.post(Api.POST_UPLOAD_FILE, params, new JsonHttpRequestCallback() {
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String msg=jsonObject.getString( "message" );
                if (jsonObject.getBoolean( "success" )) {
                    //保存处理
                    String videoUrl = jsonObject.getString("result");
                    saveVideo(videoUrl);
                }
                makeText( VideoActivity.this,msg , LENGTH_SHORT ).show();
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( VideoActivity.this, "网络原因，提交失败", LENGTH_SHORT ).show();
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
     * 保存个人视频信息
     * @param videoUrl
     */
    private void saveVideo(String videoUrl){
        RequestParams params = RequestParamsBuilder.buildRequestParams(VideoActivity.this);
        params.addFormDataPart("personalVideo", videoUrl);
        HttpRequest.post(Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
            //请求成功
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String msg = jsonObject.getString( "msg" );
                if (jsonObject.getBoolean( "success" )) {
                    //更新全局变量,提交成功更新本地的shared数据
                    ElearnApplication.teachBusinessInfo.setPersonalVideo(videoUrl);
                    Tools.setShareInfo( getContext(),"TeachBusinessInfo",ElearnApplication.teachBusinessInfo);
                    makeText( VideoActivity.this, msg, LENGTH_SHORT ).show();
                    finish();
                }
            }
            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( VideoActivity.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                Log.d( "submit_auth_fail", errorCode + ":" + msg );
            }
            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "submit_auth_end", "请求结束" );
            }
        });
    }

    public void initVideo(){

        String videoUrl = ElearnApplication.teachBusinessInfo.getPersonalVideo();
        if(!StringUtils.isBlank(videoUrl)){
//        String path = "";
//        String path = "http://139.196.127.66/linkingwin/file/view/100001612252057600";
//        String path = "http://139.196.127.66/linkingwin/file/view/100359983794753536";
//        String path = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//        String path = "http://139.196.127.66:7011/linkingwin/file/view/100027936685428736";
            Uri uri = Uri.parse(videoUrl);
            play_video.setVideoURI(uri);
        }
//        //播放完成回调
//        play_video.setOnCompletionListener(new MyPlayerOnCompletionListener());
        MediaController mediaController = new MediaController(VideoActivity.this);
        mediaController.setMediaPlayer(play_video);
        mediaController.show();
        play_video.setMediaController(mediaController);//显示控制栏
        play_video.requestFocus();
//        play_video.start();//开始播放视频
    }

//    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
//        @Override
//        public void onCompletion(MediaPlayer mp) {
//            Toast.makeText( VideoActivity.this, "播放完成了", Toast.LENGTH_SHORT).show();
//        }
//    }
}


