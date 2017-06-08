package com.hzhwck.model.system;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class Rights extends Model<Rights> {
    private static final String tableName = "hzhwc.sys_rights";
    public static Rights dao = new Rights().dao();

    public static List<Rights> getRights(){
        return dao.find("select * from " + tableName);
    }
}
