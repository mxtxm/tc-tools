package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.radiometric.workflow.WorkFlowModel;
import com.vantar.exception.*;
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
})
public class FlowController extends RouteToMethod {

    public void radioMetricFlowDelete(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.delete(params));
    }

    public void radioMetricFlowUpdateState(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.updateState(params, user));
    }

    public void radioMetricFlowCommit(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.commitMeasurements(params, user));
    }

    public void radioMetricFlowGet(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.get(params));
    }

    public void radioMetricFlowsSearch(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.search(params));
    }

    public void radioMetricFlowGetTasksNew(Params params, HttpServletResponse response) throws ServerException, NoContentException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.getNewTasks(params, user));
    }

    public void radioMetricFlowGetTasksFinished(Params params, HttpServletResponse response) throws ServerException, NoContentException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.getFinishedTasks(params, user));
    }

    public void radioMetricImageDelete(Params params, HttpServletResponse response) throws AuthException, InputException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.TECHNICIAN, Role.ENGINEER, Role.MANAGER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.deleteImage(params));
    }

    public void radioMetricLogDelete(Params params, HttpServletResponse response) throws AuthException, InputException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.TECHNICIAN, Role.ENGINEER, Role.MANAGER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, WorkFlowModel.deleteLog(params));
    }
}