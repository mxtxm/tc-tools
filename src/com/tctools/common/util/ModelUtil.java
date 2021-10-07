package com.tctools.common.util;

import com.vantar.database.dto.Dto;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;


public class ModelUtil {

    public static <D extends Dto, L extends Dto> List<L> getAll(D dto, L localizedDto, String locale) {
        List<L> localized = new ArrayList<>();
        for (Dto d : Services.get(ServiceDtoCache.class).getList(dto.getClass())) {
            localizedDto.set(d, locale);
            localized.add(localizedDto);
            try {
                localizedDto = (L) localizedDto.getClass().getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignore) {

            }
        }
        return localized;
    }

    public static Map<Long, String> getAsKeyValue(Dto dto, String locale) {
        Map<Long, String> keyVals = new LinkedHashMap<>();
        for (Dto d : Services.get(ServiceDtoCache.class).getList(dto.getClass())) {
            keyVals.put(d.getId(), ((Map<String, String>) d.getPropertyValue("name")).get(locale));
        }
        return keyVals;
    }

    public static Map<Long, String> getAcAsKeyValue(Dto dto) {
        Map<Long, String> keyVals = new LinkedHashMap<>();
        for (Dto d : Services.get(ServiceDtoCache.class).getList(dto.getClass())) {
            StringBuilder v = new StringBuilder();
            ((Map<String, String>) d.getPropertyValue("name")).forEach((lang, value) -> v.append(value).append(" - "));
            if (v.length() > 0) {
                v.setLength(v.length() - 3);
            }
            keyVals.put(d.getId(), v.toString());
        }
        return keyVals;
    }

    public static <D extends Dto> List<D> getAll(D dto) {
        return (List<D>) Services.get(ServiceDtoCache.class).getList(dto.getClass());
    }

    public static Map<Long, String> getAsKeyValue(Dto dto) {
        Map<Long, String> keyVals = new LinkedHashMap<>();
        for (Dto d : Services.get(ServiceDtoCache.class).getList(dto.getClass())) {
            keyVals.put(d.getId(), (String) d.getPropertyValue("name"));
        }
        return keyVals;
    }

    public static double round(double d, int decimals) {
        double v = NumberUtil.round(d, decimals);
        if (v > 0) {
            return v;
        }

        String[] floatParts = StringUtil.split(new BigDecimal(d).toPlainString(), '.');
        if (floatParts.length == 1) {
            return d;
        }
        decimals = 0;
        while (floatParts[1].charAt(decimals) == '0') {
            ++decimals;
        }
        return NumberUtil.round(d, ++decimals);
    }
}
