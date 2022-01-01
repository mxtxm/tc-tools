package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.radiometric.workflow.ReportModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/state/aggregate",
    "/ui/radio/metric/province/aggregate",
    "/ui/radio/metric/users/done/aggregate",
    "/ui/radio/metric/user/done/aggregate",
})
public class ReportController extends RouteToMethod {

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void radioMetricStateAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ReportModel.stateAggregate());
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void radioMetricProvinceAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ReportModel.provinceAudit());
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void radioMetricUsersDoneAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ReportModel.usersDoneAggregate(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void radioMetricUserDoneAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ReportModel.userDoneAggregate(params));
    }
}