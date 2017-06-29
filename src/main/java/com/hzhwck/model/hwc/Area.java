package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/17.
 */
public class Area extends Model<Area> {
    public static final Area dao = new Area().dao();
    private static final String tableName = "hwc_areas";

    public static List<Area> getAreas(){
        return Area.dao.find("select * from " + tableName);
    }

    public static Area findByAreaCode(String code){
        return Area.dao.findFirst("select * from " + tableName + " where code='" + code + "'");
    }
}
