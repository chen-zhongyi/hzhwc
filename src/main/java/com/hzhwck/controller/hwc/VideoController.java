package com.hzhwck.controller.hwc;

import com.google.common.io.Files;
import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/7/7.
 */
@Clear(LoginInterceptor.class)
public class VideoController extends BaseController {

    /**
     * 图片上传
     */
    @Before(POST.class)
    @ActionKey("/api/hwc/videos")
    public void add(){
        UploadFile image = getFile("video");
        String imageName = image.getFileName();
        String suffix = imageName.substring(imageName.lastIndexOf('.')).toLowerCase();
        File tempFile = new File(image.getUploadPath() + File.separator + image.getFileName());
        System.out.println("suffix" + suffix);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String name = "hwc-videos-" + s.format(new Date()) + suffix;
        File file = new File(PathKit.getWebRootPath() + File.separator + "videos" + File.separator + name);
        try{
            Files.copy(tempFile, file);
            tempFile.delete();
        }catch (IOException e){
            e.printStackTrace();
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("06", "文件上传出错", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("06", "文件上传出错", null));
            }
            return;
        }
        Map<String, String> data = new HashMap<String, String>();
        data.put("path", "/videos/" + name);
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "文件上传成功", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "文件上传成功", data));
        }
    }
}
