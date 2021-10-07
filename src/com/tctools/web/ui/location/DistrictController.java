package com.tctools.web.ui.location;

import com.tctools.business.model.location.DistrictModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/districts/get",
    "/ui/districts/keyval",
})
public class DistrictController extends RouteToMethod {

    public void districtsGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, DistrictModel.getAll());
    }

    public void districtsKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, DistrictModel.getAsKeyValue());
    }
}