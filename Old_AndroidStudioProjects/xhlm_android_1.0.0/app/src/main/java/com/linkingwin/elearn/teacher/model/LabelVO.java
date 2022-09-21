package com.linkingwin.elearn.teacher.model;

import lombok.Data;

@Data
public class LabelVO {
    boolean selected=false;//默认，标签没有被选中
    String tagType="school";//默认标签类型，school学校标签，peculiar特点标签
    String value;
}
