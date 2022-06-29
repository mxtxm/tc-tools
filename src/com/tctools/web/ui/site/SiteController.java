package com.tctools.web.ui.site;

import com.tctools.business.model.site.SiteModel;
import com.vantar.exception.*;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/site/insert",
    "/ui/site/update",
    "/ui/site/delete",
    "/ui/site/get",

    "/ui/sites/search",
    "/ui/sites/autocomplete",
    "/ui/sites/near",

    "/ui/sites/codes",
})
public class SiteController extends RouteToMethod {

    @Access({"ADMIN", "MANAGER", "VENDOR",})
    public void siteInsert(Params params, HttpServletResponse response) throws ServerException, InputException {
        Response.writeJson(response, SiteModel.insert(params));
    }

    @Access({"ADMIN", "MANAGER", "VENDOR",})
    public void siteUpdate(Params params, HttpServletResponse response) throws ServerException, InputException {
        Response.writeJson(response, SiteModel.update(params));
    }

    @Access({"ADMIN", "MANAGER", "VENDOR",})
    public void siteDelete(Params params, HttpServletResponse response) throws ServerException, InputException {
        Response.writeJson(response, SiteModel.delete(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void siteGet(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, SiteModel.getByIdOrCode(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "VENDOR", "READONLY",})
    public void sitesSearch(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, SiteModel.search(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void sitesAutocomplete(Params params, HttpServletResponse response) throws ServerException, NoContentException {
        Response.writeJson(response, SiteModel.autoComplete(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void sitesNear(Params params, HttpServletResponse response) throws ServerException, InputException {
        Response.writeJson(response, SiteModel.getNearSites(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void sitesCodes(Params params, HttpServletResponse response) throws ServerException {
        Response.writeJson(response, SiteModel.sitesCodes(params));
    }
}