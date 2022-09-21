package com.linkingwin.elearn.teacher.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import cn.finalteam.toolsfinal.StringUtils;
import lombok.Data;

@Data
public class TeachBusinessInfo {

    private String userId;  //用户ID
    private String username;//老师账号
    private String realName;//老师真实名称
    private String status; //正常、注销
    private String nickName;//老师昵称
    private String sex;//老师性别,男:0，女:1,没有选择:-1
    private String avatar;//老师头像的URL
    private String address;//老师所在的城市
    private String teachGrade;//老师教授学部
    private String teachSubject;//老师教授学科
    private String teachYear;//老师教学教龄
    private String gradSchool;///毕业院校
    private String jobLevel;//职称
    private String honor;//奖励荣誉
    private String experience;//教学经历
    private String successCase;//成功案例
    private String peculiar;//教学特点
    private String photo;//照片url
    private String peculiarTag;//特点标签
    private String schoolTag;//学校标签
    private String personalVideo;//个人视频url
    private String realNameAuth;//实名认证
    private String otherAuth;//我的其他认证
    private String lat;//纬度
    private String lng;//经度
    private String basicService="0"; //是否确认基本服务协议内容，确认1，未确认0
    private String platformRuls="0";//是否确认平台服务准则，确认1，未确认0
    private String watchVideo="0";//是否观看完视频，确认1，未确认0
    private String applyTeachStatus = "0";//申请开课状态，0未申请、1审核中、2通过

    /**
     * 拼装科目和部门
     * @return
     */
    public String getSubDep() {
        JSONObject subjects=null;
        JSONObject departments=null;
//        String strSub = "", strDep = "";
        if(StringUtils.isEmpty( teachSubject )){
            subjects=new JSONObject(  );
        }else{
            subjects=JSON.parseObject( teachSubject );
        }
//        for (Map.Entry<String, Object> entry : subjects.entrySet()) {
//            strSub = strSub + (strSub == "" ? "" : "|") + entry.getValue();
//        }
//
        if(StringUtils.isEmpty( teachGrade )){
            departments=new JSONObject(  );
        }else {
            departments=JSON.parseObject( teachGrade );
        }
//
//        for (Map.Entry<String, Object> entry : departments.entrySet()) {
//            strDep = strDep + (strDep == "" ? "" : "|") + entry.getValue();
//        }

        return getSubDep(subjects,departments);
    }

    /**
     * 拼装科目和部门
     * @return
     */
    public String getSubDep(JSONObject subjects,JSONObject departments) {
        String strSub = "", strDep = "";
        if(StringUtils.isEmpty( subjects.toJSONString() )){
            subjects=JSONObject.parseObject( "{}");
        }
        if(StringUtils.isEmpty( departments.toJSONString() )){
            departments=JSONObject.parseObject( "{}");
        }
        for (Map.Entry<String, Object> entry : subjects.entrySet()) {
            strSub = strSub + (strSub == "" ? "" : "|") + entry.getValue();
        }
        for (Map.Entry<String, Object> entry : departments.entrySet()) {
            strDep = strDep + (strDep == "" ? "" : "|") + entry.getValue();
        }
        return !StringUtils.isEmpty( strDep ) ? ( !StringUtils.isEmpty(strSub) ? strDep+"|"+strSub : strSub) : "";
    }
}
