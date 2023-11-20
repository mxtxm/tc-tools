package com.tctools.business.admin.model;

import com.tctools.business.dto.site.Site;
import com.tctools.business.dto.user.Role;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.AdminData;
import com.vantar.database.dto.Dto;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.util.file.*;
import com.vantar.web.Params;
import java.util.*;


public class AdminApp {

    public static AdminData.Event getAdminDataEvent() {
        return new AdminData.Event() {
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
                        AdminSynchHseAudit.synchWithSite(site, false, null);
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
        try {
            if (Services.get(ServiceAuth.class).hasAccess(params, Role.MANAGER)) {
                menu.put(Locale.getString(AppLangKey.ADMIN_IMPORT_EXPORT), "/admin/import/index");
            }
        } catch (ServiceException ignore) {

        }

        menu.put(Locale.getString(AppLangKey.ADMIN_IMAGE_BROWSE), "/admin/image/browse");
        menu.put("Tools", "/admin/tools/index");
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
        DirUtil.makeDirectory("/opt/tc-tools/files/hse-audit/");
        DirUtil.makeDirectory("/opt/tc-tools/files/radiometric/");
        DirUtil.makeDirectory("/opt/tc-tools/files/temp/");
        DirUtil.makeDirectory("/opt/tc-tools/files/user/");
    }
}
