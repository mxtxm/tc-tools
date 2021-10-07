package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.radiometric.Assigning;
import com.tctools.business.model.project.radiometric.workflow.MapFlowModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/radio/metric/flows/search/map",

    "/ui/radio/metric/site/assign",
    "/ui/radio/metric/site/assign/remove",

    "/ui/radio/metric/update/measurement/location",
})
public class MapFlowController extends RouteToMethod {

    public void radioMetricFlowsSearchMap(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException, AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, MapFlowModel.searchForMap(params));
    }

    public void radioMetricSiteAssign(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, Assigning.assign(params, user));
    }

    public void radioMetricSiteAssignRemove(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.VENDOR, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, Assigning.assignRemove(params, user));
    }

    public void radioMetricUpdateMeasurementLocation(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException, NoContentException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        user.projectAccess(ProjectType.RadioMetric);
        Response.writeJson(response, MapFlowModel.updateSpotLocation(params));
    }
}