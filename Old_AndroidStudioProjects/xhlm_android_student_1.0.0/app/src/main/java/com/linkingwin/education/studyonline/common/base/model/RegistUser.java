package com.linkingwin.education.studyonline.common.base.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class RegistUser implements Serializable {

    private String mobile;

    private String password;

    private String code;

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
}
