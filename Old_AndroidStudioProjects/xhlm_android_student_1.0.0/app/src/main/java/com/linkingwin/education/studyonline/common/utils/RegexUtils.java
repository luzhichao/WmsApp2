package com.linkingwin.education.studyonline.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {

    public static enum Regex {
        /**
         * 密码强度校验
         * 密码的强度必须是包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间。
         */
        COMPLEXITY_OF_PASSWORD("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$"),
        /**
         * 字符串仅能是中文
         */
        CHINESE_WORDS("^[\\u4e00-\\u9fa5]{0,}$"),
        /**
         * 由数字、26个英文字母或下划线组成的字符串
         */
        LETTER("^\\w+$"),

        /**
         * E-mail地址合规性
         */
        EMAIL("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?"),

        /**
         * 身份证号码15位的正则校验
         */
        IDCARD_15("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"),

        /**
         * 身份证号码18位的正则校验
         */
        IDCARD_18("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$"),

        /**
         * “yyyy-mm-dd“ 格式的日期校验，已考虑平闰年
         */
        SHORT_DATE("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"),

        /**
         * 金额校验，精确到2位小数
         */
        MONEY("^[0-9]+(.[0-9]{2})?$"),

        /**
         * 校验手机号  国内 13、15、18开头的手机号正则表达式
         */
        MOBILE("^1[3|5|7|8][0-9]\\d{8}$"),

        /**
         * IP4 正则语句
         */
        IP4("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b"),

        /**
         * IP6 正则语句
         */
        IP6("(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))"),
        ;

        private String regex;

        Regex(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            return regex;
        }
    }

    public static boolean matches(Regex regex, String content) {
        return matches(regex.getRegex (), content);
    }

    public static boolean  matches(String regex, String content) {
        return Pattern.matches (regex, content);
    }

    // 判断一个字符串是否含有数字
    public static boolean hasMatch(String content, String regex) {
        boolean flag = false;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }


    public static enum TextStregth {
        weak("弱"),
        normal("中"),
        strong("强")
        ;
        private String name;

        private TextStregth(String name) {
            this.name = name;
        }

        public final String getName() {
            return name;
        }
        public static TextStregth getTextStregth(int grade) {
            if (grade < 2) {
                return TextStregth.weak;
            } else if (grade < 5) {
                return TextStregth.normal;
            } else {
                return TextStregth.strong;
            }
        }
    }

    public static TextStregth checkStregthValue(String text) {
        int grade = 0;
        if (hasMatch (text, ".*\\d+.*")) {
            grade++; //数字
        }
        if (hasMatch (text, ".*[a-z]+.*")) {
            grade++; //小写
        }
        if (hasMatch (text, ".*[A-Z]+.*")) {
            grade++; //大写
        }
        if (hasMatch (text, ".*\\W+.*")) {
            grade++; //特殊字符
        }
        if (text.length () >= 10) {
            grade++;
        }
        return TextStregth.getTextStregth(grade);
    }
}
