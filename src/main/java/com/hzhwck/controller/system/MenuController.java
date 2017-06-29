package com.hzhwck.controller.system;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.system.Menu;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;

import java.util.*;

/**
 * Created by 陈忠意 on 2017/6/8.
 */
public class MenuController extends BaseController{

    @ActionKey("/api/system/menus")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            //forwardAction("/api/system/menus/add");
           // success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                //forwardAction("/api/system/menus/id/" + id);
                //success = true;
            }else {
                System.out.println("[GET] show list");
                forwardAction("/api/system/menus/search");
                success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                //forwardAction("/api/system/menus/modify/" + id);
                //success = true;
            }
        }else if(getRequest().getMethod().equals("DELETE")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[DELETE] id -- " + id);
                //success = true;
            }
        }
        if(success == false){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("02", "url出错或者请求方法出错", null));
            }
        }
    }

    /**
     * 根据登录用户角色，返回系统菜单
     */
    @Before(GET.class)
    @ActionKey("/api/system/menus/search")
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
        sort(res);
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "获取系统菜单成功", res));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "获取系统菜单成功", res));
        }
    }

    private void sort(Map<String, Object> root){
        if(root.containsKey("child")){
            List<Map<String, Object>> child = (List<Map<String, Object>>)root.get("child");
            Collections.sort(child, new Comparator<Map<String, Object>>() {
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    int x1 = Integer.parseInt(o1.get("sort").toString());
                    int x2 = Integer.parseInt(o2.get("sort").toString());
                    if(x1 < x2) return -1;
                    if(x1 > x2) return 1;
                    return 0;
                }
            });
            for(Map<String, Object> temp : child){
                sort(temp);
            }
        }
    }
}
