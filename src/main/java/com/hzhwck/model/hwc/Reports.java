package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class Reports extends Model<Reports> {
    private static final String tableName = "hzhwc.hwc_reports";
    public static final Reports dao = new Reports().dao();


}
