package com.tctools.business.dto.location;

import com.vantar.database.dto.*;
import java.util.Map;

// تعلق مکانی طراحی
@Cache
@Mongo
public class LocationType extends DtoBase {


    public Long id;

    @Localized
    @Required
    public Map<String, String> name;


    @Storage("LocationType")
    public static class Localed extends DtoBase {

        public Long id;
        @DeLocalized
        public String name;

    }
}