package com.hzhwck.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class ResponseUtil {

    private static Map<String, Object> res = new HashMap<String, Object>();

    public static Map<String, Object> setRes(String code, String msg, Object data){
        res.put("code", code);
        res.put("msg", msg);
        res.put("data", data);
        return res;
    }
}
