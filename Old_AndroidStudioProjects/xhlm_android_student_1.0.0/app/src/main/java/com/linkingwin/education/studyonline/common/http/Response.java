package com.linkingwin.education.studyonline.common.http;

import com.alibaba.fastjson.util.TypeUtils;
import static com.alibaba.fastjson.util.TypeUtils.castToBigDecimal;
import static com.alibaba.fastjson.util.TypeUtils.castToBigInteger;
import static com.alibaba.fastjson.util.TypeUtils.castToBoolean;
import static com.alibaba.fastjson.util.TypeUtils.castToByte;
import static com.alibaba.fastjson.util.TypeUtils.castToBytes;
import static com.alibaba.fastjson.util.TypeUtils.castToDate;
import static com.alibaba.fastjson.util.TypeUtils.castToDouble;
import static com.alibaba.fastjson.util.TypeUtils.castToFloat;
import static com.alibaba.fastjson.util.TypeUtils.castToInt;
import static com.alibaba.fastjson.util.TypeUtils.castToLong;
import static com.alibaba.fastjson.util.TypeUtils.castToShort;
import static com.alibaba.fastjson.util.TypeUtils.castToSqlDate;
import static com.alibaba.fastjson.util.TypeUtils.castToTimestamp;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

public class Response extends HashMap<String, Object> {

    /**
     * 请求执行是否成功
     */    public boolean isSuccess() {
        return (boolean) this.get("success");
    }
    /**
     * 得到返回的异常信息
     */
    public String getErrorMsg() {
        String msg = (String) this.get ("msg");
        return StringUtils.isEmpty (msg) ?  (String) this.get ("message") : msg;
    }

    /**
     * 获得返回代码
     */
    public String getCode() {
        return (String) this.get("code");
    }

    /**
     * 获得返回的内容
     */
    public <T> T getResult() {
        return (T) (null == get("result") ? get("content") : get("result"));
    }

    public <T> T getObject(String propertyName, Class<T> clazz) {
        return TypeUtils.castToJavaBean(get(propertyName), clazz);
    }

    public Boolean getBoolean(String propertyName) {
        return TypeUtils.castToBoolean(get(propertyName));
    }

    public byte[] getBytes(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return null;
        }
        return castToBytes(value);
    }

    public boolean getBooleanValue(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return false;
        }
        return castToBoolean(value).booleanValue();
    }

    public Byte getByte(String propertyName) {
        return castToByte(get(propertyName));
    }

    public byte getByteValue(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return 0;
        }
        return castToByte(value).byteValue();
    }

    public Short getShort(String propertyName) {
        return castToShort(get(propertyName));
    }

    public short getShortValue(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return 0;
        }
        return castToShort(value).shortValue();
    }

    public Integer getInteger(String propertyName) {
        return castToInt(get(propertyName));
    }

    public int getIntValue(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return 0;
        }
        return castToInt(value).intValue();
    }

    public Long getLong(String propertyName) {
        return castToLong(get(propertyName));
    }

    public long getLongValue(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return 0L;
        }
        return castToLong(value).longValue();
    }

    public Float getFloat(String propertyName) {
        return castToFloat(get(propertyName));
    }

    public float getFloatValue(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return 0F;
        }
        return castToFloat(value).floatValue();
    }

    public Double getDouble(String propertyName) {
        return castToDouble(get(propertyName));
    }

    public double getDoubleValue(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return 0D;
        }
        return castToDouble(value);
    }

    public BigDecimal getBigDecimal(String propertyName) {
        return castToBigDecimal(get(propertyName));
    }

    public BigInteger getBigInteger(String propertyName) {
        return castToBigInteger(get(propertyName));
    }

    public String getString(String propertyName) {
        Object value = get(propertyName);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public Date getDate(String propertyName) {
        return castToDate(get(propertyName));
    }

    public java.sql.Date getSqlDate(String propertyName) {
        return castToSqlDate(get(propertyName));
    }

    public java.sql.Timestamp getTimestamp(String propertyName) {
        return castToTimestamp(get(propertyName));
    }

    public boolean isNull(String propertyName) {
        return get(propertyName) == null;
    }
}
