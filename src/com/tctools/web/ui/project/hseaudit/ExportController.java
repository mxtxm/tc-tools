package com.tctools.web.ui.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.hseaudit.export.*;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/hse/audit/daily/report",
    "/ui/hse/audit/data",
    "/ui/hse/audit/data/many",

    // excel
    "/ui/hse/audit/question/province",
    "/ui/hse/audit/question/subcontractor",

    "/ui/hse/audit/significance/month",
    "/ui/hse/audit/significance/subcontractor/province",
    "/ui/hse/audit/significance/subcontractor/province/complete",

    // no excel
    "/ui/hse/audit/significance/province",
    "/ui/hse/audit/significance/subcontractor",
})
public class ExportController extends RouteToMethod {

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditDailyReport(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new ExportModel().dailyReport(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditData(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new ExportModel().auditData(params, response, null, null);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditDataMany(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new ExportModel().auditDataMany(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditQuestionProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new QuestionProvince().outputAggregate(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditQuestionSubcontractor(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new QuestionSubcontractor().outputAggregate(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceMonth(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new SignificanceMonth().outputAggregate(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceSubcontractorProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new SignificanceSubcontractorProvince().outputAggregate(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceSubcontractorProvinceComplete(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new SignificanceSubcontractorProvinceComplete().outputAggregate(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new SignificanceProvince().outputAggregate(params, response);
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceSubcontractor(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        new SignificanceSubcontractor().outputAggregate(params, response);
    }
}