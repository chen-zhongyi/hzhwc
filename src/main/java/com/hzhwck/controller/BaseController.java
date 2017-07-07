package com.hzhwck.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.User;
import com.hzhwck.model.system.Account;
import com.hzhwck.model.system.UserSystem;
import com.jfinal.core.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/5.
 */
public class BaseController extends Controller {

    /**
     * 用户信息整合
     * @param userName 用户名
     * @param type 整合类型 type=1表示返回给客户端用户信息，type=2表示存在服务端session中的用户信息
     * @return 返回用户信息
     */
    public Map<String, Object> buildUserMap(String userName, int type){
        Map<String, Object> map = new HashMap<String, Object>();
        Account account = Account.findByUserName(userName);
        map.put("id", account.get("id"));
        map.put("userName", account.get("userName"));
        map.put("status", account.get("status"));
        //map.put("role", Role.dao.findById(account.get("role")));
        User user = User.dao.findById(account.getStr("user").split(":")[1]);
        map.put("realName", user.get("realName"));
        map.put("areaLevel", user.get("areaLevel"));
        map.put("areaCode", user.get("areaCode"));
        map.put("sample", Samples.dao.findById(user.get("sampleId")));
        map.put("warehouseIds", user.getStr("warehouseIds"));
        map.put("type", user.get("type")); // 2企业用户， 0市级管理员，1县管理员
        List<UserSystem> userSystems = UserSystem.getByAccountId(account.get("id").toString());
        Map<String, String> temp = new HashMap<String, String>();
        for(UserSystem userSystem : userSystems){
            temp.put(userSystem.getStr("system"), userSystem.getStr("right"));
        }
        map.put("usersystem", temp);
        if(type == 2){
            map.put("accountId", account.get("id"));
            map.put("hwcUserId", user.get("id"));
            map.put("pwd", account.get("pwd"));
            //Role role = Role.dao.findById(account.get("role"));
            //map.put("role", role.toRecord().getColumns());
        }
        return map;
    }

    public String getParam(){
        HttpServletRequest request = getRequest();
        StringBuffer str = new StringBuffer("");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String line ;
            while((line = br.readLine()) != null){
                str.append(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(" iiiiii --> " + str.toString());
        if(str.length() > 0) {
            try {
                Map<String, String> map = new Gson().fromJson(str.toString(), new TypeToken<Map<String, String>>() {}.getType());
                str = new StringBuffer("");
                System.out.println(map.toString());
                for (String temp : map.keySet()) {
                    if(map.get(temp) == null) continue;
                    str.append(temp + "=" + Utf8URLencode(map.get(temp)) + "&");
                }
                int len = str.toString().length();
                str = new StringBuffer(str.toString().substring(0, len - 1));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("str --> " + str.toString());
        return str.toString();
    }

    private String getUTF(String str){
        String res = "";
        try {
            res = new String(str.getBytes("UTF-8"));
            res = URLEncoder.encode(res, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
    public String Utf8URLencode(String text) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 0 && c <= 255) {
                result.append(c);
            }else {
                byte[] b = new byte[0];
                try {
                    b = Character.toString(c).getBytes("UTF-8");
                }catch (Exception ex) {
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    result.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return result.toString();
    }

}
