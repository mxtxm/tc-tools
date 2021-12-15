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

    public void hseAuditDailyReport(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        ExportModel.dailyReport(params, response);
    }

    public void hseAuditData(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        ExportModel.auditData(params, response);
    }

    public void hseAuditQuestionProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        QuestionProvince.outputAggregate(params, response);
    }

    public void hseAuditQuestionSubcontractor(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        QuestionSubcontractor.outputAggregate(params, response);
    }

    public void hseAuditSignificanceMonth(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        SignificanceMonth.outputAggregate(params, response);
    }

    public void hseAuditSignificanceSubcontractorProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        SignificanceSubcontractorProvince.outputAggregate(params, response);
    }

    public void hseAuditSignificanceSubcontractorProvinceComplete(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        SignificanceSubcontractorProvinceComplete.outputAggregate(params, response);
    }

    public void hseAuditSignificanceProvince(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        SignificanceProvince.outputAggregate(params, response);
    }

    public void hseAuditSignificanceSubcontractor(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.HseAudit);
        SignificanceSubcontractor.outputAggregate(params, response);
    }
}