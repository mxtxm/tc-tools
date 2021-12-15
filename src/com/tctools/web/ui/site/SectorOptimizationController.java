package com.tctools.web.ui.site;

import com.tctools.business.dto.user.Role;
import com.tctools.business.model.site.SectorOptimizationModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/site/sector/optimization/get",
    "/ui/site/sector/optimization/keyval",
})
public class SectorOptimizationController extends RouteToMethod {

    public void siteSectorOptimizationGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, SectorOptimizationModel.getAll());
    }

    public void siteSectorOptimizationKeyval(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.ENGINEER, Role.MANAGER, Role.TECHNICIAN);
        Response.writeJson(response, SectorOptimizationModel.getAsKeyValue());
    }
}