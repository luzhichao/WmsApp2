package com.linkingwin.elearn.teacher.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class ExperienceVO implements Serializable {

	//主键
	private String id;
	//"简述标题"
	private String title;
	//"开始时间"
	private String beginDate;
	//"结束时间"
	private String endDate;
	//"描述"
	private String describe;

	public ExperienceVO(String id,String title,String beginDate,String endDate,String describe){
		this.id=id;
		this.title=title;
		this.beginDate=beginDate;
		this.endDate=endDate;
		this.describe=describe;
	}
	public ExperienceVO(){

	}
}
