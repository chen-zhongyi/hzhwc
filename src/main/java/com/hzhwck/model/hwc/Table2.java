package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/6/16.
 */
public class Table2 extends Model<Table2> {
    public static final Table2 dao = new Table2().dao();

    public static Page<Table2> getPage(int pageNumber, int pageSize, String orderBy, String order, String filter, String tables){
        StringBuffer sql = new StringBuffer(" from " + tables + " ");
        sql.append(filter + " ");
        sql.append(" order by " + orderBy + " " + order + " ");
        String hr = TableNames.hwcReports.split(" ")[1] + ".";
        String ht2 = TableNames.hwcTable2.split(" ")[1] + ".";
        return Table2.dao.paginate(pageNumber, pageSize, "select " + hr + "*, " + ht2 + "* ", sql.toString());
    }
}
