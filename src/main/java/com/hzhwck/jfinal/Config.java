package com.hzhwck.jfinal;

import com.hzhwck.controller.HelloController;
import com.hzhwck.controller.MessageController;
import com.hzhwck.controller.hwc.*;
import com.hzhwck.controller.system.MenuController;
import com.hzhwck.controller.system.RightsController;
import com.hzhwck.controller.system.RoleController;
import com.hzhwck.interceptor.CrossGlobleInterceptor;
import com.hzhwck.interceptor.LoggerGlobalInterceptor;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.*;
import com.hzhwck.model.system.Account;
import com.hzhwck.model.system.Menu;
import com.hzhwck.model.system.Rights;
import com.hzhwck.model.system.Role;
import com.jfinal.config.*;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

/**
 * Created by 陈忠意 on 2017/5/16.
 */
public class Config extends JFinalConfig {
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setBaseUploadPath("hwckimages");
    }
    public void configRoute(Routes me) {
        /*me.add("/hello", HelloController.class, "/");
        me.add("/messages", MessageController.class);
        me.add("/user", UsersController.class);*/
        me.add("/", HelloController.class);

        //system
        me.add("/api/system/rights", RightsController.class);
        me.add("/api/system/menu", MenuController.class);
        me.add("/api/system/role", RoleController.class);

        //hwc
        me.add("/api/hwc/users", UserController.class);
        me.add("/api/hwc/messages", MessageController.class);
        me.add("/api/hwc/reportplans", ReportPlansController.class);
        me.add("/api/hwc/reports", ReportController.class);
        me.add("/api/hwc/samples", SamplesController.class);
        me.add("/api/hwc/servicecompanys", ServiceCompanysController.class);
        me.add("/api/hwc/tablecols", TableColsController.class);
        me.add("/api/hwc/tablegroups", TableGroupsController.class);
        //me.add("/api/hwc/tables", TablesController.class);
        me.add("/api/hwc/warehouse", WarehousesController.class);
        me.add("/api/hwc/areas", AreaController.class);
        me.add("/api/hwc/tables", TableController.class);
        me.add("/api/hwc/images", ImageController.class);
        me.add("/api/hwc/getreports", GetReports.class);
    }
    public void configEngine(Engine me) {}
    public void configPlugin(Plugins me) {
        Prop p = PropKit.use("DbConfig.properties");
        System.out.println("jdbcurl --> " + p.get("jdbcurl") +
                            ", username --> " + p.get("username") +
                            ", password --> " + p.get("password"));
        DruidPlugin dp = new DruidPlugin(p.get("jdbcurl"), p.get("username"), p.get("password"));
        me.add(dp);
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
        arp.setShowSql(true);
        me.add(arp);

        //sys
        arp.addMapping("sys_accounts", Account.class);
        arp.addMapping("sys_rights", Rights.class);
        arp.addMapping("sys_menu", Menu.class);
        arp.addMapping("sys_role", Role.class);

        //hwc
        arp.addMapping("hwc_users", User.class);
        arp.addMapping("hwc_messages", Message.class);
        arp.addMapping("hwc_reportplans", ReportPlans.class);
        arp.addMapping("hwc_reports", Reports.class);
        arp.addMapping("hwc_samples", Samples.class);
        arp.addMapping("hwc_servicecompanys", ServiceCompanys.class);
        arp.addMapping("hwc_table1", Table1.class);
        arp.addMapping("hwc_table2", Table2.class);
        arp.addMapping("hwc_table3", Table3.class);
        arp.addMapping("hwc_tablecols", TableCols.class);
        arp.addMapping("hwc_warehouses", Warehouses.class);
        arp.addMapping("hwc_tables", Tables.class);
        arp.addMapping("hwc_tablegroups", TableGroups.class);
        arp.addMapping("hwc_areas", Area.class);
    }
    public void configInterceptor(Interceptors me) {
        me.addGlobalActionInterceptor(new LoggerGlobalInterceptor());
        me.add(new CrossGlobleInterceptor());
        me.add(new LoginInterceptor());
    }
    public void configHandler(Handlers me) {}
}
