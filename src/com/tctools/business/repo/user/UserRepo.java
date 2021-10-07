package com.tctools.business.repo.user;

import com.tctools.business.dto.user.User;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.CommonUser;
import com.vantar.service.cache.ServiceDtoCache;
import java.util.List;


public class UserRepo {

    public CommonUser getUserForAuth(String username) throws NoContentException {
        List<User> users = Services.get(ServiceDtoCache.class).getList(User.class);
        for (User u : users) {
            if (u.username.equals(username)) {
                return u;
            }
        }
        throw new NoContentException();
    }

    public static void setDataUpdated(Long userId, String tag) throws DatabaseException {
        if (userId == null) {
            return;
        }
        setDataUpdated(Services.get(ServiceDtoCache.class).getDto(User.class, userId), tag);
    }

    public static void setDataUpdated(User user, String tag) throws DatabaseException {
        if (user == null) {
            return;
        }
        //user.setDataUpdated(tag);
        //CommonRepoMongo.update(user);
    }
}