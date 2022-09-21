package com.linkingwin.education.studyonline.common.utils;

import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.annotation.Authority;

import org.apache.commons.lang3.StringUtils;

public class AuthorityUtils {


    /**
     * 判断权限
     * @param application
     * @param authority
     * @return
     */
    public static boolean auth(MyApplication application, Authority authority) {
        if (null != authority) {
            for (Authority.Permission permission : authority.value ()) {
                // 暂不封装
                if (permission.equals (Authority.Permission.LOGIN_USER)) {
                    // 判断当前用户是否需要登陆，如果token为空，则未登陆，否则为登陆
                    return !StringUtils.isEmpty (application.getUser ().getToken ());
                }
            }
        }
        return true;
    }
}
