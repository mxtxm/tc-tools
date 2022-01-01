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

    @Access({"ADMIN", "TECHNICIAN",})
    public void hseAuditQuestionnaireSubmit(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.submit(params, user));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER",})
    public void hseAuditQuestionnaireDelete(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.delete(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER",})
    public void hseAuditQuestionnaireUpdate(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.update(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER",})
    public void hseAuditQuestionnaireUpdateState(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.updateState(params, user));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void hseAuditQuestionnaireSearch(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException, AuthException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.search(params));
    }

    @Access({"ADMIN", "TECHNICIAN", "READONLY",})
    public void hseAuditQuestionnaireGetAssigned(Params params, HttpServletResponse response) throws ServerException, NoContentException, AuthException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.getAssigned(params, user));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "READONLY",})
    public void hseAuditQuestionnaireGet(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException, AuthException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.get(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER",})
    public void hseAuditQuestionnaireImageDelete(Params params, HttpServletResponse response) throws AuthException, InputException, ServiceException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, WorkFlowModel.deleteImage(params));
    }

    @Access({"ADMIN", "TECHNICIAN",})
    public void hseAuditQuestionnaireGetAssignedTest(Params params, HttpServletResponse response) throws ServerException, NoContentException, AuthException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        response.setHeader("Cache-Control", params.getString("cacheparams"));
        TestController.log.error(">>>>>SHAHIN {}", params.getHeaders());
        Response.writeJson(response, WorkFlowModel.getAssigned(params, user));
    }

}