package com.tctools.business.dto.site;

import com.vantar.database.dto.*;
import java.util.Map;

@Cache
@Mongo
public class Operator extends DtoBase {

    public Long id;

    @Required
    @com.vantar.database.dto.Localized
    public Map<String, String> name;


    @Storage("Operator")
    public static class Localized extends DtoBase {

        public Long id;
        @DeLocalized
        public String name;

    }
}