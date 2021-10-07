package com.tctools.business.dto.site;

import com.vantar.database.dto.*;

@NoStore
public class Collocation extends DtoBase {

    public CollocationType type;
    public Long operatorId;


    public Collocation() {

    }

    public Collocation(CollocationType type, Long operatorId) {
        this.type = type;
        this.operatorId = operatorId;
    }


    public static class Viewable extends DtoBase {

        public CollocationType type;

// todo this does not work
//        @FetchCache(dto = com.tctools.business.dto.site.Collocation.class, field = "operatorId")
        @Fetch("operatorId")
        public Operator.Localized operator;
    }
}