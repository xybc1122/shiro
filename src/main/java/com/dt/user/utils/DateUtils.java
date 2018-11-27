package com.dt.user.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    //饿了么日期转换 时间戳
    public static Long UTCLongODefaultString(String utcString) {
        try {
            if (StringUtils.isEmpty(utcString)) {
                return null;
            }
            utcString = utcString.replace("Z", " UTC");
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date date = utcFormat.parse(utcString);
            return date.getTime();
        } catch (ParseException pe) {
            return null;
        }
    }
    //获得当前时间+后面 N天时间的时间戳
    public static Long GetRearDate(Integer time){
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, time);
        return calendar.getTime().getTime();
    }
}
