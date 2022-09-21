package com.linkingwin.elearn.common.http;

public class Api {
    public final static String BASE_URL = "http://139.196.127.66:8888";
//    public final static String BASE_URL = "http://139.196.127.66:7011";
    public final static String BAIDU_MAP_API = "http://api.map.baidu.com/geocoder?output=json&ak=esNPFDwwsXWtsQfw4NMNmur1";
    public final static String LOGIN_API = BASE_URL + "/linkingwin/login";
    public final static String REGISTER_API = BASE_URL + "/linkingwin/xhlm/regist";
    public final static String GET_VERNUM_API = BASE_URL + "/linkingwin/common/captcha/sendSms/";
    public final static String POST_BASE_INFO = BASE_URL + "/linkingwin/xhlm/teachbusinessinfo/perfectBaseInfo";
    public final static String GET_TEACH_BUSINESS_INFO=BASE_URL+"/linkingwin/xhlm/teachbusinessinfo/findTeachInfoByUserName/";
    public final static String POST_SAVE_TEACH_INFO = BASE_URL + "/linkingwin/xhlm/teachbusinessinfo/saveTeachInfo";

    //老师注册协议
    public final static String PROTOCOL_API = BASE_URL + "/linkingwin/xhlm/xhlmconfiginfo/getByConfigKey/teacher_agreement";
    //基础服务准则
    public final static String GET_BASIC_SERVICE_API = BASE_URL + "/linkingwin/xhlm/xhlmconfiginfo/getByConfigKey/basic_service";
    //学习平台规则
    public final static String GET_PLATFORM_RULES_API = BASE_URL + "/linkingwin/xhlm/xhlmconfiginfo/getByConfigKey/platform_rules";

    //课程管理
    public final static String GET_COURSES_FIND_API =BASE_URL + "/linkingwin/xhlm/course/findCourseByTeachId/";
    public final static String POST_COURSES_SAVE_API =BASE_URL + "/linkingwin/xhlm/course/saveOrUpdateCourse";
    //保存教学时间接口
    public final static String POST_SAVE_TEACH_TIME = BASE_URL + "/linkingwin/xhlm/teachtime/saveTeachTime";
    //获取教学时间接口
    public final static String GET_TEACH_TIME=BASE_URL + "/linkingwin/xhlm/teachtime/getTeachTimeByTeachId/";
    //上传文件
    public final static String POST_UPLOAD_FILE = BASE_URL + "/linkingwin/upload/file";
    public final static String POST_BATCH_UPLOAD_FILE = BASE_URL + "/linkingwin/upload/uploadFiles";
    //老师查询所有对自己的评价信息
    public final static String GET_EVALUATE_BY_TEACHID = BASE_URL + "/linkingwin/xhlm/teachevaluate/findEvaluateByTeachId/";

    //老师查询自己发布课程的订单信息
    public final static String POST_TEACH_MY_ORDER = BASE_URL + "/linkingwin/xhlm/teachbusinessinfo/findTeachMyOrder";
    //老师查询购买自己课程的学生信息
    public final static String POST_TEACH_MY_STUDENT = BASE_URL + "/linkingwin/xhlm/teachbusinessinfo/findTeachMyStudent";
    //工作台显示课程记录
    public final static String POST_COURSE_BY_WORKSPACE = BASE_URL + "/linkingwin/xhlm/course/findCourseByWorkSpace";
    //工作台创建直播间
    public final static String POST_CREATE_ROOM_BY_COURSE = BASE_URL + "/linkingwin/xhlm/course/createRoomByCourse";
    //老师客户端进入直播间
    public final static String POST_CLIENT_TEACH_ENTER_ROOM = BASE_URL + "/linkingwin/xhlm/course/clientTeachEnterRoom";

    public final static Long REQ_TIMEOUT = 60000L;

    //路由跳转，从注册提交页面跳转到注册成功提示页面
    public static String REGISTER_ROUTER = "register";

    //底部导航菜单位置常量
    public final static int BOTTOM_BAR_HOME = 0;
    public final static int BOTTOM_BAR_WORKSPACE = 1;
    public final static int BOTTOM_BAR_MSG = 2;
    public final static int BOTTOM_BAR_MINE = 3;

    //男女的枚举
    public static String SEX_WOMAN="1";
    public static String SEX_MAN="0";
}
