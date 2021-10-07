package com.tctools.web.ui.site;

import com.tctools.business.dto.user.Role;
import com.tctools.business.model.site.SiteClassModel;
import com.vantar.exception.AuthException;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/site/class/get",
    "/ui/site/class/keyval",
})
public class SiteClassController extends RouteToMethod {

    public void siteClassGet(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, SiteClassModel.getAll(params));
    }

    public void siteClassKeyval(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, SiteClassModel.getAsKeyValue(params));
    }
}