package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class User extends Model<User> {

    private static final String hu = TableNames.hwcUsers.split(" ")[1] + ".";
    private static final String[] u = {"realName", "areaCode", "type"};
    private static final String[] a = {"userName", "status"};
    public static final User dao = new User().dao();

    public static User add(User user){
        if(user.save()) return user;
        return null;
    }

    public static User modify(User user){
        if(user.update())   return user;
        return null;
    }

    public static boolean delete(String id){
        return User.dao.deleteById(id);
    }

    public static Page<User> getPageUsers(int pageNumber, int pageSize, String orderBy, String oder, String filter, String tables){
        StringBuffer sb = new StringBuffer(" from " + "(select DISTINCT " + TableNames.systemAccount.split(" ")[1] + ".userName " +
                " from " + TableNames.hwcUsers + tables + " ");
        sb.append(filter);
        sb.append(" order by " + orderBy + " " + oder + ") al");
        System.out.println(sb.toString());
        return dao.paginate(pageNumber, pageSize, "select al.* ", sb.toString());
    }

    public static List<User> getAllUser(){
        return User.dao.find("select " + hu + "* from " + TableNames.hwcUsers);
    }

}
