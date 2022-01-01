package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.site.CollocationType;
import com.vantar.exception.*;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/share/type/get",
})
public class ShareTypeController extends RouteToMethod {

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void radioMetricShareTypeGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Response.writeJson(response, EnumUtil.getEnumValues(CollocationType.values()));
    }
}