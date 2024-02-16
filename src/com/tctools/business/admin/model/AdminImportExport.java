package com.tctools.business.admin.model;

import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.index.Admin;
import com.vantar.exception.FinishException;
import com.vantar.locale.*;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;


public class AdminImportExport {

    public static void index(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(AppLangKey.ADMIN_IMPORT_EXPORT), params, response, true);

        ui  .addHeading(2, Locale.getString(VantarKey.ADMIN_IMPORT))
            .addHrefBlock(Locale.getString(AppLangKey.IMPORT_SITE_DATA), "/admin/import/sites")
            .addHrefBlock(Locale.getString(AppLangKey.SYNCH_RADIO_METRIC), "/admin/synch/radiometric")
            .addHrefBlock(Locale.getString(AppLangKey.SYNCH_HSE_AUDIT), "/admin/synch/hseaudit")
            .addHrefBlock(Locale.getString(AppLangKey.SYNCH_HSE_AUDIT_WORK), "/admin/import/hseaudit/work")

            .addEmptyLine()
            .addHeading(2, Locale.getString(AppLangKey.ADMIN_EXPORT))
            .addHrefBlock("HSE Audit Daily", "/ui/hse/audit/daily/report")
            .addHrefBlock("HSE Audit by id=flow.id", "/ui/hse/audit/data")
            .addHrefBlock("HSE Audit Export reports", "/admin/export/site/reports")

            .addEmptyLine()
            .addHrefBlock("Radiometric docx - id=flow.id", "/ui/radio/metric/site/docx")
            .addHrefBlock("Radiometric zip - id=flow.id", "/ui/radio/metric/site/zip")
            .addHrefBlock("Radiometric assigned - userId=assignee.id", "/ui/radio/metric/site/assigned/excel")
            .addHrefBlock("Radiometric wave control", "/ui/radio/metric/report/wavecontrol/excel")

            .addEmptyLine()
            .addHeading(2, Locale.getString(AppLangKey.ADMIN_FIX))
            .addHrefBlock("HSE Audit fix", "/admin/hseaudit/fix")
            .addHrefBlock("HSE Audit fix files", "/admin/hseaudit/fix/files")
        ;

        ui.finish();
    }
}
