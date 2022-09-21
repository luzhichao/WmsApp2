package com.linkingwin.education.studyonline.common.base.interfaces;

import com.linkingwin.education.studyonline.R;

public interface Callback {

    public interface OnSuccess {
        public void run(Object result);
    }

    public interface OnFail {
        public void run(String errorMessage, Exception re);
    }
}
