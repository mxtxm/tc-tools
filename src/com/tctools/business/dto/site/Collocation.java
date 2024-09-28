package com.tctools.business.dto.site;

import com.vantar.database.dto.DtoBase;
import com.vantar.database.dto.FetchCache;
import com.vantar.database.dto.NoStore;

@NoStore
public class Collocation extends DtoBase {

    public CollocationType type;
    public Long operatorId;

    @Override
    public int hashCode() {
        return operatorId == null ? 0 : operatorId.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        if (this.operatorId == null && ((Collocation) obj).operatorId == null) {
            return true;
        }
        if (this.operatorId == null || ((Collocation) obj).operatorId == null) {
            return false;
        }
        return this.operatorId.equals(((Collocation) obj).operatorId);
    }


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