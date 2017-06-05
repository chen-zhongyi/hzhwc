package com.hzhwck.model;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/5/16.
 */
public class User extends Model<User>{

    public static final User dao = new User().dao();
    public static List<User> getUser(){
        return dao.find("select * from user");
    }
}
