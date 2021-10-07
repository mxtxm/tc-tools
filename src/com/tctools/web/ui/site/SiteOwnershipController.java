package com.tctools.web.ui.site;

import com.tctools.business.dto.user.Role;
import com.tctools.business.model.site.*;
import com.vantar.exception.AuthException;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/site/ownership/get",
    "/ui/site/ownership/keyval",
})
public class SiteOwnershipController extends RouteToMethod {

    public void siteOwnershipGet(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        Response.writeJson(response, SiteOwnershipModel.getAll());
    }

    public void siteOwnershipKeyval(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        Response.writeJson(response, SiteOwnershipModel.getAsKeyValue());
    }
}