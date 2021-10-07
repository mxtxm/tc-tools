package com.tctools.business.dto.site;

import com.vantar.database.dto.*;
import java.util.Map;

// تیپ سایت
@Cache
@Mongo
public class SiteType extends DtoBase {

    public Long id;

    @Localized
    @Required
    public Map<String, String> name;


    @Storage("SiteType")
    public static class Localed extends DtoBase {

        public Long id;
        @DeLocalized
        public String name;

    }
}