package com.hzhwck.model.system;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class Role extends Model<Role> {

    private static final String tableName = "sys_role";
    public static Role dao = new Role().dao();

    public static List<Role> getRoles(){
        return dao.find("select * from " + tableName);
    }
}
