package com.hzhwck.model.system;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class Menu extends Model<Menu> {

    private static final String tableName = "sys_menu";
    public static Menu dao = new Menu().dao();

}
