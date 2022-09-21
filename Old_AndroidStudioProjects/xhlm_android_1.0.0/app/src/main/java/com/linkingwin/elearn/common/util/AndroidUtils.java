package com.linkingwin.elearn.common.util;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.common.model.Params;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public class AndroidUtils {


    public static void go (Context context, Class<?> cls, boolean finish, Params params) {
        Intent intent = new Intent( context, cls);
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                intent.putExtra( entry.getKey(), entry.getValue());
            }
        }
        context.startActivity( intent );
    }

    public static void go (Context context, Class<?> cls, boolean finish, JSONObject params) {
        Intent intent = new Intent( context, cls);
        if (null != params && !params.isEmpty()) {
            Iterator<String> keySets = params.keySet().iterator();
            String key;
            while (keySets.hasNext()) {
                key = keySets.next();
                intent.putExtra( key, params.get( key ).toString());
            }
        }
        context.startActivity( intent );
    }

}
