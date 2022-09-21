package com.linkingwin.education.studyonline.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.education.studyonline.common.base.model.LoginUserVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @描述 SharedPreferences
 * @作者 gsh
 * @时间 2017/4/2 15:16
 */

public class SharedPreUtil {

    public final static String SP_NAME = "droplets";

    private static SharedPreUtil s_SharedPreUtil;

    private SharedPreferences msp;


    public SharedPreferences getSharedPref() {
        return msp;
    }

    public static synchronized SharedPreUtil getInstance() {
        return s_SharedPreUtil;
    }

    /**
     * 获取唯一的instance
     * <p>
     * // 初始化，一般在应用启动之后就要初始化
     */
    public static synchronized void initSharedPreference(Context context) {
        if (s_SharedPreUtil == null) {
            s_SharedPreUtil = new SharedPreUtil(context);
        }
    }


    @SuppressLint("WrongConstant")
    public SharedPreUtil(Context context) {
        msp = context.getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE | Context.MODE_APPEND);
    }


    public synchronized void putUser(LoginUserVO user) {
        SharedPreferences.Editor editor = msp.edit();
        String str = "";
        try {
            str = SerializableUtil.obj2Str(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.putString("user", str);
        editor.commit();
    }

    public synchronized LoginUserVO getUser() {
        //获取序列化的数据
        String str = msp.getString("user", "");
        try {
            Object obj = SerializableUtil.str2Obj(str);
            if (obj != null) {
                return (LoginUserVO) obj;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LoginUserVO();
    }

    public synchronized void deleteUser() {
        SharedPreferences.Editor editor = msp.edit();
        editor.remove("user");
        editor.commit();
    }


    public synchronized void putDicList(String systemId, List sysDic) {
        SharedPreferences.Editor editor = msp.edit();
        String str = "";
        try {
            str = SerializableUtil.obj2Str(sysDic);
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.putString(systemId+"_dicList", str);
        editor.commit();
    }

    public synchronized List getDicList(String systemId) {
        //获取序列化的数据
        String str = msp.getString(systemId+"_dicList", "");

        try {
            Object obj = SerializableUtil.str2Obj(str);
            if (obj != null) {
                return (List) obj;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    public synchronized void deleteDicList(String systemId) {
        SharedPreferences.Editor editor = msp.edit();
        editor.remove(systemId+"_dicList");
        editor.commit();
    }

    public synchronized void putServer(JSONObject jsonObject) {
        SharedPreferences.Editor editor = msp.edit();
        String str = "";
        try {
            str = SerializableUtil.obj2Str(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.putString("server", str);
        editor.commit();
    }

    public synchronized JSONObject getServer() {
        //获取序列化的数据
        String str = msp.getString("server", "");

        try {
            Object obj = SerializableUtil.str2Obj(str);
            if (obj != null) {
                return (JSONObject) obj;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void deleteServer() {
        SharedPreferences.Editor editor = msp.edit();
        editor.remove("server");
        editor.commit();
    }

    /**
     * 是否是【体验版】登录
     *
     * @return
     */
    public synchronized boolean isTest() {
        boolean isTest = msp.getBoolean("isTest", false);
        return isTest;
    }

    /**
     * 设置是否为【体验版】登录
     */
    public synchronized void setTest(boolean isTest) {
        SharedPreferences.Editor editor = msp.edit();
        editor.putBoolean("isTest", isTest);
        editor.commit();
    }

    public synchronized void putUserSys(String userCode, List syss) {
        SharedPreferences.Editor editor = msp.edit();
        String str = "";
        try {
            str = SerializableUtil.obj2Str(syss);
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.putString(userCode+"_syss", str);
        editor.commit();
    }

    public synchronized List getUserSys(String userCode) {
        //获取序列化的数据
        String str = msp.getString(userCode+"_syss", "");

        try {
            Object obj = SerializableUtil.str2Obj(str);
            if (obj != null) {
                return (List) obj;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }


}
