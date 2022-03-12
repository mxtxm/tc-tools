package com.tctools.business.model.user;

import com.tctools.business.dto.user.User;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.*;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.web.*;


public class AuthModel {

    public static ResponseMessage signin(Params params) throws AuthException, ServerException {
        return ResponseMessage.success(AppLangKey.SIGNIN_SUCCESS, Services.get(ServiceAuth.class).signin(params));
    }

    public static void signout(Params params) throws ServiceException {
        Services.get(ServiceAuth.class).signout(params);
    }

    public static ServiceAuth.SigninBundle getUserForAuth(String username) throws NoContentException {
        ServiceDtoCache cache;
        try {
            cache = Services.get(ServiceDtoCache.class);
        } catch (ServiceException ignore) {
            throw new NoContentException();
        }

        ServiceAuth.SigninBundle bundle = new ServiceAuth.SigninBundle();
        for (User u : cache.getList(User.class)) {
            if (username.equals(u.username)) {
                bundle.commonUser = u;
                bundle.commonUserPassword = (CommonUserPassword) cache.getMap(User.class).get(u.id);
                return bundle;
            }
        }
        throw new NoContentException();
    }
}