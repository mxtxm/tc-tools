package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlowState;
import com.vantar.exception.*;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/flow/state/get",
})
public class FlowStateController extends RouteToMethod {

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void radioMetricFlowStateGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Response.writeJson(response, EnumUtil.getEnumValues(RadioMetricFlowState.values()));
    }
}