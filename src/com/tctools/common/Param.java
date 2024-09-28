package com.tctools.common;

import com.vantar.common.*;
import com.vantar.util.string.StringUtil;


public class Param extends VantarParam {

    public static final String FILE_UPLOAD = "file";

    public static final long FILE_IMAGE_MIN_SIZE = 5 * 1024;
    public static final long FILE_IMAGE_MAX_SIZE = 10 * 1024 * 1024;

    public static final long FILE_MEASURE_CSV_MIN_SIZE = 1024;
    public static final long FILE_MEASURE_CSV_MAX_SIZE = 100 * 1024;

    public static final int LOCATION_DECIMALS = 6;

    public static final boolean isTest = StringUtil.toBoolean(Settings.config.getProperty("is.test"));
    //public static final String RADIO_METRIC_DIR = isTest ? "/opt/tc-tools/" : "/opt/tc-tools-test/";
    public static final String RADIO_METRIC_DIR = "/opt/tc-tools/";

    public static final String RADIO_METRIC_FILES = RADIO_METRIC_DIR + "files/radiometric/";


    public static final String HSE_AUDIT_FILES = RADIO_METRIC_DIR + "files/hse-audit/";
    public static final String USERS_FILES = RADIO_METRIC_DIR + "files/user/";
    public static final String TEMP_DIR = RADIO_METRIC_DIR + "files/temp/";

    public static final String RADIO_METRIC_URL = "/static/radiometric/";
    public static final String HSE_AUDIT_URL = "/static/hse-audit/";
    public static final String USERS_URL = "/static/user/";

    public static final Integer HSE_CRITICAL_FAIL_THRESHOLD = 1;
    public static final Integer HSE_MAJOR_FAIL_THRESHOLD = 6;

    public static final String HSE_AUDIT_AUDIT_TEMPLATE = RADIO_METRIC_DIR + "templates/hse-audit/audit.docx";
    public static final String HSE_AUDIT_DAILY_TEMPLATE = RADIO_METRIC_DIR + "templates/hse-audit/daily-report.xlsx";

    public static final String RADIO_METRIC_SITE_TEMPLATE = RADIO_METRIC_DIR + "templates/radiometric/site-radiometric.docx";
}
