package com.linkingwin.education.studyonline.common.base;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyphenate.easeui.EaseUI;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;
import com.linkingwin.education.studyonline.common.utils.SharedPreUtil;

import org.xutils.x;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @描述 继承Application
 * @作者 gsh
 * @时间 2017/3/29 9:16
 */

public class MyApplication extends Application {


    public LoginUserVO user;

    public static Map<String, String> userMaps = new HashMap<>();
    /**
     * 打开的activity
     **/
    private Set<Activity> activities = new HashSet<> ();
    /**
     * 是否显示“app更新提示框”
     */
    public boolean showUpdate = true;

    public static volatile MyApplication instance;


    public static MyApplication getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (MyApplication.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        SharedPreUtil.initSharedPreference(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // 初始化环信mob
        initEaseUi();
    }

    private void initEaseUi() {
        EaseUI.getInstance().init(this, null);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


    /**
     * 新建了一个activity
     *
     * @param activity
     */
    public MyApplication addActivity(Activity activity) {
        activities.add(activity);
        return this;
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }


    /**
     * 应用退出，结束所有的activity
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public boolean isShowUpdate() {
        return showUpdate;
    }

    public void setShowUpdate(boolean showUpdate) {
        this.showUpdate = showUpdate;
    }


    public LoginUserVO getUser() {
        if (user == null) {
            user = SharedPreUtil.getInstance().getUser();
        }
        return user;
    }

    public void setUser(LoginUserVO user) {
        SharedPreUtil.getInstance().putUser(user);
        this.user = user;
    }


    public void setUserMap(JSONArray users) {
        for (Object user : users) {
            JSONObject userobj = JSONObject.parseObject(user.toString());
            userMaps.put(userobj.getString("id"), userobj.getString("uName"));
        }
    }

    public void deleteUser() {
        SharedPreUtil.getInstance().deleteUser();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
