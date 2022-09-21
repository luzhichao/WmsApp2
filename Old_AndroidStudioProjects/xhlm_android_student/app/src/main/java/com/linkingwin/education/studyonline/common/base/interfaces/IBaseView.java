package com.linkingwin.education.studyonline.common.base.interfaces;

/**
 * @描述 基础view层
 * @作者 gsh
 * @时间 2017/5/26 16:05
 */

public interface IBaseView {
    /**
     * 显示提示信息
     * @param message
     */
    void showToast(String message, int time);

    /**
     * 显示进度条
     * @param message
     */
    void showLoading(String message);

    /**
     * 显示进度条
     * @param title
     * @param message
     */
    void showLoading(String title, String message);

    /**
     * 隐藏进度条
     */
    void hideLoading();


}
