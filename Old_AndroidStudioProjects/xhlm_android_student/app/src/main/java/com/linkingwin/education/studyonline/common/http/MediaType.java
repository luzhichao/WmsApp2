package com.linkingwin.education.studyonline.common.http;

public enum MediaType {

        APPLICATION_FORM_URLENCODED ("application/x-www-form-urlencoded"),
        APPLICATION_JSON ("application/json"),
        APPLICATION_JSON_BODY ("application/json"),
        MULTIPART_FORM_DATA ("multipart/form-data"),
        TEXT_HTML ("text/html"),
        TEXT_PLAIN("text/plain"),
        TEXT_XML ("text/xml"),
        ;

        private String value;

        MediaType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }