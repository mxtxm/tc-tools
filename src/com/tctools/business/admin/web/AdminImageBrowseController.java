package com.tctools.business.admin.web;

import com.tctools.business.admin.model.*;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/admin/image/browse",
})
public class AdminImageBrowseController extends RouteToMethod {

    public void imageBrowse(Params params, HttpServletResponse response) {
        AdminImageBrowse.index(params, response);
    }
}
