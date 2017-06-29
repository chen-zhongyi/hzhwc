package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/6/12.
 */
public class Samples extends Model<Samples> {
    public static final Samples dao = new Samples().dao();
    private static final String tableName = "hwc_samples";
    private static final String[] a = {"shtyxydm", "jsdwmc", "ssqx"};

    public static Page<Samples> getPage(int pageNumber, int pageSize, String oderBy, String oder, String filter){
        StringBuffer sql = new StringBuffer("from " + tableName + " ");
        sql.append(filter + " order by " + oderBy + " " + oder);
        return Samples.dao.paginate(pageNumber, pageSize, "select * ", sql.toString());
    }
}
