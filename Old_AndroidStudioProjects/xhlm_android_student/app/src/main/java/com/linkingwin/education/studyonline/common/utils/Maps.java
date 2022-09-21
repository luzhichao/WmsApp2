package com.linkingwin.education.studyonline.common.utils;

import java.util.HashMap;
import java.util.Map;

public class Maps {

    public static class KeyValue<K, V> {
        K k;
        V v;
        public KeyValue(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    public static <K, V> KeyValue kv(K k, V v) {
        return new KeyValue (k, v);
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> HashMap<K, V> newHashMap(KeyValue<K, V> ... keyValues) {
        final HashMap<K, V> map =  new HashMap<K, V>();
        for (KeyValue<K, V> kv : keyValues) {
            map.put (kv.k, kv.v);
        }
        return map;
    }

}
