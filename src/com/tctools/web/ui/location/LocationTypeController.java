package com.tctools.web.ui.location;

import com.tctools.business.model.location.LocationTypeModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/location/type/get",
    "/ui/location/type/keyval",
})
public class LocationTypeController extends RouteToMethod {

    public void locationTypeGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, LocationTypeModel.getAll(params));
    }

    public void locationTypeKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, LocationTypeModel.getAsKeyValue(params));
    }
}