package com.linkingwin.elearn.teacher.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 学生VO
 * @version: [版本号, 2019年2月25日]
 * @see: [相关类/方法]
 * @since: [产品/模块版本]
 */
@Data
public class StudentVO implements Serializable {

	private static final long serialVersionUID = -2227042008007947254L;
	
	private String studentId;//学生ID
	private String nickName;//昵称
	private String avatar;//头像
	private String courseName;//购买的课程名称
	private String buyCourses;//购买的课程数量
	private String status;//订单状态
	private String teachId;//老师ID

}
