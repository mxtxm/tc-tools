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

    public void radioMetricStateAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ReportModel.stateAggregate());
    }

    public void radioMetricProvinceAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ReportModel.provinceAudit());
    }

    public void radioMetricUsersDoneAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ReportModel.usersDoneAggregate(params));
    }

    public void radioMetricUserDoneAggregate(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ReportModel.userDoneAggregate(params));
    }
}