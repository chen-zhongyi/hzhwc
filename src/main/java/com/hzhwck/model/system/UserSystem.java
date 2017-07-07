package com.hzhwck.model.system;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/2.
 */
public class UserSystem extends Model<UserSystem> {

    private static final String tableName = TableNames.systemUserSystem.split(" ")[0];
    public static final UserSystem dao = new UserSystem().dao();

    public static List<UserSystem> getByAccountId(String id){
        return UserSystem.dao.find("select * from " + tableName + " where accountId = " + id);
    }

    public static UserSystem getBySystemAndAccountId(String sys, String id){
        return UserSystem.dao.findFirst("select * from " + tableName + " where accountId = " + id + " and system = '" + sys + "'");
    }
}
