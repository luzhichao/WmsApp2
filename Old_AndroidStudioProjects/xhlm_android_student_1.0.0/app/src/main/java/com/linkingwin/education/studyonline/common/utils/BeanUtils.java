package com.linkingwin.education.studyonline.common.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.linkingwin.education.studyonline.common.base.annotation.ToolbarContent;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @描述
 * @作者 gsh
 * @时间 2017/5/19 10:01
 */

public class BeanUtils {

    public static void null2Default(Object bean) {
        Field[] field = bean.getClass().getDeclaredFields();
        for (int i = 0; i < field.length; i++) {     //遍历所有属性
            String name = field[i].getName(); // 获取属性的名字
            name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
            String type = field[i].getGenericType().toString(); // 获取属性的类型
            if (type.equals("class java.lang.Integer")) {
                try {
                    Method m = bean.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(bean);
                    if (value == null) {
                        m = bean.getClass().getMethod("set" + name, Integer.class);
                        m.invoke(bean, 0);
                    }
                } catch (Exception e) {

                }
            } else if (type.equals("class java.lang.Double")) {
                try {
                    Method m = bean.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(bean);
                    if (value == null) {
                        m = bean.getClass().getMethod("set" + name, Double.class);
                        m.invoke(bean, 0);
                    }
                } catch (Exception e) {

                }
            } else if (type.equals("class java.lang.Long")) {
                try {
                    Method m = bean.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(bean);
                    if (value == null) {
                        m = bean.getClass().getMethod("set" + name, Long.class);
                        m.invoke(bean, 0);
                    }
                } catch (Exception e) {

                }
            } else if (type.equals("class java.lang.String")) {
                try {
                    Method m = bean.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(bean);
                    if (value == null) {
                        m = bean.getClass().getMethod("set" + name, String.class);
                        m.invoke(bean, "");
                    }
                } catch (Exception e) {

                }
            } else if (type.equals("class java.math.BigDecimal")) {
                try {
                    Method m = bean.getClass().getMethod("get" + name);
                    Integer value = (Integer) m.invoke(bean);
                    if (value == null) {
                        m = bean.getClass().getMethod("set" + name, BigDecimal.class);
                        m.invoke(bean, new BigDecimal(0));
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Serializable obj) throws IllegalAccessException {
        if (null == obj) {
            return null;
        }
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }

    public static void reflectGetMethod(final Object bo, final Map<String, Object> conMap) {
        final Class cls = bo.getClass();
        final Method[] methods = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            final String methodName = method.getName();
            if (methodName.startsWith("get")) {
                // 属性名
                final StringBuilder filedname = new StringBuilder();
                final String name = methodName.substring(methodName.indexOf("get") + "get".length());
                filedname.append(name.substring(0, 1).toLowerCase()).append(name.substring(1));
                try {
                    if (method.invoke(bo) == null) {
                        continue;
                    }
                    conMap.put(filedname.toString(), String.valueOf(method.invoke(bo)));
                } catch (Exception e) {
                    Log.e("反射传递参数错误", e.getMessage());
                }

            }
        }
    }

    /**
     * map 转成 JavaBean
     *
     * @param map
     * @param javaBeanClass
     * @return
     * @throws Exception
     */
    public static <T> T map2JavaBean(Map<String, Object> map, Class<T> javaBeanClass) {
        return (T) JSONObject.parseObject (JSON.toJSONString(map), javaBeanClass);
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationClass) {
        try {
            return clazz.getAnnotation (annotationClass);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

}
