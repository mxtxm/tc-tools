package com.tctools.web.ui.project.radiometric.complain;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.radiometric.complain.ComplainModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/radio/metric/complain/insert",
    "/ui/radio/metric/complain/update",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class ComplainUploadController extends RouteToMethod {

    public void radioMetricComplainInsert(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.insert(params, user));
    }

    public void radioMetricComplainUpdate(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, ComplainModel.update(params));
    }
}