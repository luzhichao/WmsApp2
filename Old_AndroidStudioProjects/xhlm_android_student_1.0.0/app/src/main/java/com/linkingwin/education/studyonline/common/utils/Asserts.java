package com.linkingwin.education.studyonline.common.utils;

import org.apache.commons.lang3.StringUtils;

public final class Asserts {

    /**
     * 断言 text 不为空，如果为空则弹出提示信息
     * @param text 内容
     * @param message 提示信息
     * @return true 不为空 false 为空
     */
    public static boolean notBlank(String text, String message) {
       boolean res = StringUtils.isEmpty (text);
       if (res) ToastUtils.show (message);
       return !res;
    }

    /**
     * 断言 text 不匹配正则表达式，如果匹配则弹出提示信息
     * @param text 内容
     * @param regex 匹配正则表达式
     * @param message 提示信息
     * @return true 不匹配 false 为匹配
     */
    public static boolean notMatche(String text, RegexUtils.Regex regex, String message) {
        boolean res = RegexUtils.matches (regex, text);
        if (res) ToastUtils.show (message);
        return !res;
    }

    /**
     * 断言 text 不匹配正则表达式，如果匹配则弹出提示信息
     * @param text 内容
     * @param regex 匹配正则表达式 字符串
     * @param message 提示信息
     * @return true 不匹配 false 为匹配
     */
    public static boolean notMatche(String text, String regex, String message) {
        boolean res = RegexUtils.matches (regex, text);
        if (res) ToastUtils.show (message);
        return !res;
    }


    /**
     * 断言 text 匹配正则表达式，如果不匹配则弹出提示信息
     * @param text 内容
     * @param regex 匹配正则表达式
     * @param message 提示信息
     * @return true 匹配 false 为不匹配
     */
    public static boolean matche(String text, RegexUtils.Regex regex, String message) {
        boolean res = RegexUtils.matches (regex, text);
        if (!res) ToastUtils.show (message);
        return res;
    }


    /**
     * 断言 text 匹配正则表达式，如果不匹配则弹出提示信息
     * @param text 内容
     * @param regex 匹配正则表达式 字符串
     * @param message 提示信息
     * @return true 匹配 false 为不匹配
     */
    public static boolean matche(String text, String regex, String message) {
        boolean res = RegexUtils.matches (regex, text);
        if (!res) ToastUtils.show (message);
        return res;
    }

    public static boolean eq(String text, String other_text, String message) {
        boolean res =  text.equals (other_text);
        if (!res) ToastUtils.show (message);
        return res;
    }
}
