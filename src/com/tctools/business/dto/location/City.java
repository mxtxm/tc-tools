package com.tctools.business.dto.location;

import com.vantar.database.dto.*;
import java.util.Map;

// شهر
@Index("provinceId:1")
@Cache
@Mongo
public class City extends DtoBase {

    public Long id;

    @Required
    @Depends(Province.class)
    public Long provinceId;

    @Localized
    @Required
    public Map<String, String> name;


    @Storage("City")
    public static class Localed extends DtoBase {

        public Long id;
        public Long provinceId;
        @DeLocalized
        public String name;

    }
}