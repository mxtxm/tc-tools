package com.tctools.web.ui.site;

import com.tctools.business.model.site.SectorOptimizationModel;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/sector/optimization/get",
    "/ui/site/sector/optimization/keyval",
})
public class SectorOptimizationController extends RouteToMethod {

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteSectorOptimizationGet(Params params, HttpServletResponse response) {
        Response.writeJson(response, SectorOptimizationModel.getAll());
    }

    @Access({"ADMIN", "MCI", "MANAGER", "ATOMI", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteSectorOptimizationKeyval(Params params, HttpServletResponse response) {
        Response.writeJson(response, SectorOptimizationModel.getAsKeyValue());
    }
}