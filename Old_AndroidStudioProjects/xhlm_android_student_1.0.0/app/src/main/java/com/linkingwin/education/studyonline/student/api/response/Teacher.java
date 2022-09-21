package com.linkingwin.education.studyonline.student.api.response;

import java.util.List;

public class Teacher {

    private String id;
    /**
     * 头像
     */
    private String avatar;

    private String userId;

    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 状态:0正常1注销
     */
    private String status;
    /**
     * 授课学部
     */
    private String teachGrade;
    /**
     * 授课学科
     */
    private String teachSubject;
    /**
     * 教龄
     */
    private Integer teachYear;
    /**
     * 毕业院校
     */
    private String gradSchool;
    /**
     * 职称
     */
    private String jobLevel;
    /**
     * 奖励荣誉
     */
    private String honor;
    /**
     * 教学经历
     */
    private String experience;
    /**
     * 成功案例
     */
    private String successCase;
    /**
     * 教学特点
     */
    private String peculiar;
    /**
     * 照片url
     */
    private String photo;
    /**
     * 特点标签
     */
    private String peculiarTag;
    /**
     * 学校标签
     */
    private String schoolTag;
    /**
     * 个人视频url
     */
    private String personalVideo;
    /**
     * 实名认证
     */
    private String realNameAuth;
    /**
     * 我的其他认证
     */
    private String otherAuth;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 经度
     */
    private String lng;
    /**
     * 申请开课状态
     */
    private String applyTeachStatus;
    /**
     * 是否确认基本服务协议内容
     */
    private String basicService;
    /**
     * 是否确认平台服务准则
     */
    private String platformRuls;
    /*
     * 是否观看完视频
     */
    private String watchVideo;

    /**
     * 发布课程最低价
     */
    private String minPrice;

    /**
     * 已授课节
     */
    private String lessonsCount;

    private List<Course> courses;//发布的课程


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeachGrade() {
        return teachGrade;
    }

    public void setTeachGrade(String teachGrade) {
        this.teachGrade = teachGrade;
    }

    public String getTeachSubject() {
        return teachSubject;
    }

    public void setTeachSubject(String teachSubject) {
        this.teachSubject = teachSubject;
    }

    public Integer getTeachYear() {
        return null == teachYear ? 0 : teachYear;
    }

    public void setTeachYear(Integer teachYear) {
        this.teachYear = teachYear;
    }

    public String getGradSchool() {
        return gradSchool;
    }

    public void setGradSchool(String gradSchool) {
        this.gradSchool = gradSchool;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getHonor() {
        return honor;
    }

    public void setHonor(String honor) {
        this.honor = honor;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSuccessCase() {
        return successCase;
    }

    public void setSuccessCase(String successCase) {
        this.successCase = successCase;
    }

    public String getPeculiar() {
        return peculiar;
    }

    public void setPeculiar(String peculiar) {
        this.peculiar = peculiar;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPeculiarTag() {
        return peculiarTag;
    }

    public void setPeculiarTag(String peculiarTag) {
        this.peculiarTag = peculiarTag;
    }

    public String getSchoolTag() {
        return schoolTag;
    }

    public void setSchoolTag(String schoolTag) {
        this.schoolTag = schoolTag;
    }

    public String getPersonalVideo() {
        return personalVideo;
    }

    public void setPersonalVideo(String personalVideo) {
        this.personalVideo = personalVideo;
    }

    public String getRealNameAuth() {
        return realNameAuth;
    }

    public void setRealNameAuth(String realNameAuth) {
        this.realNameAuth = realNameAuth;
    }

    public String getOtherAuth() {
        return otherAuth;
    }

    public void setOtherAuth(String otherAuth) {
        this.otherAuth = otherAuth;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getApplyTeachStatus() {
        return applyTeachStatus;
    }

    public void setApplyTeachStatus(String applyTeachStatus) {
        this.applyTeachStatus = applyTeachStatus;
    }

    public String getBasicService() {
        return basicService;
    }

    public void setBasicService(String basicService) {
        this.basicService = basicService;
    }

    public String getPlatformRuls() {
        return platformRuls;
    }

    public void setPlatformRuls(String platformRuls) {
        this.platformRuls = platformRuls;
    }

    public String getWatchVideo() {
        return watchVideo;
    }

    public void setWatchVideo(String watchVideo) {
        this.watchVideo = watchVideo;
    }

    public String getMinPrice() {
        return null == minPrice ? "0" : minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getLessonsCount() {
        return null == lessonsCount ? "0" : lessonsCount;
    }

    public void setLessonsCount(String lessonsCount) {
        this.lessonsCount = lessonsCount;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
