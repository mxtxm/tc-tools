package com.tctools.business.admin.web;

import com.tctools.business.admin.model.*;
import com.vantar.web.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/admin/import/index",
    "/admin/import/sites",

    "/admin/synch/radiometric",
    "/admin/synch/hseaudit",

    "/admin/import/hseaudit/work",

    "/admin/hseaudit/fix",
    "/admin/hseaudit/fix/files",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=100*1024*1024,
    maxFileSize=100*1024*1024,
    maxRequestSize=100*1024*1024*5
)
public class AdminImportExportController extends RouteToMethod {

    public void importIndex(Params params, HttpServletResponse response) {
        AdminImportExport.index(params, response);
    }

    public void importSites(Params params, HttpServletResponse response) {
        AdminSiteImport.importSites(params, response);
    }

    public void synchRadiometric(Params params, HttpServletResponse response) {
        AdminSynchRadiometric.index(params, response);
    }

    public void synchHseaudit(Params params, HttpServletResponse response) {
        AdminSynchHseAudit.index(params, response);
    }

    public void importHseauditWork(Params params, HttpServletResponse response) {
        AdminImportHseAudit.work(params, response);
    }

    public void hseauditFix(Params params, HttpServletResponse response) {
        AdminImportHseAudit.fix(params, response);
    }

    public void hseauditFixFiles(Params params, HttpServletResponse response) {
        AdminImportHseAudit.fixFiles(params, response);
    }
}
