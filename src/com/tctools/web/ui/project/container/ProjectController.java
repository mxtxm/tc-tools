package com.tctools.web.ui.project.container;

import com.tctools.business.dto.user.Role;
import com.tctools.business.model.project.container.ProjectModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/project/insert",
    "/ui/project/update",
    "/ui/project/delete",
    "/ui/project/get",
    "/ui/projects/get/all",
})
public class ProjectController extends RouteToMethod {

    public void projectInsert(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(response, ProjectModel.insert(params));
    }

    public void projectUpdate(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(response, ProjectModel.update(params));
    }

    public void projectDelete(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(response, ProjectModel.delete(params));
    }

    public void projectGet(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException, AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN, Role.VENDOR);
        Response.writeJson(response, ProjectModel.get(params));
    }

    public void projectsGetAll(Params params, HttpServletResponse response) throws ServerException, NoContentException, AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN, Role.VENDOR);
        Response.writeJson(response, ProjectModel.getAll(params));
    }

}