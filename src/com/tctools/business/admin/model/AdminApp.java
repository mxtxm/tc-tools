package com.tctools.business.admin.model;

import com.tctools.business.dto.site.Site;
import com.tctools.business.dto.user.Role;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.database.data.panel.DataUtil;
import com.vantar.database.dto.Dto;
import com.vantar.exception.VantarException;
import com.vantar.locale.Locale;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.service.log.dto.*;
import com.vantar.util.file.DirUtil;
import com.vantar.web.Params;
import java.util.*;


public class AdminApp {

    public static DataUtil.Event getAdminDataEvent() {
        return new DataUtil.Event() {
            @Override
            public Dto dtoExchange(Dto dto, String action) {
                if ("list".equalsIgnoreCase(action)) {
                    if (dto.getClass().equals(UserLog.class)) {
                        return new UserLog.Mini();
                    }
                    if (dto.getClass().equals(UserWebLog.class)) {
                        return new UserWebLog.Mini();
                    }
                }

                return dto;
            }

            @Override
            public void beforeInsert(Dto dto) {

            }

            @Override
            public void afterInsert(Dto dto) {
                if (dto instanceof Site) {
                    Site site = (Site) dto;
                    // > > > hook to projects
                    try {
                        AdminSynchRadiometric.synchWithSite(site, null);
                    } catch (VantarException ignore) {

                    }
                    // < < < hook to projects
                }
            }

            @Override
            public void beforeUpdate(Dto dto) {

            }

            @Override
            public void afterUpdate(Dto dto) {

            }

            @Override
            public void beforeDelete(Dto dto) {

            }

            @Override
            public void afterDelete(Dto dto) {

            }
        };
    }

    public static void extendMenu(Params params, Map<String, String> menu) {
        if (Services.get(ServiceAuth.class).hasAccess(params, Role.MANAGER)) {
            menu.put(Locale.getString(AppLangKey.ADMIN_IMPORT_EXPORT), "/admin/import/index");
        }
    }

    public static void extendShortcuts(Params params, Map<String, List<String>> shortcuts) {

    }

    public static void extendMonitoringLinks(Map<String, List<String>> links) {

    }

    public static void factoryResetBefore() {

    }

    public static void factoryResetAfter() {
        DirUtil.makeDirectory("/opt/tc-tools/backup/");
        DirUtil.makeDirectory("/opt/tc-tools/documents/");
        DirUtil.makeDirectory("/opt/tc-tools/files/radiometric/");
        DirUtil.makeDirectory("/opt/tc-tools/files/temp/");
        DirUtil.makeDirectory("/opt/tc-tools/files/user/");
    }
}
