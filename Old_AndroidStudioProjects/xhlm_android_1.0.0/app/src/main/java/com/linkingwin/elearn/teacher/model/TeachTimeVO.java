package com.linkingwin.elearn.teacher.model;

import com.linkingwin.elearn.common.util.Tools;

import lombok.Data;

@Data
public class TeachTimeVO {
    /**
     * 老师用户ID
     */
    private String teachId;
    /**
     * guID
     */
    private String id= Tools.createID();
    /**
     * 类型，type=0：展示图标不展示文本，type=1：展示文本不展示图标
     */
    private String type="0";
    /**
     * 周几授课(1~7)
     */
    private String teachWeek;
    /**
     * 授课时间（１：上午06:00~12:00，２：下午12:00~18:00，３：晚上18:00~24:00）
     */
    private String teachTime;

    /**
     * 是否选中，0没有选中，1选中
     */
    private String isConfirm="1";

    public TeachTimeVO(String teachId,String id,String type,String teachWeek,String teachTime,String isConfirm){
        this.teachId=teachId;
        this.id=id;
        this.type=type;
        this.teachWeek=teachWeek;
        this.teachTime=teachTime;
        this.isConfirm=isConfirm;
    }
    public TeachTimeVO(){super();}
}
