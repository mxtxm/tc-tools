package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricPhotoType;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/photo/type/get",
})
public class PhotoTypeController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void radioMetricPhotoTypeGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, EnumUtil.getEnumValues(RadioMetricPhotoType.values()));
    }
}