package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class Tables extends Model<Tables> {
    public static final Tables dao = new Tables().dao();
    private static final String tableName = TableNames.hwcTables.split(" ")[0];
    private static final String ht = TableNames.hwcTables.split(" ")[1] + ".";

    public static List<Tables> getTables(){
        String tableGroup = TableNames.hwcTableGroups.split(" ")[0];
        String tg = TableNames.hwcTableGroups.split(" ")[1] + ".";
        return Tables.dao.find("select " + ht + "*, " + tg + "frequency from " +
                    TableNames.hwcTables + ", " + TableNames.hwcTableGroups + " " +
                " where " + ht + "groupId = " + tg + "id " );
    }

    public static List<Tables> getTablesByGroupId(String id){
        return Tables.dao.find("select * from " + tableName + " where groupId=" + id);
    }

    public static Tables getTableByTheno(String theNo){
        return Tables.dao.findFirst("select * from " + tableName + " where theNo='" + theNo + "'");
    }
}
