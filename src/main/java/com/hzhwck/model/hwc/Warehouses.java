package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/12.
 */
public class Warehouses extends Model<Warehouses> {
    public static final Warehouses dao = new Warehouses().dao();
    private static final String tableName = TableNames.hwcWarehouses.split(" ")[0];
    private static final String hw = TableNames.hwcWarehouses.split(" ")[1] + ".";
    private static final String[] a = {"hwcmc", "yygsmc", "hwcssgj", "hwcsscs"};

    public static Page<Warehouses> getPage(int pageNumber, int pageSize, String oderBy, String oder, String filter, String tables){
        StringBuffer sql = new StringBuffer("from " + tables + " ");
        sql.append(filter);
        sql.append(" order by " + oderBy + " " + oder);
        return Warehouses.dao.paginate(pageNumber, pageSize, "select " + hw + "* ", sql.toString());
    }

    public static List<Warehouses> getLatAndLng(){
        return Warehouses.dao.find("select hwcmc, sampleId, lat, lng, hwcxxdz, cklx, ckyt from " + tableName + " where lat is not null and lng is not null ");
    }
}
