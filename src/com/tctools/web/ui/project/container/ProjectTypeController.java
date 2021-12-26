package com.tctools.web.ui.project.container;

import com.tctools.business.dto.project.container.ProjectType;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/project/type/get",
})
public class ProjectTypeController extends RouteToMethod {

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void projectTypeGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, EnumUtil.getEnumValues(ProjectType.values()));
    }
}