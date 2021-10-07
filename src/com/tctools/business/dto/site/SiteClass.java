package com.tctools.business.dto.site;

import com.vantar.database.dto.*;
import java.util.Map;

// کلاس سایت
@Cache
@Mongo
public class SiteClass extends DtoBase {

    public Long id;

    @Localized
    @Required
    public Map<String, String> name;


    @Storage("SiteClass")
    public static class Localed extends DtoBase {

        public Long id;
        @DeLocalized
        public String name;

    }
}