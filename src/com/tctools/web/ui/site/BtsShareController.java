package com.tctools.web.ui.site;

import com.tctools.business.model.site.BtsShareModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/bts/share/get",
    "/ui/site/bts/share/keyval",
})
public class BtsShareController extends RouteToMethod {

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsShareGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsShareModel.getAll(params));
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteBtsShareKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, BtsShareModel.getAsKeyValue(params));
    }
}