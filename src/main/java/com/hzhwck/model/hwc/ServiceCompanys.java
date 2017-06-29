package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/16.
 */
public class ServiceCompanys extends Model<ServiceCompanys> {
    private static final String tableName = "hwc_servicecompanys";
    public static final ServiceCompanys dao = new ServiceCompanys().dao();

    public static List<ServiceCompanys> getServiceCompanysByWarehouseId(String id){
        return ServiceCompanys.dao.find("select * from " + tableName + " where warehouseId=" + id);
    }
}
