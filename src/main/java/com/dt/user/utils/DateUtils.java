package com.dt.user.utils;

import com.dt.user.model.FinancialSalesBalance;
import com.dt.user.toos.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    /**
     * 设置时间转换类型
     *
     * @param fsb
     * @param seId
     * @throws IOException
     */
    public static void setDate(FinancialSalesBalance fsb, Integer seId, String time){
        switch (seId) {
            case 1:
                fsb.setDate(DateUtils.getTime(time, Constants.USA_TIME));
                break;
            case 2:
                fsb.setDate(DateUtils.getTime(time, Constants.CANADA_TIME));
                break;
            case 3:
                fsb.setDate(DateUtils.getTime(time, Constants.AUSTRALIA_TIME));
                break;
            case 4:
                fsb.setDate(DateUtils.getTime(time, Constants.UNITED_KINGDOM_TIME));
                break;
            case 5:
                fsb.setDate(DateUtils.getTime(time, Constants.GERMAN_TIME));
                break;
            case 6:
                fsb.setDate(DateUtils.getFranceTime(time, Constants.FRANCE_TIME));
                break;
            case 7:
                fsb.setDate(DateUtils.getItalyTime(time, Constants.ITALY_TIME));
                break;
            case 8:
                fsb.setDate(DateUtils.getTime(time, Constants.SPAIN_TIME));
                break;
            case 9:
                fsb.setDate(DateUtils.getTime(time, Constants.JAPAN_TIME));
                break;
            case 10:
                fsb.setDate(DateUtils.getTime(time, Constants.MEXICO_TIME));
                break;
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
     * 美国/加拿大/德国/澳大利亚/英国/时间转换/北美
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

    public static Long getXlsStrTime(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        String newDate = null;
        Long time = null;
        int janvIndex = date.indexOf("一月");
        int fevrIndex = date.indexOf("二月");
        int marsIndex = date.indexOf("三月");
        int avrIndex = date.indexOf("四月");
        int maiIndex = date.indexOf("五月");
        int juinIndex = date.indexOf("六月");
        int juilIndex = date.indexOf("七月");
        int aoutIndex = date.indexOf("八月");
        int septIndex = date.indexOf("九月");
        int octIndex = date.indexOf("十月");
        int novIndex = date.indexOf("十一月");
        int dicIndex = date.indexOf("十二月");
        if (janvIndex != -1) {
            newDate = date.replace("一月", "01");
        } else if (fevrIndex != -1) {
            newDate = date.replace("二月", "02");
        } else if (marsIndex != -1) {
            newDate = date.replace("三月", "03");
        } else if (avrIndex != -1) {
            newDate = date.replace("四月", "04");
        } else if (maiIndex != -1) {
            newDate = date.replace("五月", "05");
        } else if (juinIndex != -1) {
            newDate = date.replace("六月", "06");
        } else if (juilIndex != -1) {
            newDate = date.replace("七月", "07");
        } else if (aoutIndex != -1) {
            newDate = date.replace("八月", "08");
        } else if (septIndex != -1) {
            newDate = date.replace("九月", "09");
        } else if (octIndex != -1) {
            newDate = date.replace("十月", "10");
        }
        if (novIndex != -1) {
            newDate = date.replace("十一月", "11");
        }
        if (dicIndex != -1) {
            newDate = date.replace("十二月", "12");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            time = sdf.parse(newDate).getTime();
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
        String stringDate = "18-十二月-2019";
        System.out.println(getXlsStrTime(stringDate,"dd-MM-yyyy"));
//        System.out.println(DateUtils.getFranceTime(stringDate, Constants.FRANCE_TIME));

    }
}
