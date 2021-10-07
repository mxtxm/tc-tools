package com.tctools.web.ui.site;

import com.tctools.business.dto.user.Role;
import com.tctools.business.model.site.OperatorModel;
import com.vantar.exception.AuthException;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/operators/get",
    "/ui/operators/keyval",
})
public class OperatorController extends RouteToMethod {

    public void operatorsGet(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        Response.writeJson(response, OperatorModel.getAll(params));
    }

    public void operatorsKeyval(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        Response.writeJson(response, OperatorModel.getAsKeyValue(params));
    }
}