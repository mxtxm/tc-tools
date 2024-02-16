package com.tctools.business.model.user;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.vantar.business.*;
import com.vantar.database.common.ValidationError;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.Services;
import com.vantar.service.auth.*;
import com.vantar.util.file.*;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import java.util.*;


public class UserModel {

    public static User getCurrentUser(Params params) throws NoContentException, ServiceException, AuthException {
        ServiceAuth auth = Services.get(ServiceAuth.class);
        User user = (User) auth.getCurrentUser(params);
        if (user == null) {
            throw new NoContentException();
        }
        return user;
    }

    public static User getUserById(Params params) throws VantarException {
        Long id = params.getLong("id");
        if (!NumberUtil.isIdValid(id)) {
            throw new InputException(VantarKey.INVALID_ID, "User.id");
        }

        QueryBuilder q = new QueryBuilder(new User());
        q.condition().equal("id", id);
        return ModelMongo.getFirst(q);
    }

    public static List<User> getAll(Params params, User user) throws VantarException {

        return getUsers(params.getString("project"), null, user);
    }

    public static Map<Long, String> getAsKeyValue(Params param, User user) throws VantarException {

        Map<Long, String> users = new HashMap<>();
        for (User u : getAll(param, user)) {
            users.put(u.id, u.fullName);
        }
        return users;
    }

    public static List<User> getTechnicians(Params params, User user) throws VantarException {
        return getUsers(params.getString("project"), Role.TECHNICIAN, user);
    }

    public static Map<Long, String> getTechniciansAsKeyValue(Params params, User user) throws VantarException {
        Map<Long, String> users = new HashMap<>();
        for (User u : getTechnicians(params, user)) {
            users.put(u.id, u.fullName);
        }
        return users;
    }

    public static Map<Long, String> getAcTechniciansAsKeyValue(Params params, User user) throws VantarException {
        Map<Long, String> users = new HashMap<>();
        for (User u : getTechnicians(params, user)) {
            users.put(u.id, u.fullName + " (" + u.username + ")");
        }
        return users;
    }

    private static List<User> getUsers(String project, Role role, User user)
        throws VantarException {

        if (user.role == Role.ENGINEER) {
            if (project == null) {
                throw new AuthException(VantarKey.REQUIRED, "project");
            }

            ProjectType projectType;
            try {
                projectType = ProjectType.valueOf(project);
            } catch (IllegalArgumentException e) {
                throw new InputException(VantarKey.INVALID_VALUE, "project");
            }

            if (!user.projectTypes.contains(projectType)) {
                throw new AuthException(AppLangKey.NO_PROJECT_ACCESS, project);
            }
        }

        QueryBuilder q = new QueryBuilder(new User());
        q.condition().equal("accessStatus", AccessStatus.ENABLED);
        if (project != null) {
            q.condition().in("projectTypes", project);
        }
        if (role != null) {
            q.condition().equal("role", role);
        }

        return ModelMongo.getData(q);
    }

    public static ResponseMessage insert(Params params, User creator) throws VantarException {
        User user = new User();
        user.setExclude("role", "emailVerified", "mobileVerified", "createT", "signinT");
        return ModelMongo.insert(params, user, new CommonModel.WriteEvent() {

            @Override
            public void beforeSet(Dto dto) {

            }

            @Override
            public void beforeWrite(Dto dto) throws InputException {
                if (creator.role == Role.ENGINEER) {
                    if (((User) dto).role == Role.MANAGER) {
                        throw new InputException("engineer can not create manager");
                    }
                    for (ProjectType projectType : ((User) dto).projectTypes) {
                        if (!creator.projectTypes.contains(projectType)) {
                            throw new InputException("engineer does not have access to project");
                        }
                    }
                }
            }

            @Override
            public void afterWrite(Dto dto) {

            }
        });
    }

    public static ResponseMessage update(Params params, User creator) throws VantarException {
        User user = new User();
        user.setExclude("role", "emailVerified", "mobileVerified", "createT", "signinT", "password");
        return ModelMongo.update(params, user, new CommonModel.WriteEvent() {

            @Override
            public void beforeSet(Dto dto) {

            }

            @Override
            public void beforeWrite(Dto dto) throws InputException {
                User u = (User) dto;
                if (creator.role == Role.ENGINEER) {
                    if (((User) dto).role == Role.MANAGER) {
                        throw new InputException("engineer can not update manager");
                    }
                    for (ProjectType projectType : ((User) dto).projectTypes) {
                        if (!creator.projectTypes.contains(projectType)) {
                            throw new InputException("engineer does not have access to project");
                        }
                    }
                }

                if (u.id == null) {
                    dto.setId(creator.id);
                }
            }

            @Override
            public void afterWrite(Dto dto) {

            }
        });
    }

    public static ResponseMessage delete(Params params) throws VantarException {
        User user = new User();
        return ModelMongo.delete(params, user);
    }

    public static ResponseMessage signatureSubmit(Params params) throws InputException, AuthException, ServiceException {
        try (Params.Uploaded uploaded = params.upload(Param.FILE_UPLOAD)) {
            if (!uploaded.isUploaded() || uploaded.isIoError()) {
                throw new InputException(uploaded.getError());
            }

            List<ValidationError> errors = new ArrayList<>(2);
            if (!uploaded.isType("jpeg", "png")) {
                errors.add(new ValidationError(Param.FILE_UPLOAD, VantarKey.FILE_TYPE, "jpeg, png"));
            }
            if (uploaded.getSize() < Param.FILE_IMAGE_MIN_SIZE || uploaded.getSize() > Param.FILE_IMAGE_MAX_SIZE) {
                errors.add(
                    new ValidationError(
                        Param.FILE_UPLOAD,
                        VantarKey.FILE_SIZE,
                        Param.FILE_IMAGE_MIN_SIZE / 1024 + "KB ~ " + Param.FILE_IMAGE_MAX_SIZE / 1024 + "KB"
                    )
                );
            }
            if (!errors.isEmpty()) {
                throw new InputException(errors);
            }

            String tempPath = FileUtil.getUniqueName(Param.TEMP_DIR);
            uploaded.moveTo(tempPath);

            User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.ADMIN, Role.MANAGER, Role.ENGINEER, Role.VENDOR, Role.TECHNICIAN);

            Long id = params.getLong("id");
            if (NumberUtil.isIdValid(id)) {
                if (user.role == Role.MANAGER || user.role == Role.ADMIN || user.role == Role.ROOT) {
                    user.id = id;
                } else {
                    throw new AuthException(VantarKey.NO_ACCESS);
                }
            }

            FileUtil.move(tempPath, user.getSignature(false));

            return ResponseMessage.success(VantarKey.UPDATE_SUCCESS, user.getSignature(true));
        }
    }

    public static ResponseMessage signatureExists(User user) {
        return
            FileUtil.exists(user.getSignature(false)) ?
                ResponseMessage.success(AppLangKey.SIGNATURE_EXISTS, user.getSignature(true)) :
                ResponseMessage.success(AppLangKey.SIGNATURE_NOT_EXISTS, "");
    }

    public static ResponseMessage changePassword(Params params, User user) throws VantarException {
        Long id = params.getLong("id");
        if (NumberUtil.isIdValid(id)) {
            if (user.role == Role.MANAGER || user.role == Role.ADMIN || user.role == Role.ROOT) {
                user.id = id;
            } else {
                throw new AuthException(VantarKey.NO_ACCESS);
            }
        }

        User u = new User();
        u.id = user.id;
        u.password = params.getString("password");
        if (StringUtil.isEmpty(u.password)) {
            throw new InputException(new ValidationError("password", VantarKey.REQUIRED));
        }
        return updateUser(u);
    }

    public static ResponseMessage unsubscribe(User signedInUser) throws VantarException {
        User user = new User();
        user.id = signedInUser.id;
        user.accessStatus = AccessStatus.UNSUBSCRIBED;
        return updateUser(user);
    }

    private static ResponseMessage updateUser(User user) throws VantarException {
        return ModelMongo.update(user);
    }
}