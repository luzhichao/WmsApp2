package com.linkingwin.education.studyonline.student.api;

import com.linkingwin.education.studyonline.common.base.consts.Constants;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
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
        public <T> void request(Serializable mobile, DefaultHttpCallback callback) {
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

    /**
     * 获得老师的列表信息
     */
    GET_TEACHER_LIST(HttpMethod.GET, MediaType.TEXT_PLAIN, Constants.HttpConsts.BASE_URL + "/xhlm/teachbusinessinfo/findRecommendTeach") {
        public <T> void request(Serializable getType, DefaultHttpCallback callback) {
            Https.request (getHttpMethod (), getUri () + "/" + getType, getMediaType (), null, null, callback);
        }
    },

    /**
     * 通过查询条件查询已发布的课程
     */
    FIND_TEACH_BY_FILTER(HttpMethod.POST, MediaType.APPLICATION_FORM_URLENCODED, Constants.HttpConsts.BASE_URL + "/xhlm/teachbusinessinfo/findTeachByStudentSearch"),

    /**
     * 根据老师名查询老师的基础信息
     */
    GET_TEACHINFO_BY_USERNAME(HttpMethod.GET, MediaType.TEXT_PLAIN, Constants.HttpConsts.BASE_URL + "/xhlm/teachbusinessinfo/findTeachInfoByUserName") {
        public <T> void request(Serializable userName, DefaultHttpCallback callback) {
            Https.request (getHttpMethod (), getUri () + "/" + userName, getMediaType (), null, null, callback);
        }
    },
    /**
     * 根据老师名查询老师所有发布的课程信息
     */
    FIND_COURSES_BY_USERNAME_OF_TEACHER(HttpMethod.POST, MediaType.APPLICATION_JSON,  Constants.HttpConsts.BASE_URL + "/xhlm/course/findTeachCourseByUserName"),



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

    public <T> void request(Serializable params, final DefaultHttpCallback callback) {
        try {
            Https.request (httpMethod, uri, mediaType, null, BeanUtils.objectToMap (params), callback);
        } catch (IllegalAccessException e) {
            e.printStackTrace ();
        }
    }

}
