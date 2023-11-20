package com.tctools.web.ui.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.hseaudit.*;
import com.tctools.web.patch.TestController;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/hse/audit/questionnaire/submit",

    "/ui/hse/audit/questionnaire/delete",
    "/ui/hse/audit/questionnaire/update",
    "/ui/hse/audit/questionnaire/update/state",

    "/ui/hse/audit/questionnaire/search",
    "/ui/hse/audit/questionnaire/get/assigned",
    "/ui/hse/audit/questionnaire/get",

    "/ui/hse/audit/questionnaire/image/delete",
    "/ui/hse/audit/questionnaire/get/assigned/test",
})
public class QuestionnaireController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "TECHNICIAN", "VENDOR",})
    public void hseAuditQuestionnaireSubmit(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.submit(params, user));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR",})
    public void hseAuditQuestionnaireDelete(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.delete(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR",})
    public void hseAuditQuestionnaireUpdate(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.update(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR",})
    public void hseAuditQuestionnaireUpdateState(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.updateState(params, user));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "READONLY", "VENDOR",})
    public void hseAuditQuestionnaireSearch(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.search(params));
    }

    @Access({"ADMIN", "MCI", "TECHNICIAN", "TECHNICIAN", "READONLY", "VENDOR",})
    public void hseAuditQuestionnaireGetAssigned(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.getAssigned(params, user));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "READONLY", "VENDOR",})
    public void hseAuditQuestionnaireGet(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.get(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR",})
    public void hseAuditQuestionnaireImageDelete(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.deleteImage(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "READONLY", "VENDOR",})
    public void hseAuditQuestionnaireGetAssignedTest(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        response.setHeader("Cache-Control", params.getString("cacheparams"));
        TestController.log.error(">>>>>SHAHIN {}", params.getHeaders());
        Response.writeJson(response, WorkFlowModel.getAssigned(params, user));
    }

}