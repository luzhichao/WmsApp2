package com.linkingwin.education.studyonline.student.vo;

import com.linkingwin.education.studyonline.common.base.model.CheckAbleBean;

public class Grade implements CheckAbleBean, ITextBean {

    private String value;
    private String gradeName;
    private boolean checked;

    public Grade() {
    }

    public Grade(String gradeName) {
        this.value = gradeName;
        this.gradeName = gradeName;
    }

    public Grade(String value, String gradeName) {
        this.value = value;
        this.gradeName = gradeName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String getCheckText() {
        return gradeName;
    }

    @Override
    public String lableName() {
        return gradeName;
    }

    @Override
    public boolean isSelected() {
        return checked;
    }

    public String getValue() {
        return value;
    }
}
