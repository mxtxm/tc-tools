package com.tctools.business.dto.site;

import com.vantar.database.dto.*;
import java.util.Map;

// نوع مالکیت
@Cache
@Mongo
public class BtsOwnership extends DtoBase {

    public Long id;

    @Localized
    @Required
    public Map<String, String> name;


    @Storage("BtsOwnership")
    public static class Localed extends DtoBase {

        public Long id;
        @DeLocalized
        public String name;

    }
}