package com.tctools.business.admin.model;

import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.Admin;
import com.vantar.locale.*;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;


public class AdminImportExport {

    public static void index(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(AppLangKey.ADMIN_IMPORT_EXPORT), params, response);
        if (ui == null) {
            return;
        }

        ui  .addHeading(Locale.getString(VantarKey.ADMIN_IMPORT))
            .addBlockLink(Locale.getString(AppLangKey.IMPORT_SITE_DATA), "/admin/import/sites")
            .addBlockLink(Locale.getString(AppLangKey.SYNCH_RADIO_METRIC), "/admin/synch/radiometric")
            .addBlockLink(Locale.getString(AppLangKey.SYNCH_HSE_AUDIT), "/admin/synch/hseaudit")
            .addBlockLink(Locale.getString(AppLangKey.SYNCH_HSE_AUDIT_WORK), "/admin/import/hseaudit/work")

            .addEmptyLine()
            .addHeading(Locale.getString(AppLangKey.ADMIN_EXPORT))
            .addBlockLink("HSE Audit Daily", "/ui/hse/audit/daily/report")
            .addBlockLink("HSE Audit by id=flow.id", "/ui/hse/audit/data")

            .addEmptyLine()
            .addBlockLink("Radiometric docx - id=flow.id", "/ui/radio/metric/site/docx")
            .addBlockLink("Radiometric zip - id=flow.id", "/ui/radio/metric/site/zip")
            .addBlockLink("Radiometric assigned - userId=assignee.id", "/ui/radio/metric/site/assigned/excel")
            .addBlockLink("Radiometric wave control", "/ui/radio/metric/report/wavecontrol/excel")

            .addEmptyLine()
            .addHeading(Locale.getString(AppLangKey.ADMIN_FIX))
            .addBlockLink("HSE Audit fix", "/admin/hseaudit/fix")
            .addBlockLink("HSE Audit fix files", "/admin/hseaudit/fix/files")
        ;

        ui.finish();
    }
}
