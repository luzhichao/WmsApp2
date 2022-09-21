package com.linkingwin.elearn.teacher.model;


import lombok.Data;

@Data
public class SuccessCaseVO {

	//条目ID
	private String id;
	//"简介标题")
	private String title;
	//"详细描述")
	private String describe;

	public SuccessCaseVO(){
		super();
	}
}
