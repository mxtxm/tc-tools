package com.tctools.web.ui.project.radiometric.workflow;

import com.tctools.business.model.project.radiometric.workflow.WorkFlowModel;
import com.vantar.exception.*;
import com.vantar.web.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/radio/metric/flow/update",
    "/ui/radio/metric/image/upload",
    "/ui/radio/metric/measurement/submit",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class FlowUploadController extends RouteToMethod {

    public void radioMetricMeasurementSubmit(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, WorkFlowModel.measurementSubmit(params));
    }

    public void radioMetricFlowUpdate(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, WorkFlowModel.update(params));
    }

    public void radioMetricImageUpload(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, WorkFlowModel.uploadImages(params));
    }
}