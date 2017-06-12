package com.hzhwck.model.hwc;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class User extends Model<User> {

    private static final String tableName = "hzhwc.hwc_users";
    private static final String[] u = {"realName", "areaCode"};
    private static final String[] a = {"userName", "role", "status"};
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
        StringBuffer sb = new StringBuffer(" from " + tableName + " u, hzhwc.sys_accounts a");
        if(!filter.equals("")){
            sb.append(" where ");
            String[] filters = filter.split(",");
            List<String> filtersAccess = new LinkedList<String>();
            for(String temp : filters){
                if(temp.split(":").length != 2) continue;
                for(int i = 0;i < a.length;++i){
                    if(temp.split(":")[0].equals(a[i])){
                        filtersAccess.add("a." + temp);
                        break;
                    }
                }
                for(int i = 0;i < u.length;++i){
                    if(temp.split(":")[0].equals(u[i])){
                        filtersAccess.add("u." + temp);
                        break;
                    }
                }
            }
            for(int i = 0;i < filtersAccess.size();++i){
                String[] temp = filters[i].split(":");
                sb.append(temp[0] + " like '%" + temp[1] + "%'");
                if(i != filtersAccess.size() - 1){
                    sb.append(" and ");
                }
            }
        }
        if(!oder.equals("id")){
            boolean success = false;
            for(int i = 0;i < a.length;++i){
                if(oder.equals(a[i]))   {
                    oder = "a." + oder;
                    success = true;
                    break;
                }
            }
            for(int i = 0;i < u.length;++i){
                if(oder.equals(u[i]))   {
                    oder = "u." + oder;
                    success = true;
                    break;
                }
            }
            if(success == false)    oder = "a.id";
        }else {
            oder = "a." + oder;
        }
        sb.append(" order by " + orderBy + " " + oder);
        return dao.paginate(pageNumber, pageSize, "select a.userName as userName, a.role as role, a.status as status," +
                "u.realName as realName, u.areaLevel as areaLevel, u.areaCode as areaCode ", sb.toString());
    }

}
