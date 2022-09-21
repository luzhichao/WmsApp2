package com.linkingwin.education.studyonline.common.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class UiUtils {

    public static interface CountDownCallback {
        void onTick(long millisUntilFinished);
        void onFinish();
    }

    public static void countDownTimer(final View actionView, final TextView countDownView,
                                      long millisInFuture, long countDownInterval,
                                      final CountDownCallback callback) {
        //验证码获取成功，1分钟后可再次获取
        new CountDownTimer (millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                actionView.setEnabled(false);
                countDownView.setText(String.format("%ds后重试",millisUntilFinished/1000));
                callback.onTick (millisUntilFinished);
            }
            public void onFinish() {
                actionView.setEnabled(true);
                countDownView.setText("获取验证码");
                callback.onFinish ();
            }
        }.start();
    }
}
