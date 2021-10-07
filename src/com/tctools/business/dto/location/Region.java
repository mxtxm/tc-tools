package com.tctools.business.dto.location;

import com.vantar.database.dto.*;
import java.util.Map;

// منطقه
@Cache
@Mongo
public class Region extends DtoBase {

    public Long id;

    @Localized
    @Required
    public Map<String, String> name;


    @Storage("Region")
    public static class Localed extends DtoBase {

        public Long id;
        @DeLocalized
        public String name;

    }
}