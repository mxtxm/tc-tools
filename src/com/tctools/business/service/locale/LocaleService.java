package com.tctools.business.service.locale;

import com.vantar.locale.*;
import java.util.HashMap;
import java.util.Map;


public class LocaleService {

    public static void start(LocaleSettings settings) {
        Map<String, Translation> langTokens = new HashMap<>(2);
        langTokens.put("fa", new FA());
        langTokens.put("en", new EN());
        Locale.start(langTokens, settings.getLocaleDefault(), null);
    }
}
