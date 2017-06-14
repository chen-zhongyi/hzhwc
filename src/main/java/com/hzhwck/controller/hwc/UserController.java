package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.system.Account;
import com.hzhwck.model.system.Role;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.hzhwck.validator.hwc.UserValidator;
import com.jfinal.aop.Before;
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
    @Before(UserValidator.class)
    @ActionKey("/api/hwc/users/register")
    public void addUser(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                User user = new User();
                user.set("type", 1);
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
                if(User.modify(user) == null)   return false;
                return true;
            }
        });
        if(success){
            renderJson(ResponseUtil.setRes("00", "注册成功", buildUserMap(getPara("userName"), 1)));
        }else {
            renderJson(ResponseUtil.setRes("01", "注册失败，数据库异常", null));
        }
    }

    /**
     * 修改用户信息
     * @Param userName 用户名
     * @Param role 角色
     * @Param status 账号状态
     * @Param realName 姓名
     * @Param areaLevel 区县等级
     * @Param areaCode 区县代码
     *
     * @return userInfo  返回修改后的用户信息
     */
    @ActionKey("/api/hwc/users/modify")
    public void update(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                Account account = Account.findByUserName(getPara("userName"));
                Integer role = getParaToInt("role");
                if(role != null)    account.set("role", role);
                Integer status = getParaToInt("status");
                if(status != null)  account.set("status", status);
                if(Account.modify(account) == null) return false;
                User user = User.dao.findById(account.getStr("user").split(",")[1]);
                String realName = getPara("realName");
                if(realName != null)    user.set("realName", realName);
                Integer areaLevel = getParaToInt("areaLevel");
                if(areaLevel != null)   user.set("areaLevel", areaLevel);
                String areaCode = getPara("areaCode");
                if(areaCode != null)    user.set("areaCode", areaCode);
                if(User.modify(user) == null)   return false;
                return true;
            }
        });
        if(success){
            //???
            renderJson(ResponseUtil.setRes("00", "海外仓用户更新成功", buildUserMap(getPara("userName"), 1)));
        }else{
            renderJson(ResponseUtil.setRes("01", "海外仓用户更新失败，数据库异常", null));
        }
    }

    /**
     * 删除用户
     *
     * @Param userName
     *
     * @return
     */
    @ActionKey("/api/hwc/users/delete")
    public void delete(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                Account account = Account.findByUserName(getPara("userName"));
                if(User.delete(account.getStr("user").split(",")[1]) == false)   return false;
                return Account.delete(account.getStr("id"));
            }
        });
        if(success){
            renderJson(ResponseUtil.setRes("00", "海外仓用户删除成功", "success"));
        }else {
            renderJson( ResponseUtil.setRes("01", "海外仓用户删除失败，数据库异常", "fail"));
        }
    }

    /**
     * 登录
     *
     * @Param userName 用户名
     * @Param pwd 密码
     */
    @ActionKey("/api/hwc/users/login")
    public void login(){
        String userName = getPara("userName");
        String pwd = getPara("pwd");
        Account account = Account.findByUserName(userName);
        if(account.getStr("pwd").equals(pwd)){
            account.set("lastLogin", TimeUtil.getNowTime());
            Account.modify(account);
            setSessionAttr("user", buildUserMap(userName, 2));
            renderJson(ResponseUtil.setRes("00", "海外仓用户登录成功", "success"));
        }else {
            renderJson(ResponseUtil.setRes("02", "海外仓用户登录失败，用户名和密码不匹配", "fail"));
        }
    }

    /**
     * 用户登出
     * @Param userName
     */
    @ActionKey("/api/hwc/users/logout")
    public void Logout(){
        removeSessionAttr("user");
        renderJson(ResponseUtil.setRes("00", "海外仓用户登出成功", "success"));
    }

    /**
     * 用户搜索
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param orderBy 排序字段，默认id
     * @Param oder desc, asc
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】
     */
    @ActionKey("/api/hwc/users/search")
    public void getPageUsers(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String orderBy = getPara("oderBy", "a.id");
        String oder = getPara("oder", "asc");
        String filter = getPara("filter", "");
        renderJson(ResponseUtil.setRes("00", "获取用户分页信息成功", User.getPageUsers(pageNumber, pageSize, orderBy, oder, filter)));
    }

    /**
     * 用户信息整合
     * @param userName 用户名
     * @param type 整合类型 type=0表示返回给客户端用户信息，type=1表示存在服务端session中的用户信息
     * @return 返回用户信息
     */
    private Map<String, Object> buildUserMap(String userName, int type){
        Map<String, Object> map = new HashMap<String, Object>();
        Account account = Account.findByUserName(userName);
        map.put("userName", account.get("userName"));
        map.put("status", account.get("status"));
        map.put("role", account.get("role"));
        User user = User.dao.findById(account.getStr("user").split(",")[1]);
        map.put("realName", user.get("realName"));
        map.put("areaLevel", user.get("areaLevel"));
        map.put("areaCode", user.get("areaCode"));
        if(user.get("sampleId") != null){
            map.put("sample", Samples.dao.findById(user.get("sampleId")));
        }
        if(type == 2){
            map.put("accountId", account.get("id"));
            map.put("hwcUserId", user.get("id"));
            map.put("pwd", account.get("pwd"));
            Role role = Role.dao.findById(account.get("role"));
            map.put("role", role.toRecord().getColumns());
            map.put("roleMap", null);
        }
        return map;
    }
}
