package com.tctools.web.ui.log;

import com.vantar.service.log.ServiceLog;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    //"/ui/app/log",
    //"/ui/app/log/json",
})
public class LogController extends RouteToMethod {

    public void appLog(Params params, HttpServletResponse response) {
        ServiceLog.info("APP", params.getString("data", ""));
    }

    public void appLogJson(Params params, HttpServletResponse response) {
        ServiceLog.info("APP", params.getJson());
    }
}