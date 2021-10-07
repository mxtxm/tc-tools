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

//todo ERROR 10:42:50 RouteToMethod.java:73 ! unhandled error
//    java.lang.IllegalArgumentException: This Style does not belong to the supplied Workbook Styles Source. Are you trying to assign a style from one workbook to the cell of a different workbook?
//    at org.apache.poi.xssf.usermodel.XSSFCellStyle.verifyBelongsToStylesSource(XSSFCellStyle.java:123) ~[poi-ooxml-5.0.0.jar:5.0.0]
//    at org.apache.poi.xssf.usermodel.XSSFCell.setCellStyle(XSSFCell.java:646) ~[poi-ooxml-5.0.0.jar:5.0.0]
//    at com.tctools.business.model.project.radiometric.workflow.export.ExportWaveControl.setHeader(ExportWaveControl.java:237) ~[classes/:?]
//    at com.tctools.business.model.project.radiometric.workflow.export.ExportWaveControl.excel(ExportWaveControl.java:63) ~[classes/:?]
//    at com.tctools.web.ui.project.radiometric.workflow.ExportController.radioMetricReportWavecontrolExcel(ExportController.java:44) ~[classes/:?]
//    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:?]
//    at jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:?]
//    at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:?]
//    at java.lang.reflect.Method.invoke(Method.java:566) ~[?:?]
//    at com.vantar.web.RouteToMethod.service(RouteToMethod.java:49) [classes/:?]
//    at javax.servlet.http.HttpServlet.service(HttpServlet.java:741) [tomcat9-servlet-api-9.0.31.jar:?]
//    at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) [tomcat9-catalina-9.0.31.jar:9.0.31]
//    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) [tomcat9-catalina-9.0.31.jar:9.0.31]
//    at org.apache.tomcat.websocket.se

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

    public void radioMetricSiteDocx(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        ExportSite.docx(params, response, true);
    }

    public void radioMetricSiteZip(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        ExportSite.zip(params, response);
    }

    public void radioMetricSiteAssignedExcel(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        ExportAssigned.excel(params, user, response);
    }

    public void radioMetricReportWavecontrolExcel(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        ExportWaveControl.excel(params, response);
    }

    public void radioMetricOverviewState(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getOverview(params));
    }

    public void radioMetricOverviewStateMonthly(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getMonthlyOverview());
    }

    public void radioMetricOverviewStateMonthlyTarget(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, StateReport.getMonthlyOverviewTarget());
    }

    public void radioMetricOverviewType(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJsonString(response, MetricTypeReport.getCount());
    }

    public void radioMetricPerformanceTechMonthly(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, TechReport.getMonthlyPerformance(params));
    }

    public void radioMetricPerformanceTech(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, TechReport.getTotalPerformance(params));
    }

    public void radioMetricPerformanceProvinceMonthly(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ProvinceReport.getMonthlyPerformance(params));
    }

    public void radioMetricPerformanceProvince(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ProvinceReport.getTotalPerformance(params));
    }
}