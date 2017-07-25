package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/7/11.
 */
public class Camera2 extends Model<Camera2> {
    public static final Camera2 dao = new Camera2().dao();

    private static final String tableName = TableNames.hwcCameras2.split(" ")[0];
    private static final String hc = TableNames.hwcCameras2.split(" ")[1] + ".";


    public static Page<Camera2> getPage(int pageNumber, int pageSize, String orderBy, String order, String filter, String tables){
        StringBuffer sql = new StringBuffer(" from " + tables + " ");
        sql.append(filter + " ");
        sql.append(" order by " + orderBy + " " + order);
        return Camera2.dao.paginate(pageNumber, pageSize, "select " + hc + "* ", sql.toString());
    }
}
