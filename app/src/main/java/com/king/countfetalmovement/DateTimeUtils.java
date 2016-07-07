package com.king.countfetalmovement;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by king.zhou on 2016/3/25.
 */
public class DateTimeUtils {

    /**
     *
     * @param unixTime
     * @param "dd/MM/yyyy HH:mm:ss" "mm-dd"
     * @return
     */

    static SimpleDateFormat simpleDateFormat,simpleDateFormatYear,simpleDateFormatMonth;
    static Date date;
    public static String convertUnixTimeStampToNormalTime(long time,String format){
        if(simpleDateFormat==null){
            simpleDateFormat = new SimpleDateFormat(format);
        }
        if(date == null){
            date = new Date();
        }
        date.setTime(time);
        String result = simpleDateFormat.format(date);
        return result;
    }

    public static String getYearInUnixTimeStamp(Long unixTime){
        if(simpleDateFormatYear==null){
            simpleDateFormatYear = new SimpleDateFormat("yyyy");
        }
        if(date == null){
            date = new Date();
        }
        date.setTime(unixTime*1000);
        String result = simpleDateFormatYear.format(date);
        return result;
    }
    public static String getMonthInUnixTimeStamp(Long unixTime){
        if(simpleDateFormatMonth==null){
            simpleDateFormatMonth = new SimpleDateFormat("MM");
        }
        if(date == null){
            date = new Date();
        }
        date.setTime(unixTime*1000);
        String result = simpleDateFormatMonth.format(date);
        if(result.startsWith("0"))
            result = result.substring(1);
        return result;
    }
    public static String getYearInYYYYMM(String str){
        String result = "";
        if (str!=null&&str.length()>5){
            result = str.substring(0,4);
        }
        return  result;
    }
    public static String getMonthInYYYYMM(String str){
        String result = "";
        if (str!=null&&str.length()>5){
            result = str.substring(4);
        }
        return  result;
    }

    /**
     * YYYYMM to YYYY年MM月
     * @param date
     * @return
     */
    public static String formatYearMonth(String date){
        String result = "",year,month;
        if (date.length()>=6){
            year = date.substring(0,4);
            if ("0".equals(date.substring(4,5))){
                month = date.substring(5,6);
            }else{
                month = date.substring(4,6);
            }
            result = year+"年"+month+"月";
        }
        return result;
    }
    /**
     * MMDD to MM-DD
     * @param date
     * @return
     */
    public static String formatMonth(String date){
        String result = "",month,day;
        if (date.length()>=4){
            month = date.substring(0,2);
            day = date.substring(2,4);
            result = month+"-"+day;
        }
        return result;
    }


}
