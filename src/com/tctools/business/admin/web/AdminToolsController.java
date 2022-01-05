package com.tctools.business.admin.web;

import com.tctools.business.admin.model.AdminTools;
import com.vantar.exception.FinishException;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/admin/tools/index",
    "/admin/tools/state/add",
    "/admin/tools/state/update",

    "/admin/tools/update/all",
    "/admin/tools/radiometric/templates",
    "/admin/tools/radiometric/templates/docx",
    "/admin/tools/radiometric/signature",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class AdminToolsController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(AdminToolsController.class);

    public void toolsIndex(Params params, HttpServletResponse response) throws FinishException {
        AdminTools.index(params, response);
    }

    public void toolsStateAdd(Params params, HttpServletResponse response) throws FinishException {
        AdminTools.addState(params, response);
    }

    public void toolsStateUpdate(Params params, HttpServletResponse response) throws FinishException {
        AdminTools.updateState(params, response);
    }

    public void toolsUpdateAll(Params params, HttpServletResponse response) throws FinishException {
        AdminTools.updateAll(params, response);
    }

    public void toolsRadiometricTemplates(Params params, HttpServletResponse response) throws FinishException {
        AdminTools.radiometricTemplates(params, response);
    }

    public void toolsRadiometricTemplatesDocx(Params params, HttpServletResponse response) throws FinishException {
        Response.download(response, "/opt/tc-tools/templates/radiometric/site-radiometric.docx", "site-radiometric.docx");
    }

    public void toolsRadiometricSignature(Params params, HttpServletResponse response) throws FinishException {
        AdminTools.signature(params, response);
    }
}