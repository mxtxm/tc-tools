package com.tctools.web.ui.site;

import com.tctools.business.model.site.BtsOwnershipModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/bts/ownership/get",
    "/ui/site/bts/ownership/keyval",
})
public class BtsOwnershipController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsOwnershipGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsOwnershipModel.getAll(params));
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsOwnershipKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsOwnershipModel.getAsKeyValue(params));
    }
}