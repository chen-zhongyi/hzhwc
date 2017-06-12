package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/12.
 */
public class Warehouses extends Model<Warehouses> {
    public static final Warehouses dao = new Warehouses().dao();
    private static final String tableName = "hzhwc.hwc_warehouses";
    private static final String[] a = {"hwcmc", "yygsmc", "hwcssgj", "hwcsscs"};

    public static Page<Warehouses> getPage(int pageNumber, int pageSize, String oderBy, String oder, String filter){
        StringBuffer sql = new StringBuffer("from " + tableName + " ");
        if(filter != ""){
            List<String> f = new LinkedList<String>();
            for(String temp : filter.split(",")){
                String[] t = temp.split(":");
                if(t.length == 2){
                    for(int j = 0;j < a.length;++j){
                        if(t[0].equals(a[j])){
                            f.add("temp");
                        }
                    }
                }
            }
            for(int i = 0;i < f.size();++i){
                String temp = f.get(i);
                sql.append(temp.split(":")[0] + "like '%" + temp.split(":")[1] + "%' ");
                if(i != f.size() - 1){
                    sql.append("and ");
                }
            }
        }
        sql.append("oder by " + oderBy + " " + oder);
        return Warehouses.dao.paginate(pageNumber, pageSize, "select * ", sql.toString());
    }
}
