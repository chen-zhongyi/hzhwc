package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/1.
 */
public class Country extends Model<Country> {

    public static final Country dao = new Country().dao();

    public static final String tableName = TableNames.hwcCountry.split(" ")[0];

    public static List<Country> getAll() {
        return Country.dao.find("select * from " + tableName);
    }

    public static List<Country> getByContientCode(String code){
        return Country.dao.find("select * from " + tableName + " where contientCode = '" + code + "'");
    }

    public static Country getByCode(String code){
        return Country.dao.findFirst("select * from " + tableName + " where code = '" + code + "'");
    }
}
