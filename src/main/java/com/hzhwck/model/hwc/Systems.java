package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/2.
 */
public class Systems extends Model<Systems> {

    private static final String tableName = TableNames.hwcSystem.split(" ")[0];
    public static final Systems dao = new Systems().dao();

    public static Systems getSystemsByCode(String code){
        return Systems.dao.findFirst("select * from " + tableName + " where code = '" + code + "'");
    }

    public static List<Systems> findAll(){
        return Systems.dao.find("select * from " + tableName);
    }
}
