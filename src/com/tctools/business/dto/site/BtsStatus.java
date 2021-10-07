package com.tctools.business.dto.site;

import com.vantar.database.dto.*;
import java.util.Map;

//وضعیت نود
@Cache
@Mongo
public class BtsStatus extends DtoBase {

    public Long id;

    @Localized
    @Required
    public Map<String, String> name;


    @Storage("BtsStatus")
    public static class Localed extends DtoBase {

        public Long id;
        @DeLocalized
        public String name;

    }
}