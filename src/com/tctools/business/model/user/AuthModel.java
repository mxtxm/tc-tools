package com.tctools.business.model.user;

import com.tctools.business.service.locale.AppLangKey;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;


public class AuthModel {

    public static ResponseMessage signin(Params params) throws AuthException, ServerException {
        return new ResponseMessage(AppLangKey.SIGNIN_SUCCESS, Services.get(ServiceAuth.class).signin(params));
    }

    public static void signout(Params params) throws ServiceException {
        Services.get(ServiceAuth.class).signout(params);
    }
}