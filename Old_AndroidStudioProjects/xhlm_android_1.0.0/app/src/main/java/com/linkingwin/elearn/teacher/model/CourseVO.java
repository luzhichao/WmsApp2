package com.linkingwin.elearn.teacher.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CourseVO {

    /**
     * id
     */
    private String id;
    /**
     * 课程发布人,userName
     */
    private String publisher;

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
     * 课程人数
     */
    private String peopleNum;

    /**
     * 课程学科
     */
    private String subject;

    /**
     * 课程原价格（只显示）
     */
    private String sourcePrice;
    /**
     * 课程销售价格（实际支付价格）设置为0则可免费观看
     */
    private String currentPrice;
    /**
     * 课程课节
     */
    private String lessons;
    /**
     * 最少购买课程
     */
    private String minBuy;
    /**
     * 课程时长
     */
    private String duration;

    /**
     * 课程状态(0:失效,1:有效)
     */
    private String isAvaliable = "1";
    /**
     * 课程标题简介
     */
    private String title;
    /**
     * 课程详情
     */
    private String context;
    /**
     * 课程图片路径
     */
    private String logo;
    /**
     * 销售数量
     */
    private String pageBuycount;
    /**
     * 浏览数量
     */
    private String pageViewcount;
    /**
     * 老师预计开始授课时间
     */
    private String predictBeginTime;
    /**
     * 删除标记，1=删除，0=未删除
     */
    private String delFlag="0";

    public CourseVO(String id,String courseName, String courseGrade, String courseClass,
                    String type, String peopleNum, String subject, String sourcePrice,
                    String lessons,String minBuy,String duration) {
        this.id=id;
        this.courseName=courseName;
        this.courseGrade=courseGrade;
        this.courseClass =courseClass;
        this.type=type;
        this.peopleNum=peopleNum;
        this.subject=subject;
        this.sourcePrice=sourcePrice;
        this.lessons=lessons;
        this.minBuy=minBuy;
        this.duration=duration;
    }

    public CourseVO(){
        super();
    }

}
