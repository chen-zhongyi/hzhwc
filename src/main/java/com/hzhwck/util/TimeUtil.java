package com.hzhwck.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class TimeUtil {
    public static String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(System.currentTimeMillis()));
    }
}
