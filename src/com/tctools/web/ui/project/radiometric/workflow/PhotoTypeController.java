package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricPhotoType;
import com.tctools.business.dto.user.*;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/photo/type/get",
})
public class PhotoTypeController extends RouteToMethod {

    public void radioMetricPhotoTypeGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, EnumUtil.getEnumValues(RadioMetricPhotoType.values()));
    }
}