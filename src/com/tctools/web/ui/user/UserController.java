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

    public void usersGetRoles(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.ADMIN, Role.MANAGER, Role.ENGINEER);
        Response.writeJson(
            response,
            user.role.equals(Role.MANAGER) ?
                EnumUtil.getEnumValues(Role.values(), "ADMIN", "ROOT") :
                EnumUtil.getEnumValues(Role.values(), "ADMIN", "ROOT", "MANAGER")
        );
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void userGetCurrent(Params params, HttpServletResponse response) throws AuthException, NoContentException, ServiceException {
        Response.writeJson(response, UserModel.getCurrentUser(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void userGetById(Params params, HttpServletResponse response) throws ServerException, NoContentException, InputException {
        Response.writeJson(response, UserModel.getUserById(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void usersAll(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        Response.writeJson(response, UserModel.getAll(params, ((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void usersAllKeyval(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        Response.writeJson(response, UserModel.getAsKeyValue(params, ((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void usersTechniciansGet(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        Response.writeJson(response, UserModel.getTechnicians(params, ((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void usersTechniciansKeyval(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        Response.writeJson(response, UserModel.getTechniciansAsKeyValue(params, ((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "READONLY",})
    public void usersAcTechniciansKeyval(Params params, HttpServletResponse response) throws AuthException, ServerException, NoContentException, InputException {
        Response.writeJson(response, UserModel.getAcTechniciansAsKeyValue(params, ((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER",})
    public void userInsert(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException {
        Response.writeJson(response, UserModel.insert(params, ((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void userUpdate(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException {
        Response.writeJson(response, UserModel.update(params, ((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER",})
    public void userDelete(Params params, HttpServletResponse response) throws InputException, ServerException {
        Response.writeJson(response, UserModel.delete(params));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void userSignatureExists(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        Response.writeJson(response, UserModel.signatureExists(((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void userChangePassword(Params params, HttpServletResponse response) throws InputException, AuthException, ServerException {
        Response.writeJson(response, UserModel.changePassword(params, ((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }

    @Access({"ADMIN", "MANAGER", "ENGINEER", "TECHNICIAN", "VENDOR", "READONLY",})
    public void userUnsubscribe(Params params, HttpServletResponse response) throws AuthException, ServerException {
        Response.writeJson(response, UserModel.unsubscribe(((User) Services.get(ServiceAuth.class).getCurrentUser(params))));
    }
}