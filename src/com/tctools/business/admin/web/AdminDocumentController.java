package com.tctools.business.admin.web;

import com.vantar.admin.model.document.AdminDocument;
import com.vantar.common.Settings;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/admin/document/show/dtos",
})
public class AdminDocumentController extends RouteToMethod {

    public void documentShowDtos(Params params, HttpServletResponse response) {
        AdminDocument.showFromFile(Settings.config.getProperty("documents.dir") + "objects.md", response);
    }

}
