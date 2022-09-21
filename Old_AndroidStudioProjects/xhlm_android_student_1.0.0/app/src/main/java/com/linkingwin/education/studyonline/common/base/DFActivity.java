package com.linkingwin.education.studyonline.common.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;


/**
 * @Desc
 * @Author Willowgao
 * @Version V1.0
 * @Date 2017/12/22
 */

public class DFActivity extends BaseActivity {


    private LoginUserVO user = null;
    private String systemid = null;

    private int REQUEST_CODE_SCAN = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        user = MyApplication.getInstance().getUser();
    }


    public int getAppVersionCode() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo;
        int versionCode = 0;
        try {
            packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public void showVersionUpdate(final JSONObject version) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现新版本");
        builder.setMessage(version.getString("vdesc"));
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse(version.getString("url"));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
}
