package com.tctools.common.util;

import com.tctools.business.dto.system.Settings;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.VantarException;
import com.vantar.util.json.Json;
import com.vantar.util.string.StringUtil;
import org.slf4j.*;
import java.math.BigDecimal;


public class ExportCommon {

    protected static final Logger log = LoggerFactory.getLogger(ExportCommon.class);


    public static String getValue(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof Double) {
            String s = BigDecimal.valueOf((Double) obj).toPlainString();
            s = StringUtil.rtrim(s, '0');
            s = StringUtil.rtrim(s, '.');
            return s;
        }
        String v = obj.toString();
        if ("undefined".equalsIgnoreCase(v)) {
            return "";
        }
        return v;
    }

    protected static String getValueObj(String obj) {
        return obj == null ? "" : obj;
    }

    protected static double getValueObj(Double obj) {
        return obj == null ? 0 : obj;
    }

    protected static int getValueObj(Integer obj) {
        return obj == null ? 0 : obj;
    }

    protected static String getDateValue(String value) {
        return value == null ? "" : StringUtil.replace(value, '-', '/');
    }

    public static void cache(String key, Object value) {
        try {
            QueryBuilder q = new QueryBuilder(new Settings());
            q.condition().equal("key", key);
            Db.modelMongo.delete(new ModelCommon.Settings(q));
            Settings settings = new Settings();
            settings.key = key;
            settings.value = Json.d.toJson(value);
            Db.modelMongo.insert(new ModelCommon.Settings(settings));
        } catch (Exception e) {
            log.error("! {}=>{}", key, value, e);
        }
    }

    public static String getFromCache(String key) throws VantarException {
        QueryBuilder q = new QueryBuilder(new Settings());
        q.condition().equal("key", key);
        return (String) Db.modelMongo.getFirst(q).getPropertyValue("value");
    }

    public static String numberToMonth(int m) {
        if (m == 1) {
            return " فروردین ";
        } else if (m == 2) {
            return " اردیبهشت ";
        } else if (m == 3) {
            return " خرداد ";
        } else if (m == 4) {
            return " تیر ";
        } else if (m == 5) {
            return " مرداد ";
        } else if (m == 6) {
            return " شهریور ";
        } else if (m == 7) {
            return " مهر ";
        } else if (m == 8) {
            return " آبان ";
        } else if (m == 9) {
            return " آذز ";
        } else if (m == 10) {
            return " دی ";
        } else if (m == 11) {
            return " بهمن ";
        } else if (m == 12) {
            return " اسفند ";
        }
        return "-";
    }
}
