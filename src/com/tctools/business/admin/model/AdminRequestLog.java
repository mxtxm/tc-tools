package com.tctools.business.admin.model;

import com.vantar.admin.model.Admin;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.json.Json;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class AdminRequestLog {

    private final static int LOG_SIZE = 150;
    private volatile static List<WebServiceLog> webServiceLogs = new ArrayList<>(LOG_SIZE);
    private volatile static boolean capture = false;


    public static void showLog(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_MENU_DOCUMENTS), params, response);
        if (ui == null) {
            return;
        }

        Boolean setCapture = params.getBoolean("capture");
        if (setCapture != null) {
            capture = setCapture;
            if (capture) {
                AdminRequestLog.startLog();
            } else {
                AdminRequestLog.endLog();
            }
        }

        ui.addBlockLink("Capture: " + (capture ? "ON" : "OFF") + " --click to toggle--", "?capture=" + (capture ? "false" : "true"));

        synchronized (webServiceLogs) {
            for (int i = webServiceLogs.size() - 1; i >= 0; --i) {
                WebServiceLog item = webServiceLogs.get(i);
                ui.addHeading(item.dateTime + "  " + item.method + "  " + item.url);
                ui.addPre((item.contentType == null ? "Form data" : item.contentType) + "\n" + item.data);
            }
        }

        ui.finish();
    }

    private static void endLog() {
        RouteToMethod.requestCallback = null;
    }

    private static void startLog() {
        RouteToMethod.requestCallback = new RequestCallback() {

            @Override
            public synchronized void catchRequest(Params params) {
                if (AdminRequestLog.webServiceLogs.size() == LOG_SIZE) {
                    AdminRequestLog.webServiceLogs = new ArrayList<>(LOG_SIZE);
                }

                AdminRequestLog.WebServiceLog item = new AdminRequestLog.WebServiceLog();
                item.url = params.request.getRequestURI();
                if (StringUtil.contains(item.url, "admin")) {
                    return;
                }

                item.dateTime = new DateTime().toString();
                item.method = params.getMethod();
                item.contentType = params.getHeader("content-type");
                if (item.contentType != null && StringUtil.contains(item.contentType, "json")) {
                    item.data = Json.makePretty(params.getJson());
                } else if (item.contentType != null && StringUtil.contains(item.contentType, "multipart")) {
                    item.data = params.getAll().toString();
                } else {
                    item.data = params.getAll().toString();
                }

                //params.request.getPart()
                //Part filePart = request.getPart(name);
                //item.username = ;
                AdminRequestLog.webServiceLogs.add(item);
            }
        };
    }


    public static class WebServiceLog {

        public String dateTime;
        public String username;
        public String method;
        public String url;
        public String data;
        public String contentType;
    }
}
