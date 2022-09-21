package com.linkingwin.education.studyonline.common.base.model;

import java.io.Serializable;

import lombok.Data;

public class ResetPassword implements Serializable {

    private String mobile;

    private String password;

    private String code;

    private String passStrength;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassStrength() {
        return passStrength;
    }

    public void setPassStrength(String passStrength) {
        this.passStrength = passStrength;
    }
}
