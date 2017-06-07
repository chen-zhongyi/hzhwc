package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class User extends Model<User> {

    private static final String tableName = "hzhwc.hwc_users";
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

    public static Page<User> getPageUsers(int pageNumber, int pageSize, String orderBy, String oder, String filter){
        StringBuffer sb = new StringBuffer(" from hzhwc.hwc_users u, hzhwc.sys_accounts a");
        if(!filter.equals("")){
            sb.append(" where ");
            String[] filters = filter.split(",");
            for(int i = 0;i < filters.length;++i){
                String[] temp = filters[i].split(":");
                sb.append(temp[0] + "='%" + temp[1] + "%'");
                if(i != filters.length - 1){
                    sb.append(" and ");
                }
            }
        }
        sb.append(" order by " + orderBy + " " + oder);
        return dao.paginate(pageNumber, pageSize, "select * ", sb.toString());
    }

}
