package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.system.Account;
import com.hzhwck.model.system.Role;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.myEnum.TableNames;
import com.hzhwck.util.ResponseUtil;
import com.hzhwck.util.TimeUtil;
import com.hzhwck.validator.hwc.UserValidator;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class UserController extends BaseController {

    @Clear(LoginInterceptor.class)
    @ActionKey("/api/hwc/users")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            forwardAction("/api/hwc/users/register");
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                forwardAction("/api/hwc/users/id/" + id);
                success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/hwc/users/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                forwardAction("/api/hwc/users/modify/" + id);
                success = true;
            }
        }else if(getRequest().getMethod().equals("DELETE")){
            String userName = getPara(0);
            if(userName != null){
                System.out.println("[DELETE] userName -- " + userName);
                forwardAction("/api/hwc/users/delete/" + userName);
                success = true;
            }
        }
        if(success == false){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
            }
        }
    }
    /**
     * 根据id查询海外仓用户信息
     */
    @ActionKey("/api/hwc/users/id")
    public void getUserById(){
        String id = getPara(0);
        Account account = Account.dao.findById(id);
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "根据id查询海外仓用户信息成功", buildUserMap(account.getStr("userName"), 1)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes("00", "根据id查询海外仓用户信息成功", buildUserMap(account.getStr("userName"), 1)));
        }
    }
    /**
     * 注册用户
     * @Param userName required 用户名
     * @Param pwd required 密码
     * @Param role required 角色
     * @Param realName 真实姓名
     * @Param areaCode 所属区县代码，评审员用户必须
     * @Param areaLevel 所属区县等级，评审员用户必须
     */
    @Clear(LoginInterceptor.class)
    @Before(UserValidator.class)
    @ActionKey("/api/hwc/users/register")
    public void addUser(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                User user = new User();
                user.set("type", 1);
                user.set("realName", getPara("realName"));
                user.set("areaCode", getPara("areaCode"));
                user.set("areaLevel", getPara("areaLevel"));
                user.set("accountId", 1);
                user = User.add(user);
                if(user == null)    return false;
                Account account = new Account();
                account.set("user", "hwc_users:" + user.get("id"));
                account.set("userName", getPara("userName"));
                account.set("pwd", getPara("pwd"));
                account.set("role", 2);
                account.set("createAt", TimeUtil.getNowTime());
                account = Account.add(account);
                if(account == null) return false;
                user.set("accountId", account.get("id"));
                if(User.modify(user) == null)   return false;
                return true;
            }
        });
        if(success){
            setSessionAttr("user", buildUserMap(getPara("userName"), 2));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "注册成功", buildUserMap(getPara("userName"), 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("00", "注册成功", buildUserMap(getPara("userName"), 1)));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "注册失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("01", "注册失败，数据库异常", null));
            }
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
    @Before(UserValidator.class)
    @ActionKey("/api/hwc/users/modify")
    public void update(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                Account account = Account.dao.findById(getPara(0));
                //Integer role = getParaToInt("role");
                //if(role != null)    account.set("role", role);
                Integer status = getParaToInt("status");
                if(status != null)  account.set("status", status);
                account.set("lastLogin", TimeUtil.getNowTime());
                if(Account.modify(account) == null) return false;
                User user = User.dao.findById(account.getStr("user").split(":")[1]);
                boolean flag = false;
                String realName = getPara("realName");
                if(realName != null)    {
                    user.set("realName", realName);flag = true;
                }
                Integer areaLevel = getParaToInt("areaLevel");
                if(areaLevel != null)   {
                    user.set("areaLevel", areaLevel);flag = true;
                }
                String areaCode = getPara("areaCode");
                if(areaCode != null)    {
                    user.set("areaCode", areaCode);flag = true;
                }
                if(flag && User.modify(user) == null)   return false;
                return true;
            }
        });
        if(success){
            Account account = Account.dao.findById(getPara(0));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "海外仓用户更新成功", buildUserMap(account.getStr("userName"), 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("00", "海外仓用户更新成功", buildUserMap(account.getStr("userName"), 1)));
            }
        }else{
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "海外仓用户更新失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("01", "海外仓用户更新失败，数据库异常", null));
            }
        }
    }

    /**
     * 删除用户
     *
     * @Param userName
     *
     * @return
     */
    @Before(UserValidator.class)
    @ActionKey("/api/hwc/users/delete")
    public void delete(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                Account account = Account.findByUserName(getPara(0));
                if(User.delete(account.getStr("user").split(",")[1]) == false)   return false;
                return Account.delete(account.getStr("id"));
            }
        });
        if(success){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "海外仓用户删除成功", "success"));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("00", "海外仓用户删除成功", "success"));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("01", "海外仓用户删除失败，数据库异常", "fail"));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson( ResponseUtil.setRes("01", "海外仓用户删除失败，数据库异常", "fail"));
            }
        }
    }

    /**
     * 登录
     *
     * @Param userName 用户名
     * @Param pwd 密码
     */
    @Before({POST.class, UserValidator.class})
    @Clear(LoginInterceptor.class)
    @ActionKey("/api/hwc/users/login")
    public void login(){
        String userName = getPara("userName");
        String pwd = getPara("pwd");
        Account account = Account.findByUserName(userName);
        if(account.getStr("pwd").equals(pwd)){
            account.set("lastLogin", TimeUtil.getNowTime());
            Account.modify(account);
            Map<String, Object> user = buildUserMap(userName, 2);
            setSessionAttr("user", user);
            System.out.println(getSessionAttr("user"));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "海外仓用户登录成功", buildUserMap(userName, 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("00", "海外仓用户登录成功", buildUserMap(userName, 1)));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "海外仓用户登录失败，用户名和密码不匹配", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("02", "海外仓用户登录失败，用户名和密码不匹配", null));
            }
        }
    }

    /**
     * 用户登出
     * @Param userName
     */
    @Before(UserValidator.class)
    @ActionKey("/api/hwc/users/logout")
    public void Logout(){
        removeSessionAttr("user");
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "海外仓用户登出成功", "success"));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes("00", "海外仓用户登出成功", "success"));
        }
    }

    /**
     * 判断用户是否登录
     */
    @ActionKey("/api/hwc/users/isLogin")
    @Before(GET.class)
    @Clear(LoginInterceptor.class)
    public void isLogin(){
        Map<String, Object> loginUser = getSessionAttr("user");
        System.out.println(loginUser);
        if(loginUser == null){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("04", "用户未登录", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("04", "用户未登录", null));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes("00", "用户已登录", buildUserMap((String)loginUser.get("userName"), 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes("00", "用户已登录", buildUserMap((String)loginUser.get("userName"), 1)));
            }
        }
    }

    /**
     * 用户搜索
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param orderBy 排序字段，默认id
     * @Param oder desc, asc
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】
     */
    @Before(UserValidator.class)
    @ActionKey("/api/hwc/users/search")
    public void getPageUsers(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String oder = getPara("oder", "asc");

        //系统账号过滤
        String sa = TableNames.systemAccount.split(" ")[1] + ".";
        String hu = TableNames.hwcUsers.split(" ")[1] + ".";
        String hs = TableNames.hwcSamples.split(" ")[1] + ".";
        String orderBy = getPara("oderBy", hu + "id");
        String filter = " where " + sa + "id = " + hu + "accountId and (" + hu + "sampleId = " + hs + "id or " + hu + "sampleId is null) ";
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            filter += " and " + sa + "id = " + loginUser.get("accountId") + " ";
        }
        String status = getPara("status");
        if(status != null){
            filter += " and " + sa + "status = " + status + " ";
        }
        String userName = getPara("userName");
        if(userName != null){
            filter += " and " + sa + "userName like '%" + userName + "%' ";
        }

        //用户信息过滤
        String realName = getPara("realName");
        if(realName != null){
            filter += " and " + hu + "realName like '%" + realName + "%' ";
        }
        String areaCode = getPara("areaCode");
        if(areaCode != null){
            filter += " and (" + hu + "areaCode = '" + areaCode + "' or (" + hs + "ssqx = '" + areaCode + "' and "+ hu + "sampleId is not null)) ";
        }
        String type = getPara("type");
        if(type != null){
            filter += " and " + hu + "type = " + type + " ";
        }

        Page<User> users = User.getPageUsers(pageNumber, pageSize, orderBy, oder, filter, ", " + TableNames.hwcSamples + ", " + TableNames.systemAccount);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("pageNumber", users.getPageNumber());
        data.put("pageSize", users.getPageSize());
        data.put("totalRow", users.getTotalRow());
        data.put("totalPage", users.getTotalPage());
        data.put("fistPage", users.isFirstPage());
        data.put("lastPage", users.isLastPage());
        List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
        for(User user : users.getList()){
            list.add(buildUserMap(user.getStr("userName"), 1));
        }
        data.put("list", list);
        if(getPara("callback") != null) {
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取用户分页信息成功", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes("00", "获取用户分页信息成功", data));
        }

    }

    /**
     * 用户信息整合
     * @param userName 用户名
     * @param type 整合类型 type=1表示返回给客户端用户信息，type=2表示存在服务端session中的用户信息
     * @return 返回用户信息
     */
    private Map<String, Object> buildUserMap(String userName, int type){
        Map<String, Object> map = new HashMap<String, Object>();
        Account account = Account.findByUserName(userName);
        map.put("id", account.get("id"));
        map.put("userName", account.get("userName"));
        map.put("status", account.get("status"));
        map.put("role", Role.dao.findById(account.get("role")));
        User user = User.dao.findById(account.getStr("user").split(":")[1]);
        map.put("realName", user.get("realName"));
        map.put("areaLevel", user.get("areaLevel"));
        map.put("areaCode", user.get("areaCode"));
        map.put("sample", Samples.dao.findById(user.get("sampleId")));
        map.put("warehouseIds", user.getStr("warehouseIds"));
        map.put("type", user.get("type")); // 1企业用户， 0评审员用户
        if(type == 2){
            map.put("accountId", account.get("id"));
            map.put("hwcUserId", user.get("id"));
            map.put("pwd", account.get("pwd"));
            Role role = Role.dao.findById(account.get("role"));
            map.put("role", role.toRecord().getColumns());
        }
        return map;
    }
}
