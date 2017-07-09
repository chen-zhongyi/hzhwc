package com.hzhwck.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class TimeUtil {
    public static String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    public static Date getNow(){
        return new Date(System.currentTimeMillis());
    }

    public static Date getPriNow(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        return c.getTime();
    }

    public static Date getYearFirstMonthTime(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 0);
        return c.getTime();
    }

    public static Date getPriYearFirstMonthTime(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        c.set(Calendar.MONTH, 0);
        return c.getTime();
    }

    public static void main(String[] args){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        System.out.println(c.get(Calendar.YEAR));
        System.out.println(c.get(Calendar.MONTH));
        System.out.println(c.get(Calendar.DATE));
        System.out.println(sf.format(c.getTime()));
        c.add(Calendar.YEAR, -1);
        System.out.println(c.get(Calendar.YEAR));
        System.out.println(c.get(Calendar.MONTH));
        System.out.println(c.get(Calendar.DATE));
        System.out.println(sf.format(c.getTime()));
        c.add(Calendar.YEAR, 1);
        c.set(Calendar.MONTH, 0);
        System.out.println(c.get(Calendar.YEAR));
        System.out.println(c.get(Calendar.MONTH));
        System.out.println(c.get(Calendar.DATE));
        System.out.println(sf.format(c.getTime()));
    }
}
