package com.hzhwck.model.users;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/5/22.
 */
public class Users extends Model<Users> {
    public static final Users dao = new Users().dao();

    public static Users save(Users users){
        if(users.save())    {
            users.remove("pwd");
            return users;
        }
        return null;
    }

    public static Users update(Users users){
        if(users.update())  return findByUserName(users.getStr("userName"));
        return null;
    }

    public static Users findByUserName(String userName){
        Users user = dao.findFirst("select * from users where userName = " + userName);
        if(user != null)    user.remove("pwd");
        return user;
    }

    public static boolean delete(Users users){
        return dao.deleteById(users.get("id"));
    }

    public static Users login(String userName, String password){
        Users user = findByUserName(userName);
        if(user.getStr("pwd").equals(password))    return user;
        return null;
    }

    public static List<Record> getUserSystems(String id){
        return Db.find("select * from usersystems where userId = " + id);
    }
}
