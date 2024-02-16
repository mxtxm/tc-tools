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
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import java.util.*;

@Cache
@Mongo
@Storage("Users")
@Index({"username:1"})
public class User extends DtoBase implements CommonUser, CommonUserPassword {

    public Long id;

    @Required
    public AccessStatus accessStatus;

    @Required
    public Role role;

    public List<ProjectType> projectTypes;

    public List<Long> provinceIds;

    @Required
    public String firstName;
    @Required
    public String lastName;
    //@Tags("none")
    public String fullName;

    public String email;
    public String mobile;
    public String username;
    public String password;

    @CreateTime
    @Timestamp
    //@Tags("none")
    public DateTime createT;

    @UpdateTime
    @Timestamp
    //@Tags("none")
    public DateTime signinT;

    @NoStore
    public String signature;

    @NoStore
    public String token;


    @Override
    public void afterFetchData() {
        signature = getSignature(true);
    }

    @Override
    public boolean beforeInsert() {
        return beforeUpdate();
    }

    public boolean beforeUpdate() {
        if (StringUtil.isNotEmpty(password)) {
            password = DigestUtils.sha1Hex(password);
        } else {
            password = null;
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
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String s) {
        password = s;
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
    public Map<String, Object> getExtraData() {
        return null;
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
        return Param.USERS_URL + id + "/" + id + "-signature.jpg";
    }
}
