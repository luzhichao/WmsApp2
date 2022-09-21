package com.linkingwin.education.studyonline.common.utils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.net.ParseException;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 控件操作通用类
 *
 * @author wangshiyu
 */
public class WidgetUtil {

    /**
     * 显示日期弹出框工具
     *
     * @param context
     * @param callBack 选择后回调接口类
     * @param date
     */
    public static void showDatePickerDialog(Context context, OnDateSetListener callBack, Date date) {
        date = null != date ? date : new Date();
        Calendar d = Calendar.getInstance(Locale.CHINA);
        d.setTime(date);
        int year = d.get(Calendar.YEAR);
        int month = d.get(Calendar.MONTH);
        int day = d.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, callBack, year, month, day);
        datePickerDialog.show();
    }

    /**
     * 显示日期弹出框选择后自动填充(默认当前时间)
     *
     * @param context
     */
    public static void showDatePickerDialog(Context context, final View dateview, final String formatStr, Date date) {
        showDatePickerDialog(context, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar d = Calendar.getInstance(Locale.CHINA);
                d.set(Calendar.YEAR, year);
                d.set(Calendar.MONTH, monthOfYear);
                d.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dateStr = DateUtils.parseDateToStr(d.getTime(), DateUtils.yearMothDay);
                if (dateview instanceof TextView) {
                    ((TextView) dateview).setText(dateStr);
                } else if (dateview instanceof EditText) {
                    ((EditText) dateview).setText(dateStr);
                }
            }
        }, date);
    }

    /**
     * 显示time弹出框工具
     *
     * @param context
     * @param callBack 选择后回调接口类
     * @param date
     */
    public static void showTimePickerDialog(Context context, OnTimeSetListener callBack, Date date) {
        date = null != date ? date : new Date();
        Calendar d = Calendar.getInstance(Locale.CHINA);
        d.setTime(date);
        int hours = d.get(Calendar.HOUR);
        int minute = d.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, callBack, hours, minute, true);
        timePickerDialog.show();
    }

    /**
     * 显示日期弹出框选择后自动填充(默认当前时间)
     *
     * @param context
     */
    public static void showTimePickerDialog(Context context, final View dateview, Date date) {
        showTimePickerDialog(context, new OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hours, int minute) {
                Calendar d = Calendar.getInstance(Locale.CHINA);
                d.set(Calendar.HOUR, hours);
                d.set(Calendar.MINUTE, minute);
                String dateStr = hours + ":" + minute;
                if (dateview instanceof TextView) {
                    ((TextView) dateview).setText(dateStr);
                } else if (dateview instanceof EditText) {
                    ((EditText) dateview).setText(dateStr);
                }
            }
        }, date);
    }

    /**
     * 如果NULLFLAG为TRUE则为空时不进行考贝；
     *
     * @param target
     * @param source
     * @param nullFlag
     */
    public static void copyProperty(Object target, Object source, boolean nullFlag) {
        Field[] tarFields = target.getClass().getDeclaredFields();
        Field[] sourFields = source.getClass().getDeclaredFields();
        for (int i = 0; i < tarFields.length; i++) {
            Field f = tarFields[i];
            int n = f.getModifiers();
            // 判断是FINAL字段
            if (Modifier.isFinal(n)) {
                continue;
            }
            f.setAccessible(true);
            boolean flag = false;
            String tarType = tarFields[i].getType().toString();
            String name = tarFields[i].getName();
            for (int j = 0; j < sourFields.length; j++) {
                Field temp = sourFields[j];
                int m = temp.getModifiers();
                if (Modifier.isFinal(m)) {
                    continue;
                }
                temp.setAccessible(true);
                String sourType = sourFields[j].getType().toString();
                String sourName = sourFields[j].getName();
                if (name.equalsIgnoreCase(sourName)) {
                    try {
                        flag = true;
                        if (temp.get(source) == null && nullFlag) {
                            break;
                        }
                        if (tarType.equals(sourType)) {
                            f.set(target, temp.get(source));
                        } else if ((tarType.indexOf("Date") != -1 || (tarType.indexOf("Timestamp") != -1))
                                && sourType.indexOf("String") != -1) {
                            String time = (String) temp.get(source);
                            if (time != null && !(time.trim().equals(""))) {
                                String dateStr = "yyyy-MM-dd HH:mm:ss";
                                if (time.trim().length() < 11) {
                                    dateStr = "yyyy-MM-dd";
                                }
                                SimpleDateFormat myFmt2 = new SimpleDateFormat(dateStr);
                                Date date = new Date();
                                try {
                                    date = myFmt2.parse(time);
                                } catch (ParseException e) {
                                    System.out.println("copyProperty 时不能进行字符串到日期的转换");
                                    e.printStackTrace();
                                }
                                f.set(target, date);
                            }
                        } else if (tarType.indexOf("String") != -1
                                && (sourType.indexOf("Date") != -1 || (sourType.indexOf("Timestamp") != -1) || (sourType
                                .indexOf("CnDate") != -1))) {
                            Date time = (Date) temp.get(source);
                            if (time != null) {
                                SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                f.set(target, myFmt2.format(time));
                            }

                        } else if (tarType.indexOf("String") != -1 && sourType.indexOf("Long") != -1) {
                            if (temp.get(source) != null) {
                                f.set(target, "" + temp.get(source));
                            } else {
                                f.set(target, "");
                            }
                        } else if (tarType.indexOf("Long") != -1 && sourType.indexOf("String") != -1) {
                            if (temp.get(source) != null && !temp.get(source).equals("")) {
                                f.set(target, new Long(temp.get(source).toString()));
                            }
                        } else if (tarType.indexOf("Double") != -1 && sourType.indexOf("String") != -1) {
                            if (temp.get(source) != null && !temp.get(source).equals("")) {
                                f.set(target, new Double(temp.get(source).toString()));
                            }
                        } else if (tarType.indexOf("String") != -1 && sourType.indexOf("Double") != -1) {
                            if (temp.get(source) != null) {
                                f.set(target, "" + temp.get(source));
                            } else {
                                f.set(target, "");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                temp.setAccessible(false);
                if (flag) {
                    break;
                }
            }
            f.setAccessible(false);
        }
    }
}
