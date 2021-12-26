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

    // excel
    "/ui/hse/audit/question/province",
    "/ui/hse/audit/question/subcontractor",

    "/ui/hse/audit/significance/month",
    "/ui/hse/audit/significance/subcontractor/province",
    "/ui/hse/audit/significance/subcontractor/province/complete",

    //http://localhost:8081/ui/hse/audit/significance/subcontractor/province/complete?from=1400-01-1&to=1400-5-5&excel=1
    //http://www.cel.ictrc.ac.ir:8080/ui/hse/audit/significance/subcontractor/province/complete?from=1400-01-1&to=1400-5-5&excel=1

    //http://localhost:8081/ui/hse/audit/significance/subcontractor/province?from=1400-01-1&to=1400-5-5&subcontractorids=399,123
    //http://localhost:8081/ui/hse/audit/significance/subcontractor?from=1400-01-1&to=1400-5-5&provinceids=1,2,30
    //http://localhost:8081/ui/hse/audit/question/subcontractor?from=1399-09-1&to=1400-5-30&provinceids=1,2,30

    // no excel
    "/ui/hse/audit/significance/province",
    "/ui/hse/audit/significance/subcontractor",
})
public class ExportController extends RouteToMethod {

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditDailyReport(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        ExportModel.dailyReport(params, response);
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditData(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        ExportModel.auditData(params, response);
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditQuestionProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        QuestionProvince.outputAggregate(params, response);
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditQuestionSubcontractor(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        QuestionSubcontractor.outputAggregate(params, response);
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceMonth(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        SignificanceMonth.outputAggregate(params, response);
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceSubcontractorProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        SignificanceSubcontractorProvince.outputAggregate(params, response);
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceSubcontractorProvinceComplete(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        SignificanceSubcontractorProvinceComplete.outputAggregate(params, response);
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        SignificanceProvince.outputAggregate(params, response);
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void hseAuditSignificanceSubcontractor(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        SignificanceSubcontractor.outputAggregate(params, response);
    }
}