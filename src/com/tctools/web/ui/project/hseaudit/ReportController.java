package com.tctools.web.ui.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.hseaudit.export.ReportModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/hse/audit/state/aggregate",
    "/ui/hse/audit/activity/aggregate",
    "/ui/hse/audit/province/aggregate",
    "/ui/hse/audit/answer/aggregate",
    "/ui/hse/audit/users/done/aggregate",
    "/ui/hse/audit/user/done/aggregate",
})
public class ReportController extends RouteToMethod {

    public void hseAuditStateAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.stateAggregate());
    }

    public void hseAuditActivityAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.activityAggregate());
    }

    public void hseAuditProvinceAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.provinceAudit());
    }

    public void hseAuditAnswerAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.answerCount());
    }

    public void hseAuditUsersDoneAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.usersDoneAggregate(params));
    }

    public void hseAuditUserDoneAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.userDoneAggregate(params));
    }
}