package com.linkingwin.elearn.teacher.model;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class HonorVO implements Serializable {

	//荣誉简介
	private String honorIntro;
	//荣誉图片
	private ArrayList<String> pictureUrls;

}
