package com.tctools.web.ui.location;

import com.tctools.business.model.location.RegionModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/regions/get",
    "/ui/regions/keyval",
})
public class RegionController extends RouteToMethod {

    public void regionsGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, RegionModel.getAll(params));
    }

    public void regionsKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, RegionModel.getAsKeyValue(params));
    }
}