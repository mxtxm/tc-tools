package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricRadiationStatus;
import com.vantar.exception.*;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/radiation/status/get",
})
public class RadiationStatusController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void radioMetricRadiationStatusGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Response.writeJson(response, EnumUtil.getEnumValues(RadioMetricRadiationStatus.values()));
    }
}