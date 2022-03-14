package com.tctools.business.service.locale;

import com.vantar.locale.*;
import java.util.*;


public class FA implements Translation {

    private static final Map<LangKey, String> tokens;
    private static final String LANG_KEY = "fa";

    static {
        tokens = new HashMap<>(40);
        tokens.put(AppLangKey.IMPORT_SITE_DATA, "ایمپورت داده های سایت ها");
        tokens.put(AppLangKey.SYNCH_RADIO_METRIC, "یکسان سازی داده های پرتوسنجی با سایتها");
        tokens.put(AppLangKey.SYNCH_HSE_AUDIT, "یکسان سازی داده های پرتوسنجی با سایتها");
        tokens.put(AppLangKey.SYNCH_HSE_AUDIT_WORK, "ایمپورت کارهای انجام شده");

        tokens.put(AppLangKey.IMPORT_BTS_FILE, "CSV/EXCEL فایل");
        tokens.put(AppLangKey.IMPORT_BTS_FIELD_ORDER, "ترتیب فیلدها");

        tokens.put(AppLangKey.ADDED, "اضافه شد {0} > {1}");
        tokens.put(AppLangKey.UPDATED, "بروز شد {0} > {1}");

        tokens.put(AppLangKey.INVALID_SITE, "کد یا شناسه فایل وارد نشدده است");
        tokens.put(AppLangKey.INVALID_ASSIGNEE, "کاربری برای واگذاری انتخاب نشده است");
        tokens.put(AppLangKey.INVALID_STATE, "وضعیت نامعتبر است");
        tokens.put(AppLangKey.INVALID_MEASURE_CSV_DATA, "داده های فایل محاسبه خراب است");
        tokens.put(AppLangKey.GPS_DATA_MISSING_CSV_DATA, "فاقد اطلاعات GPS است");
        tokens.put(AppLangKey.DUPLICATE, "تکراری > {0} : {1}");

        tokens.put(AppLangKey.ADMIN_IMAGE_BROWSE, "عکس ها");

        tokens.put(AppLangKey.INVALID_LOCATION, "ظول و عرض جغرافیایی نادرست است");

        tokens.put(AppLangKey.SIGNATURE_EXISTS, "امظا وارد شده است");
        tokens.put(AppLangKey.SIGNATURE_NOT_EXISTS, "امظا وارد نشده است");

        tokens.put(AppLangKey.NO_PROJECT_ACCESS, "این کاربر به پروژه {0} دسترسی ندارد");
        tokens.put(AppLangKey.UNKNOWN_FILE, "فایل بی ربط به داده های ثبت شده {0}");

        tokens.put(AppLangKey.DOCX_CREATE_ERROR, "خطا هنگام ساختن سند");

        tokens.put(AppLangKey.ADMIN_IMPORT_EXPORT, "ایمپورت اکسپورت");
        tokens.put(AppLangKey.ADMIN_EXPORT, "اکسپورت");
        tokens.put(AppLangKey.ADMIN_FIX, "ترمیم داده ها");

        tokens.put(AppLangKey.EXPORT_FAIL, "اکسپورت دچار خطا شد");
        tokens.put(AppLangKey.SIGNIN_SUCCESS, "ورود موفق");
    }

    @Override
    public String getLangKey() {
        return LANG_KEY;
    }

    @Override
    public String getString(LangKey key) {
        return tokens.get(key);
    }

    @Override
    public String getString(String key) {
        return null;
    }
}
