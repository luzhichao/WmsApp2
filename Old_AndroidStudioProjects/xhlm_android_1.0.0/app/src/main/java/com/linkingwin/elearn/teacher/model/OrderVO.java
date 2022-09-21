package com.linkingwin.elearn.teacher.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 订单实体
 */
@Data
public class OrderVO implements Serializable {

    /**
     * 订单ID
     */
    private String id;
    /**
     * 课程图片路径
     */
    private String logo;
    /**
     * 课程ID
     */
    private String courseId;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 购买的课程数
     */
    private Integer buyCourses;
    /**
     * 课程销售价格
     */
    private String currentPrice;
    /**
     * 订单状态(0:待支付,1:已付款,2:已取消,3:已完成)
     */
    private String status;
    /**
     * 下单时间
     */
    private String orderTime;
    /**
     * 上课开始时间
     */
    private String schoolStartTime;
    /**
     * 上课结束时间
     */
    private String schoolEndTime;
    /**
     * 课节状态
     */
    private String courseStatus;
    /**
     * 直播房间ID
     */
    private String roomId;
    /**
     * 课节号
     */
    private String courseNum;

}
