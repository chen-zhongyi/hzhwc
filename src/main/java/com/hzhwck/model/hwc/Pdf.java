package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by 陈忠意 on 2017/7/10.
 */
public class Pdf extends Model<Pdf> {
    public static final Pdf dao = new Pdf().dao();

    public static Pdf getPdfByWarehouseId(String id){
        return Pdf.dao.findFirst("select * from hwc_pdf where warehouseId = " + id);
    }
}
