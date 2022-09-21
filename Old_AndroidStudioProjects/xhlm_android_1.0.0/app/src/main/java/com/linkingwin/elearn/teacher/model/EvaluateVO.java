package com.linkingwin.elearn.teacher.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 评价页面展示VO
 * @author: [LZC]
 * @version: [版本号, 2019年1月24日]
 * @see: [相关类/方法]
 * @since: [产品/模块版本]
 */
@Data
public class EvaluateVO implements Serializable {

	private static final long serialVersionUID = 8620197982498560788L;
	/**
	 * 评价ID
	 */
	private String id;
	/**
	 * 老师用户ID
	 */
	private String teachId;
	/**
	 * 学生用户ID
	 */
	private String studentId;
 	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 学生年级
	 */
	private String studentClass;
	/**
	 * 课程课节
	 */
	private Integer lessons;
	/**
	 * 课程时长
	 */
	private Integer duration;
	/**
	 * 评价内容
	 */
	private String evaluateContent;
	/**
	 * 评价时间
	 */
	private String evaluateTime;
	/**
	 * 评价等级
	 */
	private String evaluateLevel;
	/**
	 * 点赞数量
	 */
	private Integer praiseCount = 0;
	/**
	 * 回复数量
	 */
	private Integer replyCount = 0;
	

}
