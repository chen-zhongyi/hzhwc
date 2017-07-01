package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/6/16.
 */
public class Table3 extends Model<Table3> {
    public static final Table3 dao = new Table3().dao();

    public static Page<Table3> getPage(int pageNumber, int pageSize, String orderBy, String order, String filter, String tables){
        StringBuffer sql = new StringBuffer(" from " + tables + " ");
        sql.append(filter + " ");
        sql.append(" order by " + orderBy + " " + order + " ");
        String hr = TableNames.hwcReports.split(" ")[1] + ".";
        String ht3 = TableNames.hwcTable3.split(" ")[1] + ".";
        return Table3.dao.paginate(pageNumber, pageSize, "select " + hr + "*, " + ht3 + "* ", sql.toString());
    }
}
