package com.tctools.web.ui;

import com.vantar.service.log.LogEvent;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/page/not/found",
})
public class PageNotFound extends RouteToMethod {

    public void notFound(Params params, HttpServletResponse response) {
        String requestedLocation = (String) params.request.getAttribute("javax.servlet.forward.request_uri");
        LogEvent.info(PageNotFound.class, requestedLocation);
        Response.notFound(response, "404 Page not found: " + requestedLocation);
    }
}