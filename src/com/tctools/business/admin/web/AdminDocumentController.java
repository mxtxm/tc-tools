package com.tctools.business.admin.web;

import com.vantar.admin.model.AdminDocument;
import com.vantar.common.Settings;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/admin/document/show/dtos",
})
public class AdminDocumentController extends RouteToMethod {

    public void documentShowDtos(Params params, HttpServletResponse response) {
        AdminDocument.show(Settings.config.getProperty("documents.dir") + "objects.md", response, false);
    }

}
