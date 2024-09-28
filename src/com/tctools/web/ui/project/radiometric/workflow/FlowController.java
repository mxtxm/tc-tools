package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.User;
import com.tctools.business.model.project.radiometric.Assigning;
import com.tctools.business.model.project.radiometric.workflow.WorkFlowModel;
import com.vantar.exception.VantarException;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/flow/delete",
    "/ui/radio/metric/flow/update/state",
    "/ui/radio/metric/flow/commit",

    "/ui/radio/metric/flow/get",
    "/ui/radio/metric/flow/get/tasks/new",
    "/ui/radio/metric/flow/get/tasks/finished",
    "/ui/radio/metric/flows/search",

    "/ui/radio/metric/image/delete",
    "/ui/radio/metric/log/delete",

    "/ui/radio/metric/drone/assign",
})
public class FlowController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR",})
    public void radioMetricFlowDelete(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.delete(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR", "TECHNICIAN",})
    public void radioMetricFlowUpdateState(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.updateState(params, user));
    }

    @Access({"ADMIN", "MCI", "TECHNICIAN", "VENDOR",})
    public void radioMetricFlowCommit(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.commitMeasurements(params, user));
    }

    //@Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricFlowGet(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.get(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR", "TECHNICIAN", "READONLY",})
    public void radioMetricFlowsSearch(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.search(params));
    }

    @Access({"ADMIN", "MCI", "TECHNICIAN", "READONLY", "VENDOR",})
    public void radioMetricFlowGetTasksNew(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.getNewTasks(params, user));
    }

    @Access({"ADMIN", "MCI", "TECHNICIAN", "READONLY", "VENDOR",})
    public void radioMetricFlowGetTasksFinished(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.getFinishedTasks(params, user));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR",})
    public void radioMetricImageDelete(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.deleteImage(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR",})
    public void radioMetricLogDelete(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.deleteLog(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "VENDOR",})
    public void radioMetricDroneAssign(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, Assigning.assignDrone(params, user));
    }
}