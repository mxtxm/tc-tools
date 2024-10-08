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

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR",})
    public void radioMetricComplainDelete(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.delete(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR", "READONLY",})
    public void radioMetricComplainGet(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.get(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR", "READONLY",})
    public void radioMetricComplainsSearch(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.search(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR", "READONLY",})
    public void radioMetricComplainsAssignable(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.assignable(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR", "READONLY",})
    public void radioMetricComplainsAssigned(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.assigned(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR",})
    public void radioMetricComplainAssign(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, Assigning.assignComplain(params, user));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR",})
    public void radioMetricComplainAssignRemove(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, Assigning.assignRemove(params, user));
    }
}