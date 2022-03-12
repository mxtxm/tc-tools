package com.tctools.web.patch;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.vantar.exception.*;
import com.vantar.web.*;
import org.slf4j.*;
import javax.management.MBeanServer;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.management.ManagementFactory;

@WebServlet({
    "/test/t",
    "/test/index",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class TestController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Access("ROOT")
    public void t(Params params, HttpServletResponse response) throws FinishException, DatabaseException, NoContentException, IOException {
        dumpHeap("/opt/tc-tools.hprof", true);
    }

    public static void dumpHeap(String filePath, boolean live) throws IOException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
            server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(filePath, live);



    }

    public void index(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException {

    }
}