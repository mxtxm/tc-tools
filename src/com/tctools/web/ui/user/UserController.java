package com.tctools.web.ui.user;

import com.tctools.business.dto.user.*;
import com.tctools.business.model.user.UserModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/users/get/roles",

    "/ui/user/get/current",
    "/ui/user/get/by/id",
    "/ui/users/all",
    "/ui/users/all/keyval",
    "/ui/users/technicians/get",
    "/ui/users/technicians/keyval",
    "/ui/users/ac/technicians/keyval",

    "/ui/user/insert",
    "/ui/user/update",
    "/ui/user/delete",

    "/ui/user/signature/exists",

    "/ui/user/change/password",
    "/ui/user/unsubscribe",
})
public class UserController extends RouteToMethod {

    public void usersGetRoles(Params params, HttpServletResponse response) throws AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(
            response,
            user.role.equals(Role.MANAGER) ?
                EnumUtil.getEnumValues(Role.values(), "ADMIN", "ROOT") :
                EnumUtil.getEnumValues(Role.values(), "ADMIN", "ROOT", "MANAGER")
        );
    }

    public void userGetCurrent(Params params, HttpServletResponse response) throws AuthException, NoContentException {
        Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN, Role.VENDOR);
        Response.writeJson(response, UserModel.getCurrentUser(params));
    }

    public void userGetById(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR);
        Response.writeJson(response, UserModel.getUserById(params));
    }

    public void usersAll(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(response, UserModel.getAll(params, user));
    }

    public void usersAllKeyval(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(response, UserModel.getAsKeyValue(params, user));
    }

    public void usersTechniciansGet(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(response, UserModel.getTechnicians(params, user));
    }

    public void usersTechniciansKeyval(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(response, UserModel.getTechniciansAsKeyValue(params, user));
    }

    public void usersAcTechniciansKeyval(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(response, UserModel.getAcTechniciansAsKeyValue(params, user));
    }

    public void userInsert(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, UserModel.insert(params, user));
    }

    public void userUpdate(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, UserModel.update(params, user));
    }

    public void userDelete(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, UserModel.delete(params));
    }

    public void userSignatureExists(Params params, HttpServletResponse response) throws AuthException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, UserModel.signatureExists(user));
    }

    public void userChangePassword(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, UserModel.changePassword(params, user));
    }

    public void userUnsubscribe(Params params, HttpServletResponse response) throws AuthException, ServerException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);
        Response.writeJson(response, UserModel.unsubscribe(user));
    }
}