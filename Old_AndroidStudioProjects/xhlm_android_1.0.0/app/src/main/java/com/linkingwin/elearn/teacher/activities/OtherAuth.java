package com.linkingwin.elearn.teacher.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jaiky.imagespickers.ImageConfig;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

public class OtherAuth extends BaseActivity {

    @BindView(R.id.title_other_auth)
    TitleView titleView;

    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    private ImageConfig imageConfig;

    @BindView(R.id.select_other_auth)
    Button select_other_auth;
    @BindView(R.id.submit_auth)
    Button submit_auth;

    public static final int REQUEST_CODE = XhlmConstant.OTHER_AUTH_UPLOAD_REQUEST_CODE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_auth);
        ButterKnife.bind(this);
        setTranslucentStatusBar(true);
        initOtherAuth();
        titleView.setTitleText("????????????", R.color.titleWhite);
    }

    @OnClick(R.id.select_other_auth)
    public void selectOtherAuth(View v) {
        ImageSelectorUtil.openMutiSelect(imageConfig, OtherAuth.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.submit_auth)
    public void submitAuth(View view) {
        List<String> paths = imageConfig.getContainerAdapter().getmDatas();
        if (paths != null && paths.size() > 0) {
            List<File> files = new ArrayList<>();//?????????????????????
            final List<String> oldPaths = new ArrayList<>();
            for (String d : paths) {
                if (!d.startsWith("http:")) {
                    files.add(new File(d));
                } else {
                    oldPaths.add(d);
                }
            }
            //????????????????????????????????????????????????
            if (files != null && files.size() > 0) {
                batchUploadImage(files, oldPaths);
            } else {
                //????????????????????????????????????
                saveData(String.join(",",oldPaths));
            }
        } else {
            makeText(OtherAuth.this, "???????????????", LENGTH_SHORT).show();
        }
    }

    /**
     * ????????????
     */
    public void batchUploadImage(List<File> files, final List<String> oldPaths) {
        RequestParams params = RequestParamsBuilder.buildRequestParams(OtherAuth.this);
        params.addFormDataPartFiles("files", files);
        params.addFormDataPart("merchantId", XhlmConstant.UPLOAD_MERCHANTID);
        params.addFormDataPart("channel", XhlmConstant.UPLOAD_CHANNEL);
        HttpRequest.post(Api.POST_BATCH_UPLOAD_FILE, params, new JsonHttpRequestCallback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess(jsonObject);
                String msg = "??????????????????";
                if (jsonObject.getBoolean("success")) {
                    String result = jsonObject.getString("result");
                    List<String> resultList=JSONArray.parseArray(result, String.class);
                    oldPaths.addAll(resultList);
                    saveData(String.join(",",oldPaths));
                }
                makeText(OtherAuth.this, "??????????????????", LENGTH_SHORT).show();
            }

            //?????????????????????????????????JSON????????????????????????????????????
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText(OtherAuth.this, "???????????????????????????", LENGTH_SHORT).show();
                Log.d("submit_auth_fail", errorCode + ":" + msg);
            }

            //??????????????????
            @Override
            public void onFinish() {
                Log.d("submit_auth_end", "????????????");
            }
        });
    }

    /**
     * ????????????
     *
     * @param otherAuthUrls
     */
    public void saveData(final String otherAuthUrls) {
        if (!StringUtils.isBlank(otherAuthUrls)) {
            RequestParams params = RequestParamsBuilder.buildRequestParams(OtherAuth.this);
            params.addFormDataPart("otherAuth", otherAuthUrls);
            HttpRequest.post(Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                //????????????
                @Override
                protected void onSuccess(JSONObject jsonObject) {
                    super.onSuccess(jsonObject);
                    String msg = jsonObject.getString("msg");
                    if (jsonObject.getBoolean("success")) {
                        //??????????????????,???????????????????????????shared??????
                        ElearnApplication.teachBusinessInfo.setOtherAuth(otherAuthUrls);
                        Tools.setShareInfo(getContext(), "TeachBusinessInfo", ElearnApplication.teachBusinessInfo);
                        makeText(OtherAuth.this, msg, LENGTH_SHORT).show();
                    }
                }

                //?????????????????????????????????JSON????????????????????????????????????
                @Override
                public void onFailure(int errorCode, String msg) {
                    makeText(OtherAuth.this, "???????????????????????????", LENGTH_SHORT).show();
                    Log.d("submit_auth_fail", errorCode + ":" + msg);
                }

                //??????????????????
                @Override
                public void onFinish() {
                    Log.d("submit_auth_end", "????????????");
                }
            });
        } else {
            makeText(OtherAuth.this, "???????????????", LENGTH_SHORT).show();
        }
    }

    public void initOtherAuth() {
        String otherAuth = ElearnApplication.teachBusinessInfo.getOtherAuth();
        ArrayList<String> paths;   //?????????????????????
        if (!StringUtils.isEmpty(otherAuth)) {
            paths=new ArrayList<>(Arrays.asList(otherAuth.split(",")));
        } else {
            paths=new ArrayList<>();
        }

        //????????????????????????
        imageConfig = ImageSelectorUtil.initImageMutiSelector(REQUEST_CODE, OtherAuth.this, 5, 3, true, paths, llContainer);
        ImageSelectorUtil.show(imageConfig, paths);
    }
}
