package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/1.
 */
public class Yuanqu extends Model<Yuanqu> {

    public static final Yuanqu dao = new Yuanqu().dao();

    private static final String tableName = TableNames.hwcYuanqu.split(" ")[0];

    public static List<Yuanqu> getAll(){
        return Yuanqu.dao.find("select * from " + tableName);
    }

    public static List<Yuanqu> getYuanquByAreaCode(String code){
        System.out.println("areaCode = " + code);
        return Yuanqu.dao.find("select * from " + tableName + " where areaCode = '" + code + "'");
    }
}
