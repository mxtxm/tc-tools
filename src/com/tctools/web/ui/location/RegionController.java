package com.tctools.web.ui.location;

import com.tctools.business.model.location.*;
import com.vantar.exception.VantarException;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/regions/get",
    "/ui/regions/keyval",
    "/ui/regions/ac/keyval",
})
public class RegionController extends RouteToMethod {

    public void regionsGet(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, RegionModel.getAll(params));
    }

    public void regionsKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, RegionModel.getAsKeyValue(params));
    }

    public void regionsAcKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, RegionModel.getAcAsKeyValue(params));
    }
}