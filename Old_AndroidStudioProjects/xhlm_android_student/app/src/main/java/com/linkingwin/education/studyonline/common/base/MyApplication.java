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
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现

        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        SharedPreUtil.initSharedPreference(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        // 初始化环信mob
//        initEeasemob();
        initEaseUi();
    }

    private void initEaseUi() {
        EaseUI.getInstance().init(this, null);
//        EMClient.getInstance().setDebugMode(true);
    }

//    private void initEeasemob() {
//        int pid = android.os.Process.myPid();
//        String processAppName = getAppName(pid);
//        // 如果APP启用了远程的service，此application:onCreate会被调用2次
//        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
//        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
//        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
//            Log.e(this.getPackageName (), "enter the service process!");
//            // 则此application::onCreate 是被service 调用的，直接返回
//            return;
//        }
//        EMOptions options = new EMOptions();
//        // 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
//        options.setAutoTransferMessageAttachments(true);
//        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
//        options.setAutoDownloadThumbnail(true);
//        //初始化
//        EMClient.getInstance().init(this, options);
//        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);
//
//    }
//
//    private String getAppName(int pID) {
//        String processName = null;
//        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//        List l = am.getRunningAppProcesses();
//        Iterator i = l.iterator();
//        PackageManager pm = this.getPackageManager();
//        while (i.hasNext()) {
//            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
//            try {
//                if (info.pid == pID) {
//                    processName = info.processName;
//                    return processName;
//                }
//            } catch (Exception e) {
//                // Log.d("Process", "Error>> :"+ e.toString());
//            }
//        }
//        return processName;
//    }


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
