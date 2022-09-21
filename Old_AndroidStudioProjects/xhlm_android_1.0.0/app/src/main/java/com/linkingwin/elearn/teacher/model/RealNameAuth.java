package com.linkingwin.elearn.teacher.model;

import java.util.Map;

import lombok.Data;

@Data
public class RealNameAuth {
	 
	private String realName;//真实姓名
	private String authType;//证件类型
	private String authNumber;//证件号码
	private Map<String, String> authPicture;//证件照片URL
	
}
