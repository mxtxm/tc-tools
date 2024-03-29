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

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "READONLY", "VENDOR",})
    public void hseAuditFlowsSearchMap(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, MapFlowModel.searchForMap(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR",})
    public void hseAuditSiteAssign(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, MapFlowModel.assign(params, user));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR",})
    public void hseAuditSiteAssignRemove(Params params, HttpServletResponse response) throws VantarException {
        User user = ((User) Services.get(ServiceAuth.class).getCurrentUser(params));
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, MapFlowModel.removeAssign(params, user));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "VENDOR",})
    public void hseAuditCreateChild(Params params, HttpServletResponse response) throws VantarException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, MapFlowModel.createChild(params));
    }
}