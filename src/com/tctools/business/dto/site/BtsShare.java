package com.tctools.business.dto.site;

import com.vantar.database.dto.*;
import java.util.Map;

@Cache
@Mongo
public class BtsShare extends DtoBase {

    public Long id;

    @Localized
    @Required
    public Map<String, String> name;


    @Storage("BtsShare")
    public static class Localed extends DtoBase {

        public Long id;
        @DeLocalized
        public String name;

    }
}