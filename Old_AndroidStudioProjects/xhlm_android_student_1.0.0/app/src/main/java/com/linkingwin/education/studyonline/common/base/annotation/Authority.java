package com.linkingwin.education.studyonline.common.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authority {
    public enum Permission {
        LOGIN_USER
    }
    public Permission[] value();
}
