package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.model.hwc.Area;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.system.Account;
import com.hzhwck.myEnum.CodeType;
import com.hzhwck.myEnum.HwcUserType;
import com.hzhwck.myEnum.Status;
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
            //forwardAction("/api/hwc/users/register");
            redirect("/api/hwc/users/register" + "?" + getParam());
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
        }else if(getRequest().getMethod().equals("PATCH")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                redirect("/api/hwc/users/modify/" + id + "?" + getParam());
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
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
            }
        }
    }

    /**
     * 企业用户修改密码
     */
    @ActionKey("/api/hwc/users/pwdmodify")
    public void chancePwd(){
        if(!getRequest().getMethod().equals("POST")){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.paramError, "url出错或者请求方法出错", null));
            }
            return ;
        }
        String oldPwd = getPara("oldPwd");
        String pwd = getPara("pwd");
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("pwd").toString().equals(oldPwd)){
            Account account = Account.dao.findById(loginUser.get("id"));
            account.set("pwd", pwd);
            if(account.update() == false){
                if(getPara("callback") != null) {
                    String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改密码失败，数据库异常", null));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                } else {
                    renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "修改密码失败，数据库异常", null));
                }
            }else {
                loginUser.put("pwd", pwd);
                if(getPara("callback") != null) {
                    String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "修改密码失败，数据库异常", buildUserMap(account.getStr("userName"), 1)));
                    renderJson(getPara("callback", "default") + "(" + json + ")");
                } else {
                    renderJson(ResponseUtil.setRes(CodeType.success, "修改密码失败，数据库异常", buildUserMap(account.getStr("userName"), 1)));
                }
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.paramError, "旧密码错误", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.paramError, "旧密码错误", null));
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
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "根据id查询海外仓用户信息成功", buildUserMap(account.getStr("userName"), 1)));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "根据id查询海外仓用户信息成功", buildUserMap(account.getStr("userName"), 1)));
        }
    }
    /**
     * 添加区县管理员
     * @Param userName required 用户名
     * @Param pwd required 密码
     * @Param realName 真实姓名
     * @Param areaCode 所属区县代码，评审员用户必须
     * @Param areaLevel 所属区县等级，评审员用户必须
     */
    @Before(UserValidator.class)
    @ActionKey("/api/hwc/users/register")
    public void addUser(){
        boolean success = Db.tx(new IAtom() {
            public boolean run() throws SQLException {
                User user = new User();
                user.set("type", getPara("type"));
                user.set("realName", getPara("realName"));
                user.set("areaCode", getPara("areaCode"));
                user.set("yqCode", getPara("yqCode"));
                if(getPara("areaCode") != null)
                    user.set("areaLevel", Area.findByAreaCode(getPara("areaCode")).get("level"));
                user.set("accountId", -1);
                user = User.add(user);
                if(user == null)    return false;
                Account account = new Account();
                account.set("user", "hwc_users:" + user.get("id"));
                account.set("userName", getPara("userName"));
                account.set("pwd", getPara("pwd"));
                account.set("createAt", TimeUtil.getNowTime());
                Map<String, Object> loginUser = getSessionAttr("user");
                account.set("createBy", loginUser.get("id"));
                account = Account.add(account);
                if(account == null) return false;
                user.set("accountId", account.get("id"));
                if(user.update() == false)   return false;
                return true;
            }
        });
        if(success){
            //setSessionAttr("user", buildUserMap(getPara("userName"), 2));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "添加区县管理员成功", buildUserMap(getPara("userName"), 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "添加区县管理员成功", buildUserMap(getPara("userName"), 1)));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加区县管理员失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "添加区县管理员失败，数据库异常", null));
            }
        }
    }

    /**
     * 修改用户信息
     * @Param userName 用户名
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
                Account account = new Account()
                        .set("id", getPara(0));
                Integer status = getParaToInt("status");
                boolean flag = false;
                if(status != null)  {
                    account.set("status", status);flag = true;
                }
                String pwd = getPara("pwd");
                if(pwd != null){
                    account.set("pwd", pwd);flag = true;
                }
                if(flag == true && Account.modify(account) == null) return false;
                account = Account.dao.findById(getPara(0));
                User user = new User()
                        .set("id", account.getStr("user").split(":")[1]);
                String realName = getPara("realName");
                flag = false;
                if(realName != null)    {
                    user.set("realName", realName);flag = true;
                }
                String areaCode = getPara("areaCode");
                if(areaCode != null)    {
                    user.set("areaCode", areaCode);
                    user.set("areaLevel", Area.findByAreaCode(getPara("areaCode")).get("level"));
                    flag = true;
                }
                String yqCode = getPara("yqCode");
                if(yqCode != null){
                    user.set("yqCode", yqCode);
                    flag = true;
                }
                if(flag == true && User.modify(user) == null)   return false;
                return true;
            }
        });
        Map<String, Object> loginUser = getSessionAttr("user");
        if(loginUser.get("id").toString().equals(getPara(0).toString())){
            setSessionAttr("user", buildUserMap(loginUser.get("userName").toString(), 2));
        }
        if(success){
            Account account = Account.dao.findById(getPara(0));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "海外仓用户更新成功", buildUserMap(account.getStr("userName"), 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "海外仓用户更新成功", buildUserMap(account.getStr("userName"), 1)));
            }
        }else{
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "海外仓用户更新失败，数据库异常", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.dataBaseError, "海外仓用户更新失败，数据库异常", null));
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
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "海外仓用户删除成功", "success"));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "海外仓用户删除成功", "success"));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.dataBaseError, "海外仓用户删除失败，数据库异常", "fail"));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson( ResponseUtil.setRes(CodeType.dataBaseError, "海外仓用户删除失败，数据库异常", "fail"));
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
        if(account.getInt("status") == Status.error){
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.paramError, "海外仓用户登录失败，账号被冻结，请联系管理员", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.paramError, "海外仓用户登录失败，账号被冻结，请联系管理员", null));
            }
            return ;
        }
        if(account.getStr("pwd").equals(pwd)){
            account.set("lastLogin", TimeUtil.getNowTime());
            Account.modify(account);
            Map<String, Object> user = buildUserMap(userName, 2);
            setSessionAttr("user", user);
            System.out.println(getSessionAttr("user"));
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "海外仓用户登录成功", buildUserMap(userName, 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "海外仓用户登录成功", buildUserMap(userName, 1)));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.paramError, "海外仓用户登录失败，用户名和密码不匹配", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.paramError, "海外仓用户登录失败，用户名和密码不匹配", null));
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
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "海外仓用户登出成功", "success"));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "海外仓用户登出成功", "success"));
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
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.notLogin, "用户未登录", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.notLogin, "用户未登录", null));
            }
        }else {
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "用户已登录", buildUserMap((String)loginUser.get("userName"), 1)));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "用户已登录", buildUserMap((String)loginUser.get("userName"), 1)));
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
        String order = getPara("order", "asc");

        //系统账号过滤
        String sa = TableNames.systemAccount.split(" ")[1] + ".";
        String hu = TableNames.hwcUsers.split(" ")[1] + ".";
        String hs = TableNames.hwcSamples.split(" ")[1] + ".";
        String orderBy = getPara("oderBy", hu + "id");
        String filter = " where " + sa + "id = " + hu + "accountId ";
        Map<String, Object> loginUser = getSessionAttr("user");

        String sampleFilter = "";
        String jsdwmc = getPara("jsdwmc");
        if(jsdwmc != null){
            sampleFilter = " and " + hs + "jsdwmc like '%" + jsdwmc + "%' ";
        }
        String shtyxydm = getPara("shtyxydm");
        if(shtyxydm != null){
            sampleFilter += " and " + hs + "shtyxydm like '%" + shtyxydm + "%' ";
        }
        if(loginUser.get("type").toString().equals(HwcUserType.sample)){
            filter += " and " + sa + "id = " + loginUser.get("accountId") + " ";
        }else if(loginUser.get("type").toString().equals(HwcUserType.qxAdmin)){
            String accountId = loginUser.get("id").toString();
            String yqCode = getPara("yqCode");
            if(yqCode == null) {
                if(sampleFilter.equals("")) {
                    filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode = '" + loginUser.get("areaCode") + "' and " + hu + "sampleId is null) or (" + hu + "areaCode is null and yqCode is null " +
                            "and " + hu + "sampleId = " + hs + "id and " + hs + "ssqx = '" + loginUser.get("areaCode") + "')) ";
                }else {
                    filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode is null and yqCode is null " +
                            "and " + hu + "sampleId = " + hs + "id and " + hs + "ssqx = '" + loginUser.get("areaCode") + "'" + sampleFilter + ")) ";
                }
            }else {
                if(sampleFilter.equals("")) {
                    filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode = '" + loginUser.get("areaCode") + "' and " + hu + "sampleId is null) or (" + hu + "areaCode is null and yqCode is null " +
                            "and " + hu + "sampleId = " + hs + "id and " + hs + "ssqx = '" + loginUser.get("areaCode") + "' and ssyq = '" + yqCode + "' )) ";
                }else {
                    filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode is null and yqCode is null " +
                            "and " + hu + "sampleId = " + hs + "id and " + hs + "ssqx = '" + loginUser.get("areaCode") + "' and ssyq = '" + yqCode + "' " + sampleFilter + ")) ";
                }
            }
        }else if(loginUser.get("type").toString().equals(HwcUserType.admin)){
            String areaCode = getPara("areaCode");
            String accountId = loginUser.get("id").toString();
            String yqCode = getPara("yqCode");
            if(areaCode != null){
                if(yqCode == null) {
                    if(sampleFilter.equals("")) {
                        filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode = '" + areaCode + "' and " + hu + "sampleId is null) or (" + hu + "areaCode is null and yqCode is null " +
                                "and " + hu + "sampleId = " + hs + "id and " + hs + "ssqx = '" + areaCode + "')) ";
                    }else {
                        filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode is null and yqCode is null " +
                                "and " + hu + "sampleId = " + hs + "id and " + hs + "ssqx = '" + areaCode + "'" + sampleFilter + ")) ";
                    }
                }else {
                    if(sampleFilter.equals("")) {
                        filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode = '" + areaCode + "' and " + hu + "sampleId is null) or (" + hu + "areaCode is null and yqCode is null " +
                                "and " + hu + "sampleId = " + hs + "id and " + hs + "ssqx = '" + areaCode + "' and ssyq = '" + yqCode + "' )) ";
                    }else {
                        filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode is null and yqCode is null " +
                                "and " + hu + "sampleId = " + hs + "id and " + hs + "ssqx = '" + areaCode + "' and ssyq = '" + yqCode + "' " + sampleFilter + ")) ";
                    }
                }
            }
            if(areaCode == null && yqCode != null) {
                if(sampleFilter.equals("")) {
                    filter += " and " + sa + "id != " + accountId + " and ((" + hu + "yqCode = '" + yqCode + "' and " + hu + "sampleId is null) or (" + hu + "areaCode is null and yqCode is null " +
                            "and " + hu + "sampleId = " + hs + "id and " + hs + "ssyq = '" + yqCode + "')) ";
                }else {
                    filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode is null and yqCode is null " +
                            "and " + hu + "sampleId = " + hs + "id and " + hs + "ssyq = '" + yqCode + "' " + sampleFilter + ")) ";
                }
            }
            if(areaCode == null && yqCode == null){
                if(!sampleFilter.equals("")){
                    filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode is null and yqCode is null " +
                            "and " + hu + "sampleId = " + hs + "id " + sampleFilter + ")) ";
                }
            }
        }else if(loginUser.get("type").toString().equals(HwcUserType.yqadmin)){
            String accountId = loginUser.get("id").toString();
            if(sampleFilter.equals("")) {
                filter += " and " + sa + "id != " + accountId + " and ((" + hu + "yqCode = '" + loginUser.get("yqCode") + "' and " + hu + "sampleId is null) or (" + hu + "areaCode is null and yqCode is null " +
                        "and " + hu + "sampleId = " + hs + "id and " + hs + "ssyq = '" + loginUser.get("yqCode") + "')) ";
            }else {
                filter += " and " + sa + "id != " + accountId + " and ((" + hu + "areaCode is null and yqCode is null " +
                        "and " + hu + "sampleId = " + hs + "id and " + hs + "ssyq = '" + loginUser.get("yqCode") + "'" + sampleFilter + ")) ";
            }
        }else {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("pageNumber", pageNumber);
            data.put("pageSize", pageSize);
            data.put("totalRow", 0);
            data.put("totalPage", 0);
            data.put("fistPage", true);
            data.put("lastPage", true);
            List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
            data.put("list", list);
            if(getPara("callback") != null) {
                String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "获取用户分页信息成功", data));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            } else {
                renderJson(ResponseUtil.setRes(CodeType.success, "获取用户分页信息成功", data));
            }
            return ;
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
        String type = getPara("type");
        if(type != null){
            filter += " and " + hu + "type = " + type + " ";
        }

        Page<User> users = User.getPageUsers(pageNumber, pageSize, orderBy, order, filter, ", " + TableNames.hwcSamples + ", " + TableNames.systemAccount);
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
            String json = JsonKit.toJson(ResponseUtil.setRes(CodeType.success, "获取用户分页信息成功", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        } else {
            renderJson(ResponseUtil.setRes(CodeType.success, "获取用户分页信息成功", data));
        }

    }


}
