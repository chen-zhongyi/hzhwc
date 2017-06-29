package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class Reports extends Model<Reports> {
    private static final String hr = TableNames.hwcReports.split(" ")[1] + ".";
    public static final Reports dao = new Reports().dao();

    public static Page<Reports> getPage(int pageNumber, int pageSize, String oderBy, String oder, String filter, String tables){
        StringBuffer sql = new StringBuffer("from " + TableNames.hwcReports + tables + " ");
        sql.append(filter + " ") ;
        sql.append("order by " + oderBy + " " + oder);
        return Reports.dao.paginate(pageNumber, pageSize, "select " + hr + "* ", sql.toString());
    }

    public static List<Reports> findByWarehouseId(String id){
        return Reports.dao.find("select " + hr + "* from " + TableNames.hwcReports + " where " + hr + "warehouseId=" + id);
    }
}
