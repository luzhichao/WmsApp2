package com.linkingwin.education.studyonline.common.http;

import com.alibaba.fastjson.JSONObject;

import org.xutils.http.annotation.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
* @描述 接口请求服务器返回对象
* @作者 gsh
* @时间 2017/4/2 10:12
*/
@HttpResponse(parser = JsonResponseParser.class)
public class JsonResponse extends HashMap<String, Object> {

}
