package com.tctools.business.admin.web;

import com.tctools.business.admin.model.*;
import com.vantar.exception.FinishException;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/admin/image/browse",
})
public class AdminImageBrowseController extends RouteToMethod {

    public void imageBrowse(Params params, HttpServletResponse response) throws FinishException {
        AdminImageBrowse.index(params, response);
    }
}
