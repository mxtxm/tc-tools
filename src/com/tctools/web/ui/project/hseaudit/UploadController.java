package com.tctools.web.ui.project.hseaudit;

import com.tctools.business.model.project.hseaudit.WorkFlowModel;
import com.vantar.exception.*;
import com.vantar.web.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/hse/audit/questionnaire/image/upload",
    "/ui/hse/audit/questionnaire/image/upload/direct",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class UploadController extends RouteToMethod {

    public void hseAuditQuestionnaireImageUpload(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, WorkFlowModel.imageUpload(params));
    }

    public void hseAuditQuestionnaireImageUploadDirect(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, WorkFlowModel.imageUploadDirect(params));
    }
}