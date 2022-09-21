package com.linkingwin.education.studyonline.student.api;

import com.linkingwin.education.studyonline.common.base.consts.Constants;
import com.linkingwin.education.studyonline.common.http.HttpCallback;
import com.linkingwin.education.studyonline.common.http.Https;
import com.linkingwin.education.studyonline.common.http.MediaType;
import com.linkingwin.education.studyonline.common.utils.BeanUtils;

import org.xutils.http.HttpMethod;

import java.io.Serializable;

public enum Api {
    /**
     * 登录
     * @params LoginUserVO 登陆用户信息
     */
    LOGIN(HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, Constants.HttpConsts.BASE_URL + "/login"),
    /**
     * 发送短信验证码
     * @params mobile 手机号
     */
    SEND_CAPTCHA(HttpMethod.GET, MediaType.TEXT_PLAIN, Constants.HttpConsts.BASE_URL + "/common/captcha/sendSms") {
        public <T> void request(Serializable mobile, HttpCallback<T> callback) {
            Https.request (getHttpMethod (), getUri () + "/" + mobile, getMediaType (), null, null, callback);
        }
    },
    /**
     * 注册新用户
     * @params RegistUser 注册用户信息
     */
    REGISTER(HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, Constants.HttpConsts.BASE_URL + "/xhlm/regist"),
    /**
     * 重设用户密码
     * @params ResetPassword 重设用户密码信息
     */
    RESETPWD(HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, Constants.HttpConsts.BASE_URL + "/user/resetByMobile"),

    /**
     * 阿里支付
     */
    APP_PAY(HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, Constants.HttpConsts.BASE_URL + "/pay/doPay"),

    /**
     * 获取进入百家云直播间参数
     */
    BAIJIAYUN_CLIENT_ENTER_ROOM(HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, Constants.HttpConsts.BASE_URL + "/video/live/clientEnterRoom"),

    ;

    private HttpMethod httpMethod;
    private MediaType mediaType;
    private String uri;

    Api(HttpMethod httpMethod, MediaType mediaType,  String uri) {
        this.httpMethod = httpMethod;
        this.mediaType = mediaType;
        this.uri = uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getUri() {
        return uri;
    }

    public <T> void request(Serializable params, final HttpCallback<T> callback) {
        try {
            Https.request (httpMethod, uri, mediaType, null, BeanUtils.objectToMap (params), callback);
        } catch (IllegalAccessException e) {
            e.printStackTrace ();
        }
    }

}
