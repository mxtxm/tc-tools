package com.tctools.web.ui.project.radiometric.complain;

import com.tctools.business.model.project.radiometric.complain.PropertyModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/properties/get",
    "/ui/properties/keyval",
})
public class PropertyController extends RouteToMethod {

    public void propertiesGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, PropertyModel.getAll());
    }

    public void propertiesKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, PropertyModel.getAsKeyValue());
    }
}