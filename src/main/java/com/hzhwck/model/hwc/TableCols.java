package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class TableCols extends Model<TableCols> {
    private static final String tableName = "hzhwc.hwc_tablecols";
    public static final TableCols dao = new TableCols().dao();

    public static List<TableCols> getColsByTableId(String id){
        return TableCols.dao.find("select * from " + tableName + " where tableId=" + id + " order by orderNum");
    }
}
