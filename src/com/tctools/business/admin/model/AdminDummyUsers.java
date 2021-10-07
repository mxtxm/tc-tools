package com.tctools.business.admin.model;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.Admin;
import com.vantar.locale.*;
import com.vantar.locale.Locale;
import com.vantar.service.Services;
import com.vantar.service.auth.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class AdminDummyUsers {

    public static void index(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }

        if (params.contains("f")) {
            String role = params.getString("role");
            if (StringUtil.isNotEmpty(role)) {
                User dummy = new User();
                dummy.id = 1L;
                dummy.role = Role.valueOf(role);
                dummy.accessStatus = AccessStatus.ENABLED;
                dummy.firstName = "Dummy";
                dummy.lastName = "User";
                dummy.fullName = "Dummy User";
                dummy.email = "dummy@user.com";
                dummy.mobile = "12345678";
                dummy.createT = new DateTime();
                dummy.signinT = new DateTime();
                dummy.projectTypes = new ArrayList<>();
                dummy.projectTypes.addAll(Arrays.asList(ProjectType.values()));
                dummy.setToken("DUMMY-AUTH-TOKEN");
                Services.get(ServiceAuth.class).setDummyUser(dummy);
            }
        }

        CommonUser user = Services.get(ServiceAuth.class).getDummyUser();
        if (user == null) {
            ui.addMessage(Locale.getString(AppLangKey.NO_DUMMY_USER_SET));
        } else {
            ui.addMessage(Locale.getString(AppLangKey.DUMMY_USER_SET));
            ui.addMessage(user.toString());
        }

        String[] roles = new String[Role.values().length];
        int i = 0;
        for (Role role : Role.values()) {
            roles[i++] = role.name();
        }

        ui  .beginFormPost()
            .addSelect(Locale.getString(AppLangKey.ROLE), "role", roles)
            .addSubmit()
            .containerEnd();

        ui.finish();
    }
}
