package com.linkingwin.elearn.common.constant;

public interface XhlmConstant {

    String UPLOAD_MERCHANTID = "ROOT";//上传的商家ID
    String UPLOAD_CHANNEL = "LOCAL";//上传通道

    String HTTP_ACCESS_TOKEN = "accessToken";//登陆token

    //*************选择图片请求code，每个功能唯一*********************//
    int REAL_AUTH_UPLOAD_REQUEST_CODE = 111;//实名认证
    int OTHER_AUTH_UPLOAD_REQUEST_CODE = 112;//我的认证
    int JOB_LEVEL_UPLOAD_REQUEST_CODE = 113;//职称
    int HONOR_UPLOAD_REQUEST_CODE = 124;//荣誉奖励新增编辑
    int HONOR_ADAPTER_UPLOAD_REQUEST_CODE = 125;//荣誉奖励列表
    int MY_AVATAR_UPLOAD_REQUEST_CODE = 126;//我的头像
    int MY_PICTURE_UPLOAD_REQUEST_CODE = 127;//个人风采-照片

    int VIDEO_UPLOAD_REQUEST_CODE = 211;//选择视频
}
