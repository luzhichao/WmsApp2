package com.linkingwin.education.studyonline.student.api.response;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 课程VO
 * @author: [LZC]
 * @version: [版本号, 2019年2月22日]
 * @see: [相关类/方法]
 * @since: [产品/模块版本]
 */
public class Course implements Serializable {
	/**
     * 课程发布人
     */
    private String publisher;
    /**
     * 老师名称
     */
    private String teachName;
    /**
	 * 课程名称
	 */
	private String courseName;
	/**
	 * 课程学部
	 */
	private String courseGrade;
	/**
	 * 课程年级
	 */
	private String courseClass;
	/**
	 * 课程类型(1:一对一,2:一对多小班课)
	 */
	private String type;
	/**
	 * 课程学科
	 */
	private String subject;
	/**
	 * 老师性别(0:女、1：男)
	 */
	private String sex;
	/**
	 * 教学经验
	 */
	private String teachYear;
	/**
	 * 教学经验类型（1：5年一下、2：5-10年、3：10年以上）
	 */
	private String teachYearType;
	/**
	 * 输入文字（可能为老师、科目、年级、特点）
	 */
	private String inputText;
	
	/**
	 * 课程课节
	 */
	private Integer lessons;
	/**
	 * 课程时长，单位小时
	 */
	private Double duration;
	/**
	 *  最小购买课次
	 */
	private Integer minBuy;
	/**
	 * 课程状态(0:失效,1:有效)
	 */
	private String isAvaliable = "1";
	/**
	 * 课程标题简介
	 */
	private String title;
	/**
	 * 老师头像
	 */
	private String avatar;
	/**
	 * 销售数量
	 */
	private Integer pageBuycount;
	/**
	 * 课程销售价格（实际支付价格）
	 */
	private BigDecimal currentPrice;

	/**
	 * 排序规则（1：价格最高、2：销售最多）
	 */
	private String orderRule;


	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getTeachName() {
		return teachName;
	}

	public void setTeachName(String teachName) {
		this.teachName = teachName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseGrade() {
		return courseGrade;
	}

	public void setCourseGrade(String courseGrade) {
		this.courseGrade = courseGrade;
	}

	public String getCourseClass() {
		return courseClass;
	}

	public void setCourseClass(String courseClass) {
		this.courseClass = courseClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTeachYear() {
		return teachYear;
	}

	public void setTeachYear(String teachYear) {
		this.teachYear = teachYear;
	}

	public String getTeachYearType() {
		return teachYearType;
	}

	public void setTeachYearType(String teachYearType) {
		this.teachYearType = teachYearType;
	}

	public String getInputText() {
		return inputText;
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	public Integer getLessons() {
		return lessons;
	}

	public void setLessons(Integer lessons) {
		this.lessons = lessons;
	}

	public Double getDuration() {
		return null == duration ? 0d : duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Integer getMinBuy() {
		return null == minBuy ? 0 : minBuy;
	}

	public void setMinBuy(Integer minBuy) {
		this.minBuy = minBuy;
	}

	public String getIsAvaliable() {
		return isAvaliable;
	}

	public void setIsAvaliable(String isAvaliable) {
		this.isAvaliable = isAvaliable;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getPageBuycount() {
		return pageBuycount;
	}

	public void setPageBuycount(Integer pageBuycount) {
		this.pageBuycount = pageBuycount;
	}

	public BigDecimal getCurrentPrice() {
		return null == currentPrice ? BigDecimal.ZERO : currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getOrderRule() {
		return orderRule;
	}

	public void setOrderRule(String orderRule) {
		this.orderRule = orderRule;
	}
}
