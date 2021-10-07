package com.tctools.business.admin.web;

import com.tctools.business.admin.model.AdminDummyUsers;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/admin/users/dummy/index",
})
public class AdminDummyUserController extends RouteToMethod {

    public void usersDummyIndex(Params params, HttpServletResponse response) {
        AdminDummyUsers.index(params, response);
    }

}
