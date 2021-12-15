package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.site.CollocationType;
import com.tctools.business.dto.user.Role;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/radio/metric/share/type/get",
})
public class ShareTypeController extends RouteToMethod {

    public void radioMetricShareTypeGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, EnumUtil.getEnumValues(CollocationType.values()));
    }
}