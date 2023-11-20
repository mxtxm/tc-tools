package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.User;
import com.tctools.business.model.project.radiometric.ExportWorkflowModel;
import com.tctools.business.model.project.radiometric.workflow.export.*;
import com.tctools.common.Param;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

///ui/radio/metric/search/excel?q=%7B%0A%22condition%22%3A%7B%22operator%22%3A%22AND%22%2C%22items%22%3A%5B%7B%22col%22%3A%22site.provinceId%22%2C%22type%22%3A%22IN%22%2C%22values%22%3A%5B1%5D%7D%2C%7B%22col%22%3A%22site.cityId%22%2C%22type%22%3A%22EQUAL%22%2C%22value%22%3A1%7D%2C%7B%22col%22%3A%22measurementDateTime%22%2C%22type%22%3A%22BETWEEN%22%2C%22values%22%3A%5B%221402-04-11%2B14%3A33%22%2C%221402-04-11%2B15%3A33%22%5D%7D%5D%7D%0A%7D
//
//    {
//    "condition":{"operator":"AND","items":[{"col":"site.provinceId","type":"IN","values":[1]},{"col":"site.cityId","type":"EQUAL","value":1},{"col":"measurementDateTime","type":"BETWEEN","values":["1402-04-11+14:33","1402-04-11+15:33"]}]}
//    }
@WebServlet({
    "/ui/radio/metric/search/excel",
    "/ui/radio/metric/site/state/excel",

    "/ui/radio/metric/site/docx",
    "/ui/radio/metric/site/zip",
    "/ui/radio/metric/site/assigned/excel",
    "/ui/radio/metric/report/wavecontrol/excel",

    "/ui/radio/metric/overview/state",
    "/ui/radio/metric/overview/state/monthly",
    "/ui/radio/metric/overview/state/monthly/target",
    "/ui/radio/metric/overview/type",

    "/ui/radio/metric/performance/tech/monthly",
    "/ui/radio/metric/performance/tech",
    "/ui/radio/metric/performance/province/monthly",
    "/ui/radio/metric/performance/province",
})
public class ExportController extends RouteToMethod {

    public void radioMetricSearchExcel(Params params, HttpServletResponse response) throws VantarException {
        ExportWorkflowModel model = new ExportWorkflowModel();
        model.createXlsx(params, response);
    }


    public void radioMetricSiteStateExcel(Params params, HttpServletResponse response) {
        Response.download(response, Param.RADIO_METRIC_FILES + "site-states.xlsx", "site-states.xlsx");
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricSiteDocx(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        ExportSite.docx(params, response, true);
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricSiteZip(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        ExportSite.zip(params, response);
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricSiteAssignedExcel(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        ExportAssigned.excel(params, user, response);
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricReportWavecontrolExcel(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        new ExportWaveControl().excel(params, response);
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricOverviewState(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getOverview(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricOverviewStateMonthly(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getMonthlyOverview());
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricOverviewStateMonthlyTarget(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getMonthlyOverviewTarget());
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricOverviewType(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJsonString(response, MetricTypeReport.getCount());
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricPerformanceTechMonthly(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, TechReport.getMonthlyPerformance(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricPerformanceTech(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, TechReport.getTotalPerformance(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricPerformanceProvinceMonthly(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ProvinceReport.getMonthlyPerformance(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricPerformanceProvince(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ProvinceReport.getTotalPerformance(params));
    }
}