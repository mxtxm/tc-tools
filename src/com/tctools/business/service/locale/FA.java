package com.tctools.business.service.locale;

import com.vantar.locale.*;
import java.util.*;


public class FA implements Translation {

    private static final Map<LangKey, String> tokens = new HashMap<>();
    private static final Map<String, String> tokensString = new HashMap<>(1);
    private static final String LANG_KEY = "fa";

    static {
        tokens.put(AppLangKey.APP_TITLE, "جعبه ابزار مخابراتی");

        tokens.put(AppLangKey.MOBILE_OR_EMAIL_IS_REQUIRED, "شماره موبایل یا ایمیل باید وارد شود");
        tokens.put(AppLangKey.SIGNUP_SUCCESS, "کاربر با موفقیت ساخته شد");
        tokens.put(AppLangKey.SIGNIN_SUCCESS, "کاربر با موفقیت وارد شد");
        tokens.put(AppLangKey.INVALID_ROLE, "نقض کاربر نادرست است");
        tokens.put(AppLangKey.VERIFICATION_TOKEN_SENT, "کد اهراز هویت فرستاده شد {0}");

        tokens.put(AppLangKey.SIGNUP_MOBILE_VERIFICATION_TOKEN_MESSAGE, "کد اهراز هویت جعبه ابزار مخابراتی:\n{0}");
        tokens.put(AppLangKey.MOBILE_VERIFICATION_TOKEN_MESSAGE, "کد اهراز هویت جعبه ابزار مخابراتی:\n{0}");
        tokens.put(AppLangKey.MOBILE_SUCCESSFULLY_VERIFIED, "شماره موبایل با موفقیت تایید شد");
        tokens.put(AppLangKey.MOBILE_NOT_VERIFIED, "شماره موبایل تایید نشد");

        tokens.put(AppLangKey.SIGNUP_EMAIL_VERIFICATION_TOKEN_MESSAGE, "کد اهراز هویت جعبه ابزار مخابراتی:\n{0}");
        tokens.put(AppLangKey.EMAIL_VERIFICATION_TOKEN_MESSAGE, "کد اهراز هویت جعبه ابزار مخابراتی:\n{0}");
        tokens.put(AppLangKey.EMAIL_SUCCESSFULLY_VERIFIED, "ایمیل با موفقیت تایید شد");
        tokens.put(AppLangKey.EMAIL_NOT_VERIFIED, "ایمیل تایید نشد");

        tokens.put(AppLangKey.SIGNIN_SMS_VERIFICATION_TOKEN_MESSAGE, "کد ورود جعبه ابزار مخابراتی:\n{0}");
        tokens.put(AppLangKey.SIGNIN_EMAIL_VERIFICATION_TOKEN_MESSAGE, "کد ورود جعبه ابزار مخابراتی:\n{0}");

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

        tokens.put(AppLangKey.ADMIN_DUMMY_USERS, "کاربران قلابی");
        tokens.put(AppLangKey.ADMIN_WEBSERVICES, "وب سرویس ها");
        tokens.put(AppLangKey.ADMIN_MAP, "نقشه");
        tokens.put(AppLangKey.ADMIN_IMAGE_BROWSE, "عکس ها");
        tokens.put(AppLangKey.DUMMY_USER_SET, "کاربر قلابی انتخاب شده");
        tokens.put(AppLangKey.NO_DUMMY_USER_SET, "کاربر قلابی انتخابی نشده");
        tokens.put(AppLangKey.ROLE, "نقش کاربر");

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
        tokens.put(AppLangKey.NOT_POSSIBLE, "عملیات قابل انجام نیست");
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
        return tokensString.get(key);
    }
}
