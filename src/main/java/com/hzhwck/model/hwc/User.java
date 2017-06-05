package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class User extends Model<User> {

    private static final String tableName = "hzhwc.hwc_users";
    private static final User dao = new User().dao();

    public static User add(User user){
        if(user.save()) return user;
        return null;
    }

    public static User update(User user){
        if(user.update())   return user;
        return null;
    }

    public static boolean delete(String id){
        return User.dao.deleteById(id);
    }
}
