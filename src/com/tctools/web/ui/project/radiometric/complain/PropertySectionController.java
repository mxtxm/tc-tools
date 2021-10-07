package com.tctools.web.ui.project.radiometric.complain;

import com.tctools.business.model.project.radiometric.complain.PropertySectionModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/property/sections/get",
    "/ui/property/sections/keyval",
})
public class PropertySectionController extends RouteToMethod {

    public void propertySectionsGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, PropertySectionModel.getAll());
    }

    public void propertySectionsKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, PropertySectionModel.getAsKeyValue());
    }
}