package com.tctools.web.ui.user;

import com.tctools.business.model.user.AuthModel;
import com.vantar.exception.*;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/user/signin",
    "/ui/user/signout",
})
public class AuthController extends RouteToMethod {

    public void userSignin(Params params, HttpServletResponse response) throws ServerException, AuthException {
        Response.writeJson(response, AuthModel.signin(params));
    }

    public void userSignout(Params params, HttpServletResponse response) {
        AuthModel.signout(params);
        Response.writeSuccess(response);
    }
}