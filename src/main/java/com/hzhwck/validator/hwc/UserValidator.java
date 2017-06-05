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
            validateRequiredString("userName", "userName", "用户名不能为空");
            if(c.getAttr("userName") == null){
                if(Account.findByUserName(c.getPara("userName")) != null){
                    addError("userName", "用户名已存在");
                }
            }
            validateRequiredString("pwd", "pwd", "密码必须填写");
            validateInteger("role", 0, 1, "role", "角色出错");
            if(c.getPara("role").equals("0")){ //评审员用户
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
