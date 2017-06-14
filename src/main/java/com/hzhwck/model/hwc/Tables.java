package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class Tables extends Model<Tables> {
    public static final Tables dao = new Tables().dao();
    private static final String tableName = "hzhwc.hwc_tables";

    public static List<Tables> getTables(){
        return Tables.dao.find("select * from " + tableName );
    }

    public static List<Tables> getTablesByGroupId(String id){
        return Tables.dao.find("select * from " + tableName + " where groupId=" + id);
    }
}
