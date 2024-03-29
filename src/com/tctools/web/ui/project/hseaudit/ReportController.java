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

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "READONLY", "VENDOR",})
    public void hseAuditStateAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.stateAggregate());
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "READONLY", "VENDOR",})
    public void hseAuditActivityAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.activityAggregate());
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "READONLY", "VENDOR",})
    public void hseAuditProvinceAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.provinceAudit());
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "READONLY", "VENDOR",})
    public void hseAuditAnswerAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.answerCount());
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "READONLY", "VENDOR",})
    public void hseAuditUsersDoneAggregate(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.usersDoneAggregate(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "READONLY", "VENDOR",})
    public void hseAuditUserDoneAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, ReportModel.userDoneAggregate(params));
    }
}