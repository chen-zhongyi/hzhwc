package com.hzhwck.validator.hwc;

import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class TableColsValidator extends Validator{
    protected void validate(Controller c){
        if(getActionKey().equals("/api/hwc/tablecols")){
            validateInteger("tableId", 1, 3, "tableId", "tableId只能取{1,2,3}" );
        }
    }

    protected void handleError(Controller c){
        Map<String, Object> data = new HashMap<String, Object>();
        Enumeration<String> enumeration = c.getAttrNames();
        while(enumeration.hasMoreElements()){
            String temp = enumeration.nextElement();
            data.put(temp, c.getAttr(temp));
        }
        c.renderJson(ResponseUtil.setRes("02", "参数出错", data));
    }
}
