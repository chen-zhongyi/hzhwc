package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class TableGroups extends Model<TableGroups> {
    public static final TableGroups dao = new TableGroups().dao();
    private static final String tableName = "hwc_tablegroups";

    public static List<TableGroups> getTableGroups(){
        return TableGroups.dao.find("select * from " + tableName );
    }
}
