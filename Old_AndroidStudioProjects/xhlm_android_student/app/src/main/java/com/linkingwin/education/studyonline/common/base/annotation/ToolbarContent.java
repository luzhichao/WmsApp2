package com.linkingwin.education.studyonline.common.base.annotation;

import com.linkingwin.education.studyonline.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToolbarContent {
    /**
     * 点击返回的组件ID，当为-1时 不显示返回按钮
     */
    public int resId() default R.mipmap.ic_back;
    public String title();
    public String cusTitle() default "";
    public String subTitle() default "";

}


