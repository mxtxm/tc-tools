package com.tctools.web.ui.location;

import com.tctools.business.model.location.CityModel;
import com.vantar.exception.*;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/cities/get",
    "/ui/cities/keyval",
    "/ui/cities/ac/keyval",
})
public class CityController extends RouteToMethod {

    public void citiesGet(Params params, HttpServletResponse response) throws ServerException, NoContentException {
        Response.writeJson(response, CityModel.getAll(params));
    }

    public void citiesKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, CityModel.getAsKeyValue(params));
    }

    public void citiesAcKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, CityModel.getAcAsKeyValue(params));
    }
}