package com.tctools.web.ui.project.container;

import com.tctools.business.model.project.container.ProjectModel;
import com.vantar.exception.*;
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

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER",})
    public void projectInsert(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, ProjectModel.insert(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER",})
    public void projectUpdate(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, ProjectModel.update(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER",})
    public void projectDelete(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, ProjectModel.delete(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void projectGet(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, ProjectModel.get(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void projectsGetAll(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, ProjectModel.getAll(params));
    }
}