package com.tctools.web.ui.site;

import com.tctools.business.model.site.BtsTowerModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/bts/tower/get",
    "/ui/site/bts/tower/keyval",
})
public class BtsTowerController extends RouteToMethod {

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsTowerGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsTowerModel.getAll());
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsTowerKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsTowerModel.getAsKeyValue());
    }
}