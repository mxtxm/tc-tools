package com.tctools.business.dto.user;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.vantar.database.dto.*;
import com.vantar.exception.AuthException;
import com.vantar.service.auth.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.List;

@Cache
@Mongo
@Storage("users")
@Index({"username:1"})
public class User extends DtoBase implements CommonUser {

    public Long id;

    @Required
    public AccessStatus accessStatus;

    @Required
    public Role role;

    public List<ProjectType> projectTypes;

    @PresentBy("Province")
    public List<Long> provinceIds;

    @Required
    public String firstName;
    @Required
    public String lastName;
    @Tags("none")
    public String fullName;

    public String email;
    public String mobile;
    public String username;
    public String password;

    @CreateTime
    @Timestamp
    @Tags("none")
    public DateTime createT;

    @UpdateTime
    @Timestamp
    @Tags("none")
    public DateTime signinT;

    //public Map<String, Long> dataUpdates;

    @NoStore
    public String signature;

    @NoStore
    public String token;
    private boolean siginingIn;
    private boolean changePassword;


    public String getSignature(boolean isUrl) {
        if (id == null) {
            return null;
        }
        String filePath = Param.USERS_FILES + id + "-signature.jpg";
        if (!isUrl) {
            return filePath;
        }
        if (!FileUtil.exists(filePath)) {
            return null;
        }
        return Param.USERS_URL + id  + "/" + id + "-signature.jpg";
    }

    @Override
    public void afterFetchData() {
        signature = getSignature(true);
        if (Thread.currentThread().getStackTrace().length > 8
            && Thread.currentThread().getStackTrace()[7].getClassName().endsWith("ServiceDtoCache")) {
            return;
        }
        if (!siginingIn) {
            password = "";
        }
    }

    public boolean beforeInsert() {
        return beforeUpdate();
    }

    public boolean beforeUpdate() {
        if (!changePassword) {
            password = null;
        }

        if (StringUtil.isNotEmpty(password)) {
            password = DigestUtils.sha1Hex(password);
        }
        if (firstName != null && lastName != null) {
            fullName = firstName + ' ' + lastName;
        }



        if (StringUtil.isNotEmpty(password)) {
            password = DigestUtils.sha1Hex(password);
        }
        if (firstName != null && lastName != null) {
            fullName = firstName + ' ' + lastName;
        }
        return true;
    }

    @Override
    public boolean passwordEquals(String password) {
        return this.password.equals(DigestUtils.sha1Hex(password));
    }

    @Override
    public void setChangePasswordMode(boolean b) {

    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public AccessStatus getAccessStatus() {
        return accessStatus;
    }

    @Override
    public void nullPassword() {
        password = null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getMobile() {
        return mobile;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public CommonUserRole getRole() {
        return role;
    }

    @Override
    public List<? extends CommonUserRole> getRoles() {
        return null;
    }

    @Override
    public void setSigningIn() {
        siginingIn = true;
    }

    public static User getTemporaryRoot() {
        User dummy = new User();
        dummy.id = 1L;
        dummy.role = Role.ROOT;
        dummy.accessStatus = AccessStatus.ENABLED;
        dummy.firstName = "Dummy";
        dummy.lastName = "Root";
        dummy.fullName = "Dummy Root";
        dummy.email = "dummy@root.com";
        dummy.mobile = "12345678";
        dummy.username = "root";
        dummy.password = "root";
        dummy.createT = new DateTime();
        dummy.signinT = new DateTime();
        return dummy;
    }

    public void projectAccess(ProjectType projectType) throws AuthException {
        if (role == Role.MANAGER || role == Role.ADMIN || role == Role.ROOT) {
            return;
        }
        if (projectTypes != null && projectTypes.contains(projectType)) {
            return;
        }
        throw new AuthException(AppLangKey.NO_PROJECT_ACCESS, projectType.toString());
    }

    public DateTime getDataUpdate(String tag) {
        return null;
        //return dataUpdates == null ? null : new DateTime(dataUpdates.get(tag));
    }

    public void setDataUpdated(String tag) {
//        if (dataUpdates == null) {
//            dataUpdates = new HashMap<>();
//        }
//        dataUpdates.put(tag, DateTime.getTimestamp());
    }
}
