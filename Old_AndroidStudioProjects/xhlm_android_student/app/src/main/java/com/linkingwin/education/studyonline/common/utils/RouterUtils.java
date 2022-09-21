package com.linkingwin.education.studyonline.common.utils;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Map;

public class RouterUtils {

    /**
     * fragment 可返回跳转到 Activity，
     * @param fromFragment from fragment
     * @param toActivityCls to Activity
     * @param params 参数
     */
    public static void fragmentGoActivity(android.support.v4.app.Fragment fromFragment,
                                          Class<? extends FragmentActivity> toActivityCls,
                                          Map<String, Serializable> params) {
        fragmentGoActivity (fromFragment, toActivityCls, params, false);
    }

    /**
     * Activity 可返回跳转到 Activity，
     * @param fromActivity  from Activity
     * @param toActivityCls to Activity
     * @param params 参数
     */
    public static void activityGoActivity(FragmentActivity fromActivity,
                                          Class<? extends FragmentActivity> toActivityCls,
                                          Map<String, Serializable > params) {
        activityGoActivity(fromActivity, toActivityCls, params, false);
    }

    /**
     * fragment 跳转到 Activity，
     * @param fromFragment from fragment
     * @param toActivityCls to Activity
     * @param params 参数
     * @param isFinished 是否能返回
     */
    public static void fragmentGoActivity(android.support.v4.app.Fragment fromFragment,
                                          Class<? extends FragmentActivity> toActivityCls,
                                          Map<String, Serializable> params, boolean isFinished) {
        activityGoActivity(fromFragment.getActivity (), toActivityCls, params, isFinished);
    }

    /**
     * Activity 可返回跳转到 Activity，
     * @param fromActivity  from Activity
     * @param toActivityCls to Activity
     * @param params 参数
     * @param isFinished 是否能返回
     */
    public static void activityGoActivity(FragmentActivity fromActivity,
           Class<? extends FragmentActivity> toActivityCls,
            Map<String, Serializable > params, boolean isFinished) {
        Intent intent = new Intent(fromActivity, toActivityCls);
        if (null != params && !params.isEmpty ()) {
            for (Map.Entry<String, Serializable> entry : params.entrySet ()) {
                intent.putExtra (entry.getKey (), entry.getValue ());
            }
        }
        fromActivity.startActivity(intent);
        if (isFinished) fromActivity.finish ();
    }
}
