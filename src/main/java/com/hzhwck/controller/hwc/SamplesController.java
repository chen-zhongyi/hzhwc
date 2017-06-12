package com.hzhwck.controller.hwc;

import com.hzhwck.controller.BaseController;
import com.hzhwck.model.hwc.Samples;
import com.hzhwck.model.hwc.User;
import com.hzhwck.util.ResponseUtil;
import com.jfinal.core.ActionKey;

import java.util.Map;

/**
 * Created by 陈忠意 on 2017/6/12.
 */
public class SamplesController extends BaseController {

    /**
     * 添加样本企业信息
     */
    @ActionKey("/api/hwc/samples/add")
    public void add(){
        Samples samples = getModel(Samples.class, "sample");
        if(samples.save() == false){
            ResponseUtil.setRes("01", "添加海外仓样本企业失败，数据库异常", null);
            return ;
        }
        Map<String, Object> loginUser = getSessionAttr("user");
        User user = User.dao.findById(loginUser.get("hwcUserId"));
        user.set("sampleId", samples.get("id"));
        if(User.modify(user) == null){
            ResponseUtil.setRes("01", "添加海外仓样本企业失败，数据库异常", null);
            samples.delete();
            return ;
        }
        loginUser.put("sample", samples);
        renderJson(ResponseUtil.setRes("00", "添加海外仓样本企业成功", samples));
    }

    /**
     * 修改样本企业信息
     */
    @ActionKey("/api/hwc/samples/modify")
    public void modify(){
        Map<String, Object> loginUser = getSessionAttr("user");
        Samples samples = getModel(Samples.class, "sample");
        String id = ((Samples)loginUser.get("sample")).get("id").toString();
        samples.set("id", id);
        if(samples.update() == false){
            ResponseUtil.setRes("01", "更新海外仓样本企业失败，数据库异常", null);
            return ;
        }
        loginUser.put("sample", samples);
        renderJson(ResponseUtil.setRes("00", "更新海外仓样本企业成功", samples));
    }

    /**
     * 分页查询样本企业信息
     * @Param pageNumber 第几页,默认值1
     * @Param pageSize 分页大小，默认值20
     * @Param orderBy 排序字段，默认id
     * @Param oder desc, asc
     * @Param filter 过滤信息，字符串【过滤字段:过滤信息,过滤字段:过滤信息..】,自动过滤不合法字段
     */
    @ActionKey("/api/hwc/samples/search")
    public void getPage(){
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 20);
        String oderBy = getPara("oderBy", "id");
        String oder = getPara("oder", "asc");
        String filter = getPara("filter", "");
        renderJson(ResponseUtil.setRes("00", "获取海外仓企业信息成功",
                Samples.getPage(pageNumber, pageSize, oderBy, oder, filter)));
    }
}
