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
    "/ui/site/bts/ownership/get",
    "/ui/site/bts/ownership/keyval",
})
public class BtsOwnershipController extends RouteToMethod {

    public void siteBtsOwnershipGet(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, BtsOwnershipModel.getAll(params));
    }

    public void siteBtsOwnershipKeyval(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, BtsOwnershipModel.getAsKeyValue(params));
    }
}