package com.linkingwin.elearn.common;

import android.app.Application;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.teacher.model.LocationModel;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.linkingwin.elearn.teacher.widget.BottomBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.finalteam.okhttpfinal.Part;
import cn.finalteam.toolsfinal.StringUtils;
import okhttp3.Headers;
import okhttp3.Interceptor;

public class ElearnApplication extends Application {
    private static boolean isLogin;
    //登陆基本信息
    private static JSONObject jsonLoinInfo = null;
    //TeachBusinessInfo，每次重启APP需要从网络上获取全局变量
    public static TeachBusinessInfo teachBusinessInfo;
    //定义app全局的底部导航栏
    public static BottomBar bottomBar;
    //token
    public static String accessToken;

    //用于记录手工选择的城市信息（基于组件选择的没有经纬度，只有通过定位的才有经纬度）
    private static LocationModel selectCity = new LocationModel();

    //用于记录所有授课学科和科目的所有枚举值
    private static JSONObject subjects;
    private static JSONObject departments;
    //用于记录学历的枚举值
    public static JSONObject educations;
    //用于记录职称的枚举值
    public static JSONObject jobLevels;
    //用于记录教学特点的枚举值
    public static String teachTraitTag;

    //用于记录年月的枚举值
    public static List<String> listYear;//4.生成年
    public static List<String> listMonth;//5.生成月

    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttpFinal();
        initSharedAllInfo();
        initEaseUI();
    }

    /**
     * 环信UI初始化
     */
    private void initEaseUI() {
//        EMOptions options = new EMOptions();
//         //默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
//        options.setAutoTransferMessageAttachments(true);
//        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
//        options.setAutoDownloadThumbnail(true);
//        //初始化
//        EMClient.getInstance().init(this, options);
//        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(true);

        EaseUI.getInstance().init(this, null);
    }

    /**
     * 初始化OkHttpFinal
     */
    private void initOkHttpFinal() {
        List<Part> commomParams = new ArrayList<>();
        Headers commonHeaders = new Headers.Builder().build();
        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setCommenParams(commomParams)
                .setCommenHeaders(commonHeaders)
                .setTimeout(Api.REQ_TIMEOUT)    //设置超时时间
                //.setInterceptors(interceptorList)   //拦截器
                //.setDebug(true)  //打开debug模式
                //.setCookieJar(CookieJar.NO_COOKIES)
                //.setCertificates(...)
                //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
                .setDebug(true);
//        addHttps(builder);
        OkHttpFinal.getInstance().init(builder.build());
    }

    public void initSharedAllInfo() {
        SharedPreferences sharedPre = getSharedPreferences("config", MODE_PRIVATE);
        //登陆信息初始化
        getLoginInfo(sharedPre);
        //departMents字典初始化
        initDepartment(sharedPre);
        //subjects字典初始化
        initSubject(sharedPre);
        //education字典初始化
        initEducations(sharedPre);
        //年月初始化
        initDateSelector();
        //职称-字典初始化
        initJobLevel(sharedPre);
        //教学特点标签枚举值初始化
        initTeachTrait(sharedPre);
    }

    /**
     * 初始化日期选择的枚举值
     */
    public static void initDateSelector() {
        //4.生成年
        int index = 0;
        listYear = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        while (index++ < 100) {
            //从1949年开始100年,当大于当前年份则停止
            listYear.add((index + 1969) + "");
            if (index + 1969 == cal.get(Calendar.YEAR)) break;
        }
        //5.生成月
        listMonth = new ArrayList<>();
        index = 0;
        while (index++ < 12) {
            //从1月-12月
            listMonth.add((index) + "");
        }
    }

    /**
     * 初始化获取登陆时已存储的用户账号信息
     */
    public static void getLoginInfo(SharedPreferences sharedPre) {
        String username = sharedPre.getString("username", "");
        String password = sharedPre.getString("password", "");
        if (!(null == username || username.trim().equals(""))) {
            jsonLoinInfo = new JSONObject();
            jsonLoinInfo.put("username", username);
            jsonLoinInfo.put("password", password);
            isLogin = true;
        }
    }
    /**
     * 初始化教学特点标签枚举值
     */
    public void initTeachTrait(SharedPreferences sharedPre){
        String json= sharedPre.getString("teachTraitEnum", "");
        if(!StringUtils.isEmpty(json)){
            teachTraitTag=json;
        }else{
            teachTraitTag="小升初,高考,中考,机构经验,名校毕业,公校教学经验,带过重点班,普通话标准,擅长自主招生,逻辑性强,陪读,承诺加分";
        }
    }

    /**
     * 初始化职称枚举值
     */
    public void initJobLevel(SharedPreferences sharedPre) {
        String json = sharedPre.getString("jobLevel", "");
        if (!StringUtils.isEmpty(json)) {
            jobLevels = JSONObject.parseObject(json);
        } else {
            jobLevels = JSONObject.parseObject("{\"1\":\"无职称\",\"2\":\"三级教师\",\"3\":\"二级教师\",\"4\":\"一级教师\",\"5\":\"高级教师\",\"6\":\"正高级教师\"}");
        }
    }


    /**
     * 初始化department配置和默认值
     */
    public void initDepartment(SharedPreferences sharedPre) {
        String json = sharedPre.getString("departments", "");
        if (json != "") {
            departments = JSONObject.parseObject(json);
        } else {
            departments = JSONObject.parseObject("{\"1\":\"小学\",\"2\":\"初中\",\"3\":\"高中\"}");
        }
    }

    /**
     * 初始化subject配置和默认值
     */
    public void initSubject(SharedPreferences sharedPre) {
        String json = sharedPre.getString("subjects", "");
        if (json != "") {
            subjects = JSONObject.parseObject(json);
        } else {
            subjects = JSONObject.parseObject("{\"1\":\"数学\",\"2\":\"语文\",\"3\":\"英语\",\"4\":\"物理\",\"5\":\"化学\",\"6\":\"生物\",\"7\":\"自然\",\"8\":\"地理\",\"9\":\"书法\",\"10\":\"奥数\"}");
        }
    }

    /**
     * 初始化学历配置和默认值
     *
     * @return
     */
    public void initEducations(SharedPreferences sharedPre) {
        String json = sharedPre.getString("educations", "");
        if (json != "") {
            educations = JSONObject.parseObject(json);
        } else {
            educations = JSONObject.parseObject("{\"1\":\"高中\",\"2\":\"本科\",\"3\":\"研究生\",\"4\":\"博士\"}");
        }
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean b) {
        isLogin = b;
    }

    public static JSONObject getJsonLoinInfo() {
        return jsonLoinInfo;
    }

    public static LocationModel getSelectCity() {
        return selectCity;
    }

    public static JSONObject getSubjects() {
        return subjects;
    }

    public static JSONObject getDepartments() {
        return departments;
    }

}
