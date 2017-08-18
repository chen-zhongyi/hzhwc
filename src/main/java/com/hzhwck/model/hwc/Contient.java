package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/1.
 */
public class Contient extends Model<Contient> {

    public static final Contient dao = new Contient().dao();

    private static final String tableName = TableNames.hwcContient.split(" ")[0];

    public static List<Contient> getAll(){
        List<Contient> contients = Contient.dao.find("select * from " + tableName);
        for(Contient c : contients){
            c.put("countrys", Country.getByContientCode(c.getStr("code")));
        }
        return contients;
    }

    public static Contient getByCode(String code){
        return Contient.dao.findFirst("select * from " + tableName + " where code = '" + code + "'");
    }
}
