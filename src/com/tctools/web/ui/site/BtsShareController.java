package com.tctools.web.ui.site;

import com.tctools.business.dto.user.Role;
import com.tctools.business.model.site.BtsShareModel;
import com.vantar.exception.AuthException;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/site/bts/share/get",
    "/ui/site/bts/share/keyval",
})
public class BtsShareController extends RouteToMethod {

    public void siteBtsShareGet(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, BtsShareModel.getAll(params));
    }

    public void siteBtsShareKeyval(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, BtsShareModel.getAsKeyValue(params));
    }
}