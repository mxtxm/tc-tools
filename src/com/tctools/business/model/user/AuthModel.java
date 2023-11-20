package com.tctools.business.model.user;

import com.tctools.business.dto.user.User;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.*;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;


public class AuthModel {

    private static final String SEPARATOR_SIGNIN_USERNAME = ">>>";

    public static ResponseMessage signin(Params params) throws AuthException, ServerException {
        String username = params.getString("username");
        if (username != null && username.contains(SEPARATOR_SIGNIN_USERNAME)) {
            String[] usernames = StringUtil.splitTrim(username, SEPARATOR_SIGNIN_USERNAME);
            params.set("username", usernames[0]);

            User admin = (User) Services.get(ServiceAuth.class).signin(params);
            for (User user : ServiceDtoCache.asList(User.class)) {
                if (user.username.equalsIgnoreCase(usernames[1])) {
                    Services.get(ServiceAuth.class).forceSignin(user);
                    return ResponseMessage.success(AppLangKey.SIGNIN_SUCCESS, (User) user.getClone());
                }
            }
        }
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