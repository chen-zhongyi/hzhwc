package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/7/10.
 */
public class Pdf extends Model<Pdf> {
    public static final Pdf dao = new Pdf().dao();
    private static final String p = TableNames.hwcPdf.split(" ")[1] + ".";
    private static final String s = TableNames.hwcSamples.split(" ")[1] + ".";

    public static Pdf getPdfByWarehouseId(String id){
        return Pdf.dao.findFirst("select * from hwc_pdf where warehouseId = " + id);
    }

    public static Page<Pdf> getPage(int pageNumber, int pageSize, String orderBy, String order, String filter, String tables){
        StringBuffer sql = new StringBuffer(" from " + tables + " ");
        sql.append(filter + " ");
        sql.append(" order by " + orderBy + " " + order);
        return Pdf.dao.paginate(pageNumber, pageSize, "select " + p + "*, " + s + "id as sampleId ", sql.toString());
    }
}
