package com.tctools.web.ui.project.radiometric.complain;

import com.tctools.business.dto.project.radiometric.complain.ComplainType;
import com.vantar.exception.*;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/complain/type/get",
})
public class ComplainTypeController extends RouteToMethod {

    @Access({"MANAGER", "VENDOR", "READONLY",})
    public void usersGetRoles(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Response.writeJson(response, EnumUtil.getEnumValues(ComplainType.values()));
    }
}