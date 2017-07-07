package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/7/6.
 */
public class Camera extends Model<Camera> {

    public static final Camera dao = new Camera().dao();

    private static final String tableName = TableNames.hwcCameras.split(" ")[0];
    private static final String hc = TableNames.hwcCameras.split(" ")[1] + ".";


    public static Page<Camera> getPage(int pageNumber, int pageSize, String orderBy, String order, String filter){
        StringBuffer sql = new StringBuffer(" from " + tableName + " ");
        sql.append(filter + " ");
        sql.append(" order by " + orderBy + " " + order);
        return Camera.dao.paginate(pageNumber, pageSize, "select * ", sql.toString());
    }
}
