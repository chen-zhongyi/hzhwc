package com.hzhwck.model.hwc;

import com.hzhwck.myEnum.TableNames;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Created by 陈忠意 on 2017/6/14.
 */
public class ReportPlans extends Model<ReportPlans> {
    private static final String tableName = TableNames.hwcReportPlans.split(" ")[0];
    private static final String hrp = TableNames.hwcReportPlans.split(" ")[1] + ".";
    public static final ReportPlans dao = new ReportPlans().dao();
    private static final String[] a = {"tableGroupId", "status"};

    public static ReportPlans getReportPlanByName(String name){
        return ReportPlans.dao.findFirst("select * from " + tableName + " where name = " + name);
    }

    public static ReportPlans getReportPlansAndTableGroupsById(String id){
        String tableGroup = TableNames.hwcTableGroups.split(" ")[0];
        String tg = TableNames.hwcTableGroups.split(" ")[1] + "." ;
        return ReportPlans.dao.findFirst("select " + hrp + "*, " + tg + "frequency " +
                        "from " + TableNames.hwcTableGroups + ", " + TableNames.hwcReportPlans + " " +
                        " where " + hrp + "tableGroupId = " + tg + "id " +
                        " and " + hrp + "id = " + id);
    }

    public static Page<ReportPlans> getPage(int pageNumber, int pageSize, String oderBy, String oder, String filter, String tables){
        StringBuffer sql = new StringBuffer("from " + tables + " ");
        sql.append(filter);
        sql.append(" order by " + oderBy + " " + oder);
        String tg = TableNames.hwcTableGroups.split(" ")[1] + ".";
        return ReportPlans.dao.paginate(pageNumber, pageSize, "select " + hrp + "*, " + tg + "frequency ", sql.toString());
    }
}
