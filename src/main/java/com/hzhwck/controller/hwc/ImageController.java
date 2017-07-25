package com.hzhwck.controller.hwc;

import com.google.common.io.Files;
import com.hzhwck.controller.BaseController;
import com.hzhwck.interceptor.LoginInterceptor;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.aop.Clear;
import com.jfinal.core.ActionKey;
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
 * Created by 陈忠意 on 2017/6/26.
 */
@Clear(LoginInterceptor.class)
public class ImageController extends BaseController{

    @ActionKey("/api/hwc/images")
    public void test(){
        boolean success = false;
        if(getRequest().getMethod().equals("POST")){
            System.out.println("[POST] add");
            forwardAction("/api/hwc/images/add");
            success = true;
        }else if(getRequest().getMethod().equals("GET")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[GET] show id -- " + id);
                //forwardAction("/api/hwc/images/id/" + id);
                //success = true;
            }else {
                System.out.println("[GET] show list");
                //forwardAction("/api/hwc/images/search");
                //success = true;
            }
        }else if(getRequest().getMethod().equals("PUT")){
            String id = getPara(0);
            if(id != null){
                System.out.println("[PUT] update id -- " + id);
                //forwardAction("/api/hwc/images/modify/" + id);
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
     * 图片上传
     */
    @ActionKey("/api/hwc/images/add")
    public void add(){
        UploadFile image = getFile("image");
        //2、创建一个文件上传解析器
        //DiskFileItemFactory factory = new DiskFileItemFactory();
        //2、创建一个文件上传解析器
        //ServletFileUpload upload = new ServletFileUpload(factory);
        /*HttpServletRequest request = getRequest();

        try {
            InputStream in = request.getInputStream();
            OutputStream out = new FileOutputStream(PathKit.getWebRootPath() + File.separator + "hwckimages" +
                File.separator + "image.jpg");
            byte[] a = new byte[1000];
            int len;
            while((len = in.read()) != -1){
                out.write(a, 0, len);
            }
            out.close();
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }*/
        //File image = new File(PathKit.getWebRootPath() + File.separator + "hwcimages" +
        //        File.separator + "image.jpg");
        String imageName = image.getFileName();
        String suffix = imageName.substring(imageName.lastIndexOf('.')).toLowerCase();
        File tempFile = new File(image.getUploadPath() + File.separator + image.getFileName());
        if(!(suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".jpeg") || suffix.equals(".svg") || suffix.equals(".gif"))){
            if(getPara("callback") != null){
                String json = JsonKit.toJson(ResponseUtil.setRes("06", "文件上传出错,只支持.jpg .png .jpeg .svg .gif格式图片", null));
                renderJson(getPara("callback", "default") + "(" + json + ")");
            }else {
                renderJson(ResponseUtil.setRes("06", "文件上传出错,只支持.jpg .png .jpeg .svg .gif格式图片", null));
            }
            tempFile.delete();
            return;
        }
        System.out.println("suffix" + suffix);
        int width = getParaToInt("width", 100);
        int height = getParaToInt("height", 100);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String name = "hwc-images-" + s.format(new Date()) + suffix;
        File file = new File(PathKit.getWebRootPath() + File.separator + "hwckimages" + File.separator + name);
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
        data.put("path", "/hwckimages/" + name);
        if(getPara("callback") != null){
            String json = JsonKit.toJson(ResponseUtil.setRes("00", "文件上传成功", data));
            renderJson(getPara("callback", "default") + "(" + json + ")");
        }else {
            renderJson(ResponseUtil.setRes("00", "文件上传成功", data));
        }
    }
}
