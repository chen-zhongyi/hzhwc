package com.hzhwck.controller.hwc;

import com.hzhwck.util.ResponseUtil;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 陈忠意 on 2017/7/11.
 */
@WebServlet("/api/simpleFileupload")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("文件上传开始");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //1、创建一个DiskFileItemFactory工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //2、创建一个文件上传解析器
        ServletFileUpload upload = new ServletFileUpload(factory);
        //解决上传文件名的中文乱码
        upload.setHeaderEncoding("UTF-8");
        factory.setSizeThreshold(1024 * 500);//设置内存的临界值为500K
        String path = PathKit.getWebRootPath() + File.separator + "hwckimages";
        File linshi = new File(path);//当超过500K的时候，存到一个临时文件夹中
        factory.setRepository(linshi);
        upload.setSizeMax(1024 * 1024 * 550);//设置上传的文件总的大小不能超过5M
        try {
            // 1. 得到 FileItem 的集合 items
            List<FileItem> /* FileItem */items = upload.parseRequest(request);


            Map<String, String> data = new HashMap<String, String>();

            // 2. 遍历 items:
            for (FileItem item : items) {
                // 若是一个一般的表单域, 打印信息
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString("utf-8");

                    System.out.println(name + ": " + value);


                }
                // 若是文件域则把文件保存到 e:\\files 目录下.
                else {
                    String fileName = item.getName();
                    long sizeInBytes = item.getSize();
                    System.out.println(fileName);
                    System.out.println(sizeInBytes);

                    InputStream in = item.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
                    Date date = new Date(System.currentTimeMillis());

                    String name = sf.format(date) + fileName;
                    fileName = path + File.separator + name;//文件最终上传的位置
                    System.out.println(fileName);
                    OutputStream out = new FileOutputStream(fileName);

                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }

                    data.put("path", "/hwckimages/" + name);

                    out.close();
                    in.close();
                }
            }

            String json = JsonKit.toJson(ResponseUtil.setRes("00", "文件上传成功", data));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(json);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }

    }
}
