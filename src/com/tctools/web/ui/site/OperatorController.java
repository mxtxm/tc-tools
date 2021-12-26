package com.tctools.web.ui.site;

import com.tctools.business.model.site.OperatorModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/operators/get",
    "/ui/operators/keyval",
})
public class OperatorController extends RouteToMethod {

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void operatorsGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, OperatorModel.getAll(params));
    }

    @Access({"MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void operatorsKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, OperatorModel.getAsKeyValue(params));
    }
}