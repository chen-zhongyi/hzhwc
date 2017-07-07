package com.hzhwck.model.system;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;

import java.util.List;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class Menu extends Model<Menu> {

    private static final String tableName = TableNames.systemMenus.split(" ")[0];
    private static final String sm = TableNames.systemMenus.split(" ")[1] + ".";
    public static Menu dao = new Menu().dao();

    public static List<Menu> getUserMenus(String accountId){
        String tableUserSystem = TableNames.systemUserSystem.split(" ")[0];
        String su = TableNames.systemUserSystem.split(" ")[1] + ".";
        return Menu.dao.find("select " + sm + "* " +
                "from " + TableNames.systemMenus + ", " + TableNames.systemUserSystem +
                " where " + su + "accountId = " + accountId +
                " and " + su + "right != 'nothing'" +
                " and " + sm + "status = 1 " +
                " and " + sm + "system = " + su + "system " );
    }

    public static List<Menu> getSampleMenus(){
        return Menu.dao.find("select * from " + tableName + " where type = 'hwc_3' and status = 1");
    }
}
