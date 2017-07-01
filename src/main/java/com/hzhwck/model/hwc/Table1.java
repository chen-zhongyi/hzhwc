package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/6/16.
 */
public class Table1 extends Model<Table1> {
    public static final Table1 dao = new Table1().dao();

    public static Page<Table1> getPage(int pageNumber, int pageSize, String orderBy, String order, String filter, String tables){
        StringBuffer sql = new StringBuffer(" from " + tables + " ");
        sql.append(filter + " ");
        sql.append(" order by " + orderBy + " " + order + " ");
        String hr = TableNames.hwcReports.split(" ")[1] + ".";
        String ht1 = TableNames.hwcTable1.split(" ")[1] + ".";
        return Table1.dao.paginate(pageNumber, pageSize, "select " + hr + "*, " + ht1 + "* ", sql.toString());
    }
}
