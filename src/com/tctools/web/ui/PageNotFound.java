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
        LogEvent.info(PageNotFound.class, params.getCurrentUrl());
        Response.notFound(response, "404 Page not found: " + params.getCurrentUrl());
    }
}