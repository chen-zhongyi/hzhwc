package com.hzhwck.jfinal;

import com.hzhwck.controller.HelloController;
import com.hzhwck.controller.MessageController;
import com.hzhwck.controller.Users.UsersController;
import com.hzhwck.interceptor.LoggerGlobalInterceptor;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.system.Account;
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
    }
    public void configRoute(Routes me) {
        me.add("/hello", HelloController.class, "/");
        me.add("/messages", MessageController.class);
        me.add("/user", UsersController.class);
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
        //arp.addMapping("user", User.class);
        //arp.addMapping("messages", Message.class);

        //sys
        arp.addMapping("hzhwc.account", Account.class);

        //hwc
        arp.addMapping("hzhwc.hwc_users", User.class);
    }
    public void configInterceptor(Interceptors me) {
        me.addGlobalActionInterceptor(new LoggerGlobalInterceptor());
    }
    public void configHandler(Handlers me) {}
}
