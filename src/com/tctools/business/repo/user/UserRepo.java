package com.tctools.business.repo.user;

import com.tctools.business.dto.user.User;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.CommonUser;
import com.vantar.service.cache.ServiceDtoCache;
import java.util.List;


public class UserRepo {

    public CommonUser getUserForAuth(String username) throws NoContentException {
        List<User> users;
        try {
            users = Services.get(ServiceDtoCache.class).getList(User.class);
        } catch (ServiceException e) {
            throw new NoContentException();
        }
        for (User u : users) {
            if (u.username.equals(username)) {
                return u;
            }
        }
        throw new NoContentException();
    }
}