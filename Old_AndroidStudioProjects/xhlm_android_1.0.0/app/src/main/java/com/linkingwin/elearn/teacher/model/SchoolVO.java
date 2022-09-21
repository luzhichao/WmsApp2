package com.linkingwin.elearn.teacher.model;

import lombok.Data;

@Data
public class SchoolVO {
    private String id;  //主键
    private String schoolName;//学校名字
    private String beginDate;//开始日期
    private String endDate;//结束日期
    private String education;//学历
    private String major;//专业
    private String isOpen="true";//默认等于true，是否公开,true公开，false不公开

    public SchoolVO(){
        super();
    }

    public SchoolVO(String id, String schoolName, String beginDate, String endDate, String education, String major, String isOpen) {
        this.id = id;
        this.schoolName = schoolName;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.education = education;
        this.major = major;
        this.isOpen = isOpen;
    }

    /**
     * 这样直接转化啊，王朝禹你个奶奶的
     * SchoolVO mGraduateSchoolModel = JSONObject.parseObject( "",SchoolVO.class )
     * String mGraduateSchoolModel = JSONObject.toJSONString( mGraduateSchoolModel );
     */
}
