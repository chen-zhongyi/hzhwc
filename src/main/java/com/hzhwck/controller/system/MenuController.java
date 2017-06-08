package com.hzhwck.controller.system;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.system.Menu;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class MenuController extends BaseController{

    /**
     * 根据登录用户角色，返回系统菜单
     */
    @ActionKey("/api/system/menus")
    public void getMenus(){
        Map<String, Object> user = (Map<String, Object>)getSessionAttr("user");
        Map<String, Object> role = (Map<String, Object>)user.get("role");
        if(((Integer)role.get("status")) == 0){
            renderJson(ResponseUtil.setRes("03", "该用户角色已经停用，请联系管理员", role));
            return ;
        }
        String[] menus = ((String)role.get("resou")).split(",");
        List<Menu> ms = new LinkedList<Menu>();
        for(int i = 0;i < menus.length;++i){
            Menu m = Menu.dao.findById(menus[i].split(":")[0]);
            if(m.getInt("status") == 0) continue;
            ms.add(m);
        }
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("id", 0);
        res.put("name", "根节点");
        List<Map<String, Object>> queue = new LinkedList<Map<String, Object>>();
        queue.add(res);
        while(true){
            Map<String, Object> temp = queue.remove(0);
            for(Menu m : ms){
                if(((Integer)temp.get("id")) == m.getInt("pid")){
                    if(!temp.containsKey("child")){
                        temp.put("child", new LinkedList<Object>());
                    }
                    Map<String, Object> x = m.toRecord().getColumns();
                    ((List)temp.get("child")).add(x);
                    queue.add(x);
                }
            }
            if(queue.size() == 0)   break;
        }
        renderJson(ResponseUtil.setRes("00", "获取系统菜单成功", res));
    }
}
