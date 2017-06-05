package com.hzhwck.controller;

import com.hzhwck.model.Systems;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/5/23.
 */
public class SystemsController extends Controller {

    private static Map<String, Object> res = new HashMap<String, Object>();

    /**
     * 获取系统管理
     */
    @ActionKey("/systems")
    public void systems(){
        res.put("code", "00");
        res.put("msg", "获取系统管理成功");
        res.put("data", Systems.getSystems());
        renderJson(res);
    }
}
