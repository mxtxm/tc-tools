package com.tctools.web.ui.user;

import com.tctools.business.model.user.UserModel;
import com.vantar.exception.*;
import com.vantar.web.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/user/signature/submit",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class UserUploadController extends RouteToMethod {

    public void userSignatureSubmit(Params params, HttpServletResponse response) throws VantarException {
        Response.writeJson(response, UserModel.signatureSubmit(params));
    }

}