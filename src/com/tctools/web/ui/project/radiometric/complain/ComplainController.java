package com.tctools.web.ui.project.radiometric.complain;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.radiometric.Assigning;
import com.tctools.business.model.project.radiometric.complain.ComplainModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/radio/metric/complain/delete",

    "/ui/radio/metric/complain/get",
    "/ui/radio/metric/complains/search",
    "/ui/radio/metric/complains/assignable",
    "/ui/radio/metric/complains/assigned",

    "/ui/radio/metric/complain/assign",
    "/ui/radio/metric/complain/assign/remove",
})
public class ComplainController extends RouteToMethod {

    public void radioMetricComplainDelete(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.delete(params));
    }

    public void radioMetricComplainGet(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.get(params));
    }

    public void radioMetricComplainsSearch(Params params, HttpServletResponse response) throws InputException, ServerException, NoContentException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.search(params));
    }

    public void radioMetricComplainsAssignable(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        try {
            user.projectAccess(ProjectType.RadioMetric);
        } catch (AuthException e) {
            e.printStackTrace();
        }
        Response.writeJson(response, ComplainModel.assignable(params));
    }

    public void radioMetricComplainsAssigned(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.assigned(params));
    }

    public void radioMetricComplainAssign(Params params, HttpServletResponse response) throws InputException, ServerException, NoContentException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, Assigning.assignComplain(params, user));
    }

    public void radioMetricComplainAssignRemove(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, Assigning.assignRemove(params, user));
    }
}