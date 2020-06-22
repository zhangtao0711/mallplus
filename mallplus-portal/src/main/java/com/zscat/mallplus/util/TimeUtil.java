package com.zscat.mallplus.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtil {

    public static Long getSecondsTobeforedawn() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        // 改成这样就好了
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
    /**
     * 获取昨天日期字符串
     * @param format  格式(例如:yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static String getYesterdayStr(String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return new SimpleDateFormat(format).format(cal.getTime());
    }


}
