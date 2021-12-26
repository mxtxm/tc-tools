package com.tctools.web.ui.datetime;

import com.vantar.util.datetime.DateTime;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;

@WebServlet({
    "/ui/datetime/now",
    "/ui/datetime/now/persian",
    "/ui/datetime/now/time",
    "/ui/datetime/now/date",
    "/ui/datetime/now/date/persian",

    "/ui/datetime/week/persian",

    "/ui/datetime/to/persian",
    "/ui/datetime/to/gregorian",
})
public class DateTimeController extends RouteToMethod {

    public void datetimeNow(Params params, HttpServletResponse response) {
        Response.writeString(response, (new DateTime()).formatter().getDateTime());
    }

    public void datetimeNowPersian(Params params, HttpServletResponse response) {
        Response.writeString(response, (new DateTime()).formatter().getDateTimePersianHm());
    }

    public void datetimeNowTime(Params params, HttpServletResponse response) {
        Response.writeString(response, (new DateTime()).formatter().getTimeHms());
    }

    public void datetimeNowDate(Params params, HttpServletResponse response) {
        Response.writeString(response, (new DateTime()).formatter().getDate());
    }

    public void datetimeNowDatePersian(Params params, HttpServletResponse response) {
        Response.writeString(response, (new DateTime()).formatter().getDatePersian());
    }

    public void datetimeToPersian(Params params, HttpServletResponse response) {
        DateTime dt = params.getDateTime("value");
        Response.writeString(response, dt.formatter().getDatePersian());
    }

    public void datetimeWeekPersian(Params params, HttpServletResponse response) {
        Response.writeString(response, (new DateTime()).addDays(7).formatter().getDateTimePersianHm());
    }

    public void datetimeToGregorian(Params params, HttpServletResponse response) {
        DateTime dt = params.getDateTime("value");
        Response.writeString(response, dt.formatter().getDate());
    }
}