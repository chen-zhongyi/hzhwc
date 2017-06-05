package com.hzhwck.controller.Users;

import com.hzhwck.model.users.Users;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/5/22.
 */
public class UsersController extends Controller {
    private static Map<String, Object> res = new HashMap<String, Object>();

    /**
     * 根据用户名查询用户信息
     * @Param userName  用户名
     */
    @ActionKey("/users")
    public void users(){
        String userName = getPara("userName");
        if(!checkLoginUser(userName))   return ;
        Users users = Users.findByUserName(userName);
        if(users == null){
            res.put("code", "01");
            res.put("msg", "根据用户名查询用户信息出错");
            res.put("data", null);
        }else {
            res.put("code", "00");
            res.put("msg", "根据用户名查询用户信息成功");
            res.put("data", users);
        }
        renderJson(res);
    }

    /**
     * 登录
     * @Param userName  用户名
     * @Param pwd 密码
     */
    @ActionKey("/users/login")
    public void userLogin(){
        String userName = getPara("userName");
        String pwd = getPara("pwd");
        Users user = Users.login(userName, pwd);
        if(user == null){
            res.put("code", "01");
            res.put("msg", "用户名和密码不匹配");
            res.put("data", null);
        }else {
            res.put("code", "00");
            res.put("msg", "登录成功");
            res.put("data", user);
            setSessionAttr("user", user);
        }
        renderJson(res);
    }

    /**
     * 登出
     * @Param userName 用户名
     */
    @ActionKey("/users/logout")
    public void userLogout(){
        String userName = getPara("userName");
        if(!checkLoginUser(userName))   return ;
        removeSessionAttr("user");
        res.put("code", "00");
        res.put("msg", "登出成功");
        res.put("data", null);
        renderJson(res);
    }

    /**
     * 注册
     * @Param user  用户信息
     */
    @ActionKey("/users/register")
    public void userRegister(){
        Users user = getModel(Users.class);
        if(Users.findByUserName(user.getStr("userName")) != null){
            res.put("code", "01");
            res.put("msg", "用户名已存在");
            res.put("data", null);
            renderJson(res);
            return ;
        }
        if(user.getStr("pwd") == null){
            user.set("password", "e10adc3949ba59abbe56e057f20f883e");
        }
        Users loginUser = getSessionAttr("user");
        if(loginUser != null){
            user.set("createBy", loginUser.get("userName"));
        }
        Users temp = Users.save(user);
        if(temp == null){
            res.put("code", "01");
            res.put("msg", "注册用户失败");
            res.put("data", null);
        }else {
            res.put("code", "00");
            res.put("msg", "注册成功");
            res.put("data", temp);
        }
        renderJson(res);
    }

    /**
     * 跟新用户基本信息
     * @Param user 用户
     */
    @ActionKey("/users/updateInfo")
    public void userUpdateInfo(){
        Users user = getModel(Users.class);
        if(!checkLoginUser(user.getStr("userName")))   return ;
        Users temp = Users.findByUserName(user.getStr("userName"));
        if(temp == null){
            res.put("code", "01");
            res.put("msg", "用户不存在");
            res.put("data", null);
            renderJson(res);
            return ;
        }
        user.set("id", temp.get("id"));
        temp = Users.update(user);
        if(temp == null){
            res.put("code", "01");
            res.put("msg", "更新失败");
            res.put("data", null);
        }else{
            res.put("code", "00");
            res.put("msg", "更新成功");
            res.put("data", temp);
        }
        renderJson(res);
    }

    /**
     * 更新密码
     * @Param userName 用户名
     * @Param oldPwd 旧密码
     * @Param pwd 密码
     */
    @ActionKey("/users/updatePwd")
    public void userUpdatePwd(){
        String userName = getPara("userName");
        if(!checkLoginUser(userName))   return ;
        String oldPwd = getPara("oldPwd");
        String pwd = getPara("pwd");
        Users user = Users.findByUserName(userName);
        if(user == null || !user.get("pwd").equals(oldPwd)){
            res.put("code", "01");
            res.put("msg", "密码错误或者用户不存在");
            res.put("data", null);
            renderJson(res);
            return ;
        }
        user.set("pwd", pwd);
        user = Users.update(user);
        if(user == null){
            res.put("code", "01");
            res.put("msg", "更新密码失败");
            res.put("data", null);
        }else{
            res.put("code", "00");
            res.put("msg", "更新密码成功");
            res.put("data", user);
        }
        renderJson(res);
    }

    /**
     * 获取用户系统权限
     * @Param username 用户名
     */
    @ActionKey("/users/systems")
    public void userSystems(){
        String userName = getPara("userName");
        if(!checkLoginUser(userName))   return ;
        Users user = Users.findByUserName(userName);
        res.put("code", "00");
        res.put("msg", "获取用户系统权限成功");
        res.put("data", Users.getUserSystems(user.getStr("id")));
        renderJson(res);
    }

    /**
     * 根据登录用户查看是否有权限管理用户
     */
    @ActionKey("/users/manageUsers")
    public void userManageUsers(){
        
    }

    /**
     * 查看登录用户与操作用户是否匹配
     * @Param userName
     * @return
     */
    private boolean checkLoginUser(String userName){
        Users user = Users.findByUserName(userName);
        Users loginUser = getSessionAttr("user");
        if(user.get("createBy").equals(loginUser.getStr("userName")))   return true;
        if(!userName.equals(loginUser.getStr("userName"))){
            res.put("code", "02");
            res.put("msg", "无权限访问");
            res.put("data", null);
            renderJson(res);
            return false;
        }
        return true;
    }
}
