package com.dt.user.utils;

import com.dt.user.toos.Constants;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * 美国/加拿大/德国/澳大利亚/英国/时间转换
     *
     * @throws ParseException
     */
    public static Long getTime(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        Long time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            time = sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 意大利日期转换
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Long getItalyTime(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        Long time = null;
        String newDate = null;
        int genIndex = date.indexOf("gen");
        int febIndex = date.indexOf("feb");
        int marIndex = date.indexOf("mar");
        int aprIndex = date.indexOf("apr");
        int magIndex = date.indexOf("mag");
        int giuIndex = date.indexOf("giu");
        int lugIndex = date.indexOf("lug");
        int agoIndex = date.indexOf("ago");
        int setIndex = date.indexOf("set");
        int ottIndex = date.indexOf("ott");
        int novIndex = date.indexOf("nov");
        int dicIndex = date.indexOf("dic");
        if (genIndex != -1) {
            newDate = date.replace("gen", "01");
        } else if (febIndex != -1) {
            newDate = date.replace("feb", "02");
        } else if (marIndex != -1) {
            newDate = date.replace("mar", "03");
        } else if (aprIndex != -1) {
            newDate = date.replace("apr", "04");
        } else if (magIndex != -1) {
            newDate = date.replace("mag", "05");
        } else if (giuIndex != -1) {
            newDate = date.replace("giu", "06");
        } else if (lugIndex != -1) {
            newDate = date.replace("lug", "07");
        } else if (agoIndex != -1) {
            newDate = date.replace("ago", "08");
        } else if (setIndex != -1) {
            newDate = date.replace("set", "09");
        } else if (ottIndex != -1) {
            newDate = date.replace("ott", "10");
        } else if (novIndex != -1) {
            newDate = date.replace("nov", "11");
        } else if (dicIndex != -1) {
            newDate = date.replace("dic", "12");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            time = sdf.parse(newDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 法国日期转换
     * @param date
     * @param pattern
     * @return
     */
    public static Long getFranceTime(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        Long time = null;
        String newDate = null;
        int janvIndex = date.indexOf("janv");
        int fevrIndex = date.indexOf("févr");
        int marsIndex = date.indexOf("mars");
        int avrIndex = date.indexOf("avr");
        int maiIndex = date.indexOf("mai");
        int juinIndex = date.indexOf("juin");
        int juilIndex = date.indexOf("juil");
        int aoutIndex = date.indexOf("août");
        int septIndex = date.indexOf("sept");
        int octIndex = date.indexOf("oct");
        int novIndex = date.indexOf("nov");
        int dicIndex = date.indexOf("déc");
        if (janvIndex != -1) {
            newDate = date.replace("janv", "01");
        } else if (fevrIndex != -1) {
            newDate = date.replace("févr", "02");
        } else if (marsIndex != -1) {
            newDate = date.replace("mars", "03");
        } else if (avrIndex != -1) {
            newDate = date.replace("mai", "04");
        } else if (maiIndex != -1) {
            newDate = date.replace("mag", "05");
        } else if (juinIndex != -1) {
            newDate = date.replace("juin", "06");
        } else if (juilIndex != -1) {
            newDate = date.replace("juil", "07");
        } else if (aoutIndex != -1) {
            newDate = date.replace("août", "08");
        } else if (septIndex != -1) {
            newDate = date.replace("sept", "09");
        } else if (octIndex != -1) {
            newDate = date.replace("oct", "10");
        } else if (novIndex != -1) {
            newDate = date.replace("nov", "11");
        } else if (dicIndex != -1) {
            newDate = date.replace("déc", "12");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
        try {
            time = sdf.parse(newDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static void main(String[] args) throws ParseException {
        String stringDate = "31/10/2018 16:06:26 PDT";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        System.out.println(sdf.parse(stringDate).getTime());
//        System.out.println(DateUtils.getFranceTime(stringDate, Constants.FRANCE_TIME));

    }
}
