package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/16.
 */
public class ServiceCompanys extends Model<ServiceCompanys> {
    private static final String tableName = TableNames.hwcServiceCompanys.split(" ")[0];
    private static final String sc = TableNames.hwcServiceCompanys.split(" ")[1] + ".";
    public static final ServiceCompanys dao = new ServiceCompanys().dao();

    public static List<ServiceCompanys> getServiceCompanysByWarehouseId(String id){
        return ServiceCompanys.dao.find("select * from " + tableName + " where warehouseId=" + id);
    }

    public static boolean isExist(ServiceCompanys s){
        return ServiceCompanys.dao.findFirst("select * from " + tableName
                + " where qymc = '" + s.get("qymc") + "' "
                + " and lxr = '" + s.get("lxr") + "' "
                + " and qyjs = '" + s.get("qyjs") + "' "
                + " and dz = '" + s.get("dz") + "' "
                + " and hwlx = '" + s.get("hwlx") + "' "
                + " and htjzsj = '" + s.get("htjzsj") + "' "
                + " and warehouseId = " + s.get("warehouseId")) != null;
    }

    public static Page<ServiceCompanys> getPage(int pageNumber, int pageSize, String oderBy, String oder, String filter, String tables){
        StringBuffer sql = new StringBuffer("from " + tables + " ");
        sql.append(filter);
        sql.append(" order by " + oderBy + " " + oder);
        return ServiceCompanys.dao.paginate(pageNumber, pageSize, "select " + sc + "* ", sql.toString());
    }
}
