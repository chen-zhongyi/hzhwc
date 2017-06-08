package com.hzhwck.validator.hwc;


import com.hzhwck.model.system.Account;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class UserValidator extends Validator {

    protected void validate(Controller c){
        if(getActionKey().equals("/api/hwc/users/register")){
            validateString("userName", 4, 20, "userName", "用户名长度在4-20个字符之间");
            if(c.getAttr("userName") == null){
                if(Account.findByUserName(c.getPara("userName")) != null){
                    addError("userName", "用户名已存在");
                }
            }
            validateRequiredString("pwd", "pwd", "密码必须填写");
            validateInteger("role", 0, 1, "role", "角色出错");
            if(c.getPara("role") != null && c.getPara("role").equals("0")){ //评审员用户
                if(c.getPara("areaCode") == null){
                    addError("areaCode", "区县所属代码必须填写");
                }else{
                    //验证areaCode在数据库中是否存在
                }
                if(c.getPara("areaLevel") == null){
                    addError("areaLevel", "所属区县等级必须填写");
                }else{
                    //验证areaLevel是否正确
                }
            }
            validateString("realName", 2, 10, "realName", "姓名长度在2-10个字符之间");
        }else if(getActionKey().equals("/api/hwc/users/modify")){
            String userName = c.getPara("userName");
            if(userName != null){
                if(Account.findByUserName(userName) == null)    addError("userName", "用户名不存在");
            }else
                addError("uerName","用户名不能为空");
            if(c.getPara("role") != null) {
                validateInteger("role", 0, 1, "role", "角色出错");
            }
            if(c.getPara("status") != null){
                validateInteger("status", 0, 1, "role", "用户状态码出错");
            }
            if(c.getPara("realName") != null){
                validateString("realName", 2, 10, "realName", "姓名长度在2-10个字符之间");
            }
            if(c.getPara("areaLevel") != null && c.getPara("areaName") != null){
                //验证areaLevel是否正确
            }
        }else if(getActionKey().equals("/api/hwc/users/delete")){
            String userName = c.getPara("userName");
            if(userName != null){
                if(Account.findByUserName(userName) == null)    addError("userName", "用户名不存在");
            }else
                addError("uerName","用户名不能为空");
        }else if(getActionKey().equals("/api/hwc/users/login")){
            String userName = c.getPara("userName");
            if(userName != null){
                if(Account.findByUserName(userName) == null)    addError("userName", "用户名不存在");
            }else
                addError("uerName","用户名不能为空");
            String pwd = c.getPara("pwd");
            if(pwd == null)
                addError("pwd","密码不能为空");
        }else if(getActionKey().equals("/api/hwc/users/logout")){
            if(!((Map<String, Object>)c.getSessionAttr("user")).get("userName").equals(c.getPara("userName"))){
                addError("userName", "用户名出错");
            }
        }else if(getActionKey().equals("/api/hwc/users/search")){
            validateInteger("pageNumber", "pageNumber", "第几页必须为数字");
            validateInteger("pageSize", "pageSize", "分页大小必须为数字");
            if(c.getPara("oder") != null ){
                if(!c.getPara("oder").equals("desc") && !c.getPara("oder").equals("asc")){
                    addError("oder", "为desc或asc");
                }
            }
        }
    }

    protected void handleError(Controller c){
        Map<String, Object> data = new HashMap<String, Object>();
        while(c.getAttrNames().hasMoreElements()){
            String temp = c.getAttrNames().nextElement();
            data.put(temp, c.getAttr(temp));
        }
        c.renderJson(ResponseUtil.setRes("02", "参数出错", data));
    }
}
