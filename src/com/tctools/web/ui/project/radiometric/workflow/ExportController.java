package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.radiometric.workflow.export.ExportSite;
import com.tctools.business.model.project.radiometric.workflow.export.*;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
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

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricSiteDocx(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        ExportSite.docx(params, response, true);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricSiteZip(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        ExportSite.zip(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricSiteAssignedExcel(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        ExportAssigned.excel(params, user, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricReportWavecontrolExcel(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        new ExportWaveControl().excel(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricOverviewState(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getOverview(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricOverviewStateMonthly(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getMonthlyOverview());
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricOverviewStateMonthlyTarget(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getMonthlyOverviewTarget());
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricOverviewType(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJsonString(response, MetricTypeReport.getCount());
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricPerformanceTechMonthly(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, TechReport.getMonthlyPerformance(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricPerformanceTech(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, TechReport.getTotalPerformance(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricPerformanceProvinceMonthly(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ProvinceReport.getMonthlyPerformance(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricPerformanceProvince(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ProvinceReport.getTotalPerformance(params));
    }
}