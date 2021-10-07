package com.tctools.web.ui.site;

import com.tctools.business.dto.user.Role;
import com.tctools.business.model.site.SiteModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
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

    public void siteInsert(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR);
        Response.writeJson(response, SiteModel.insert(params));
    }

    public void siteUpdate(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR);
        Response.writeJson(response, SiteModel.update(params));
    }

    public void siteDelete(Params params, HttpServletResponse response) throws AuthException, ServerException, InputException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR);
        Response.writeJson(response, SiteModel.delete(params));
    }

    public void siteGet(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException, AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, SiteModel.get(params));
    }

    public void sitesSearch(Params params, HttpServletResponse response) throws ServerException, NoContentException, AuthException, InputException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR);
        Response.writeJson(response, SiteModel.search(params));
    }

    public void sitesAutocomplete(Params params, HttpServletResponse response) throws ServerException, AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, SiteModel.autoComplete(params));
    }

    public void sitesNear(Params params, HttpServletResponse response) throws ServerException, AuthException, InputException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, SiteModel.getNearSites(params));
    }

    public void sitesCodes(Params params, HttpServletResponse response) throws ServerException, AuthException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, SiteModel.sitesCodes(params));
    }
}