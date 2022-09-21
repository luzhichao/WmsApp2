package com.linkingwin.education.studyonline.common.base.model;

import com.alibaba.fastjson.JSONArray;

import org.xutils.db.annotation.Column;

import java.io.Serializable;

import lombok.Data;

/**
 * @描述 登录用户实体类
 * @作者 gsh
 * @时间 2017/4/2 10:39
 */
public class LoginUserVO implements Serializable {

    // token
    private String token;
    // 用户ID 
    private String id;

    // 用户名 
    private String username;

    // 密码 
    private String password;

    // 昵称 
    private String nickName;

    // 手机 
    private String mobile;

    // 邮件 
    private String email;

    // 省市县地址 
    private String address;

    // 街道地址 
    private String street;

    // 0女 1男 2保密 
    private Integer sex;

    // 密码强度 
    private String passStrength;

    // 用户头像 
    private String avatar;

    // 用户类型 0普通用户 1管理员 
    private Integer type;

    // 状态 默认0正常 -1拉黑 
    private Integer statusL;

    // 描述/详情/备注 
    private String description;

    // 所属部门id 
    private String departmentId;

    // 所属部门名称 
    private String departmentTitle;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPassStrength() {
        return passStrength;
    }

    public void setPassStrength(String passStrength) {
        this.passStrength = passStrength;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatusL() {
        return statusL;
    }

    public void setStatusL(Integer statusL) {
        this.statusL = statusL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentTitle() {
        return departmentTitle;
    }

    public void setDepartmentTitle(String departmentTitle) {
        this.departmentTitle = departmentTitle;
    }
}
