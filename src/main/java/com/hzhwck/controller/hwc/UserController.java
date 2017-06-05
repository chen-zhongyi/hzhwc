package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.system.Account;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.jfinal.core.ActionKey;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class UserController extends BaseController {

    /**
     * 注册用户
     * @Param userName required 用户名
     * @Param pwd required 密码
     * @Param role required 角色
     * @Param realName 真实姓名
     * @Param areaCode 所属区县代码，评审员用户必须
     * @Param areaLwvel 所属区县等级，评审员用户必须
     */
    @ActionKey("/api/hwc/users/register")
    public void addUser(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                User user = new User();
                user.set("realName", getPara("realName"));
                user.set("areaCode", getPara("areaName"));
                user.set("areaLevel", getPara("areaLevel"));
                user = User.add(user);
                if(user == null)    return false;
                Account account = new Account();
                account.set("user", "hwc_users:" + user.get("id"));
                account.set("userName", getPara("userName"));
                account.set("pwd", getPara("pwd"));
                account.set("role", getPara("role"));
                account.set("createAt", TimeUtil.getNowTime());
                account = Account.add(account);
                if(account == null) return false;
                user.set("accountId", account.get("id"));
                if(User.update(user) == null)   return false;
                return true;
            }
        });
        if(success){
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("userName", getPara("userName"));
            //data.put("role", );
            //data.put("areaName", );
            data.put("realName", getPara("realName"));
            renderJson(ResponseUtil.setRes("00", "注册成功", data));
        }else {
            renderJson(ResponseUtil.setRes("01", "注册失败，数据库异常", null));
        }
    }
}
