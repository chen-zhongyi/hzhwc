package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Country;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.kit.JsonKit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 陈忠意 on 2017/7/1.
 */
public class CountryController extends BaseController{

    //添加国家的脚本
    private void index(){

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:\\Users\\陈忠意\\Downloads\\country.txt"), "UTF-8"));
            String line ;
            while((line = br.readLine()) != null){
                if(line.trim().equals("end"))  break;
                System.out.println(Arrays.toString(line.trim().split(" ")));
                int firstC = -1;
                String country = "";
                for(int i = 0;i < line.length();++i)if(line.charAt(i) >= 'A' && line.charAt(i) <= 'Z'){
                    firstC = i;
                    for(;i < line.length();++i)if((line.charAt(i) >= 'A' && line.charAt(i) <= 'Z') || (line.charAt(i) >= 'a' && line.charAt(i) <= 'z')){
                        continue;
                    }else
                        break;
                    country = line.substring(firstC, i);
                    break;
                }
                //ystem.out.println("firstC = " + firstC);
                String countryName = "";
                if(firstC != -1)    countryName = line.substring(0, firstC);
                System.out.println(countryName + " --> " + country);
                Country c = new Country()
                        .set("name", countryName)
                        .set("contientCode", "Africa")
                        .set("code", country);
                c.save();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        renderJson("{\"id\":\"hello world !\"}");
    }

    /**
     * 根据获取国家
     */
    @Before(GET.class)
    @ActionKey("/api/hwc/countrys")
    public void getCountry(){
        List<Country> data = Country.getAll();
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "根据获取国家", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "根据获取国家", data));
        }
    }
}
