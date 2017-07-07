package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.system.Account;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.validator.hwc.RegisterLoginValidator;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

import java.sql.SQLException;

/**
 * Created by 陈忠意 on 2017/7/2.
 */
@Clear(LoginInterceptor.class)
public class RegisterLoginController extends BaseController{

    /**
     * 企业用户注册
     * @Param userName 用户名
     * @Param pwd 密码
     * @Param realName 真实姓名
     */
    @Before({POST.class, RegisterLoginValidator.class})
    @ActionKey("/api/hwc/register")
    public void register(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                String userName = getPara("userName");
                String pwd = getPara("pwd");
                String realName = getPara("realName");
                User user = new User()
                        .set("realName", realName)
                        .set("type", HwcUserType.sample)
                        .set("accountId", -1);         //先随便填写一个，之后添加完account后更新
                if(user.save() == false)    return false;
                Account account = new Account()
                        .set("userName", userName)
                        .set("pwd", pwd)
                        .set("user", "hwc_user:" + user.get("id").toString());
                if(account.save() == false) return false;
                user.set("accountId", account.get("id"));
                if(user.update() == false)  return false;
                return true;
            }
        });
        if(success == false)    {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "注册失败，数据库异常, 请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "注册失败，数据库异常, 请联系管理员", null));
            }
        }else{
            setSessionAttr("user", buildUserMap(getPara("userName"), 2));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "注册成功", buildUserMap(getPara("userName"), 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "注册成功", buildUserMap(getPara("userName"), 1)));
            }
        }
    }
}
