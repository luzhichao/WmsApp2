package com.linkingwin.elearn.common.model;

import java.io.Serializable;
import java.util.HashMap;

public class Params extends HashMap<String, Serializable> {

    private Params() {}

    public static Params newInstance() {
        return new Params();
    }

    public Params add(String key, Serializable val) {
        put(key, val);
        return this;
    }


}
