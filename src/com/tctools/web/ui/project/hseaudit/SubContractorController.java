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

    public void hseAuditSubcontractorsSearch(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, SubcontractorModel.search(params));
    }

    public void hseAuditSubcontractorGet(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, SubcontractorModel.get(params));
    }

    public void hseAuditSubcontractorAutocomplete(Params params, HttpServletResponse response) throws ServerException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, SubcontractorModel.autoComplete(params));
    }
}