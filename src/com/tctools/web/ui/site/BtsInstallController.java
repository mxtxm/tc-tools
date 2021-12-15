package com.tctools.web.ui.site;

import com.tctools.business.dto.user.Role;
import com.tctools.business.model.site.BtsInstallModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/site/bts/install/get",
    "/ui/site/bts/install/keyval",
})
public class BtsInstallController extends RouteToMethod {

    public void siteBtsInstallGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, BtsInstallModel.getAll(params));
    }

    public void siteBtsInstallKeyval(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, BtsInstallModel.getAsKeyValue(params));
    }
}