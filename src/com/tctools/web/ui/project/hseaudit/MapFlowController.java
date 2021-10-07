package com.tctools.web.ui.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.hseaudit.MapFlowModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/hse/audit/flows/search/map",

    "/ui/hse/audit/site/assign",
    "/ui/hse/audit/site/assign/remove",
    "/ui/hse/audit/create/child",
})
public class MapFlowController extends RouteToMethod {

    public void hseAuditFlowsSearchMap(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, MapFlowModel.searchForMap(params));
    }

    public void hseAuditSiteAssign(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, MapFlowModel.assign(params, user));
    }

    public void hseAuditSiteAssignRemove(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, MapFlowModel.removeAssign(params, user));
    }

    public void hseAuditCreateChild(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, MapFlowModel.createChild(params));
    }
}