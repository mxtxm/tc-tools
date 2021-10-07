package com.tctools.business.service.locale;

import com.vantar.locale.*;
import java.util.*;


public class EN implements Translation {

    private static final Map<LangKey, String> tokens = new HashMap<>();
    private static final Map<String, String> tokensString = new HashMap<>(1);
    private static final String LANG_KEY = "en";

    static {
        tokens.put(AppLangKey.APP_TITLE, "TC-tools");

        tokens.put(AppLangKey.MOBILE_OR_EMAIL_IS_REQUIRED, "Mobile number or email is required");
        tokens.put(AppLangKey.SIGNUP_SUCCESS, "User created and signed up successfully");
        tokens.put(AppLangKey.SIGNIN_SUCCESS, "User signed in successfully");
        tokens.put(AppLangKey.INVALID_ROLE, "User Role is not valid on this system");
        tokens.put(AppLangKey.VERIFICATION_TOKEN_SENT, "Verification code is sent {0}");

        tokens.put(AppLangKey.SIGNUP_MOBILE_VERIFICATION_TOKEN_MESSAGE, "TC-tools verification code:\n{0}");
        tokens.put(AppLangKey.MOBILE_VERIFICATION_TOKEN_MESSAGE, "TC-tools verification code:\n{0}");
        tokens.put(AppLangKey.MOBILE_SUCCESSFULLY_VERIFIED, "Mobile number verified successfully");
        tokens.put(AppLangKey.MOBILE_NOT_VERIFIED, "Mobile number could not be verified");

        tokens.put(AppLangKey.SIGNUP_EMAIL_VERIFICATION_TOKEN_MESSAGE, "TC-tools verification link:\n{0}");
        tokens.put(AppLangKey.EMAIL_VERIFICATION_TOKEN_MESSAGE, "TC-tools email verification link:\n{0}");
        tokens.put(AppLangKey.EMAIL_SUCCESSFULLY_VERIFIED, "Email verifies successfully");
        tokens.put(AppLangKey.EMAIL_NOT_VERIFIED, "Email could not be verified");

        tokens.put(AppLangKey.SIGNIN_SMS_VERIFICATION_TOKEN_MESSAGE, "TC-tools signin code:\n{0}");
        tokens.put(AppLangKey.SIGNIN_EMAIL_VERIFICATION_TOKEN_MESSAGE, "TC-tools signin code:\n{0}");

        tokens.put(AppLangKey.IMPORT_SITE_DATA, "Import site data");
        tokens.put(AppLangKey.SYNCH_RADIO_METRIC, "Synch Radio Metric data with sites");
        tokens.put(AppLangKey.SYNCH_HSE_AUDIT, "Synch HSE Audit data with sites");
        tokens.put(AppLangKey.SYNCH_HSE_AUDIT_WORK, "Import work data");
        tokens.put(AppLangKey.IMPORT_BTS_FILE, "CSV/EXCEL file");
        tokens.put(AppLangKey.IMPORT_BTS_FIELD_ORDER, "Field order");

        tokens.put(AppLangKey.ADDED, "Added {0} > {1}");
        tokens.put(AppLangKey.UPDATED, "Updated {0} > {1}");

        tokens.put(AppLangKey.INVALID_SITE, "Invalid Site id or code");
        tokens.put(AppLangKey.INVALID_ASSIGNEE, "Assignee user not selected");
        tokens.put(AppLangKey.INVALID_STATE, "Invalid state");
        tokens.put(AppLangKey.INVALID_MEASURE_CSV_DATA, "Corrupted measurement result data, csv file");
        tokens.put(AppLangKey.GPS_DATA_MISSING_CSV_DATA, "GPS data missing, csv file");
        tokens.put(AppLangKey.DUPLICATE, "Duplicate > {0} : {1}");

        tokens.put(AppLangKey.ADMIN_DUMMY_USERS, "Dummy users");
        tokens.put(AppLangKey.ADMIN_WEBSERVICES, "WebServices");
        tokens.put(AppLangKey.ADMIN_MAP, "map");
        tokens.put(AppLangKey.ADMIN_IMAGE_BROWSE, "Photos");
        tokens.put(AppLangKey.DUMMY_USER_SET, "Set dummy user");
        tokens.put(AppLangKey.NO_DUMMY_USER_SET, "No Dummy user");
        tokens.put(AppLangKey.ROLE, "User role");

        tokens.put(AppLangKey.INVALID_LOCATION, "Invalid location");

        tokens.put(AppLangKey.SIGNATURE_EXISTS, "Signature exists");
        tokens.put(AppLangKey.SIGNATURE_NOT_EXISTS, "Signature not exists");
        tokens.put(AppLangKey.NO_PROJECT_ACCESS, "User does not have access to project {0}");
        tokens.put(AppLangKey.UNKNOWN_FILE, "Unrelated file to data '{0}'");

        tokens.put(AppLangKey.DOCX_CREATE_ERROR, "Error while creating docx");

        tokens.put(AppLangKey.ADMIN_IMPORT_EXPORT, "ImportExport");
        tokens.put(AppLangKey.ADMIN_EXPORT, "Export");
        tokens.put(AppLangKey.ADMIN_FIX, "Fix");

        tokens.put(AppLangKey.EXPORT_FAIL, "Export failed");
        tokens.put(AppLangKey.NOT_POSSIBLE, "Action is not possible");
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
