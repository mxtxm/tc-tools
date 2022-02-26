package com.tctools.business.repo.user;

import com.tctools.business.dto.user.User;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.*;
import com.vantar.service.cache.ServiceDtoCache;


public class UserRepo {

    public ServiceAuth.SigninBundle getUserForAuth(String username) throws NoContentException {
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