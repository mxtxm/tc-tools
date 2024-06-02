package com.tctools.web.patch;

import com.vantar.exception.*;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/test/t",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class TestController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);

    public void t(Params params, HttpServletResponse response) throws VantarException, FinishException {
    }
}