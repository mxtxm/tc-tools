package com.tctools.web.ui.project.radiometric.complain;

import com.tctools.business.dto.project.radiometric.complain.ComplainType;
import com.tctools.business.dto.user.Role;
import com.vantar.exception.AuthException;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/complain/type/get",
})
public class ComplainTypeController extends RouteToMethod {

    public void usersGetRoles(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR);
        Response.writeJson(response, EnumUtil.getEnumValues(ComplainType.values()));
    }
}