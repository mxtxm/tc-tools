package com.tctools.web.ui.location;

import com.tctools.business.model.location.ProvinceModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/provinces/get",
    "/ui/provinces/keyval",
    "/ui/provinces/ac/keyval",
})
public class ProvinceController extends RouteToMethod {

    public void provincesGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, ProvinceModel.getAll(params));
    }

    public void provincesKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, ProvinceModel.getAsKeyValue(params));
    }

    public void provincesAcKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, ProvinceModel.getAcAsKeyValue(params));
    }
}