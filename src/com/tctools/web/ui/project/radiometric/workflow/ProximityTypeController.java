package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricProximityType;
import com.vantar.exception.*;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/proximity/type/get",
})
public class ProximityTypeController extends RouteToMethod {

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void radioMetricProximityTypeGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Response.writeJson(response, EnumUtil.getEnumValues(RadioMetricProximityType.values()));
    }
}