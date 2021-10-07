package com.tctools.business.admin.web;

import com.tctools.business.admin.model.*;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/admin/webservice/index",
    "/admin/webservice/show",
    "/admin/webservice/form",

    "/admin/webservice/log",
})
public class AdminWebserviceController extends RouteToMethod {

    public void webserviceIndex(Params params, HttpServletResponse response) {
        AdminWebService.index(params, response);
    }

    public void webserviceShow(Params params, HttpServletResponse response) {
        AdminWebService.show(params, response);
    }

    public void webserviceForm(Params params, HttpServletResponse response) {
        AdminWebService.form(params, response);
    }

    public void webserviceLog(Params params, HttpServletResponse response) {
        AdminRequestLog.showLog(params, response);
    }

}
