package com.dt.user.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    public static Long getRearDate(Integer time) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, time);
        return calendar.getTime().getTime();
    }

    /**
     * 德国时间转换
     *
     * @param DatumUhrzei
     * @return
     */
    public static Long getGermanTime(String DatumUhrzei) {
        if (StringUtils.isBlank(DatumUhrzei)) {
            return null;
        }
        Long time = null;
        int indexGMT = DatumUhrzei.indexOf("G");
        String date = DatumUhrzei.substring(0, indexGMT).replace('.', ':').trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");
        try {
            time = sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 美国/加拿大时间转换
     *
     * @throws ParseException
     */
    public static Long getTime(String date,String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        Long time=null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            time = sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    public static void main(String[] args) throws ParseException {
        String stringDate = "30/11/2018 10:25:28 AM GMT+09:00";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a", Locale.ENGLISH);
        System.out.println(sdf.parse(stringDate).getTime());
    }
}
