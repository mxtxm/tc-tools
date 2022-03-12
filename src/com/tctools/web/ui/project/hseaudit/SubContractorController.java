package com.tctools.web.ui.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.hseaudit.SubcontractorModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/hse/audit/subcontractors/search",
    "/ui/hse/audit/subcontractor/get",
    "/ui/hse/audit/subcontractor/autocomplete",
})
public class SubContractorController extends RouteToMethod {

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "READONLY", "VENDOR",})
    public void hseAuditSubcontractorsSearch(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, SubcontractorModel.search(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "READONLY", "VENDOR",})
    public void hseAuditSubcontractorGet(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, SubcontractorModel.get(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "READONLY", "VENDOR",})
    public void hseAuditSubcontractorAutocomplete(Params params, HttpServletResponse response) throws ServerException, AuthException {
        ((User) Services.get(ServiceAuth.class).getCurrentUser(params)).projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, SubcontractorModel.autoComplete(params));
    }
}