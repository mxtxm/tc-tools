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

        @FetchCache(dto = Operator.class, field = "operatorId")
        public Operator.Localized operator;
    }
}