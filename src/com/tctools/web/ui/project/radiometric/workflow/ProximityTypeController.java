package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricProximityType;
import com.tctools.business.dto.user.Role;
import com.vantar.exception.AuthException;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/radio/metric/proximity/type/get",
})
public class ProximityTypeController extends RouteToMethod {

    public void radioMetricProximityTypeGet(Params params, HttpServletResponse response) throws AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, EnumUtil.getEnumValues(RadioMetricProximityType.values()));
    }
}