package com.zscat.mallplus.set.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static Date addDateMinut(Date date, int hour){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("front:" + format.format(date)); //显示输入的日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);// 24小时制
        date = cal.getTime();
//        System.out.println("after:" + format.format(date));  //显示更新后的日期
        return date;

    }

    public static Date addDateYear(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR,year);
        Date time = calendar.getTime();
        return time;
    }

    public static Date subDateYear(Date date,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,year);
        Date time = calendar.getTime();
        return time;
    }
}
