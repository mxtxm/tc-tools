package com.tctools.business.dto.site;

import com.vantar.database.dto.*;

@Cache
@Mongo
public class SectorOptimization extends DtoBase {

    public Long id;

    @Required
    public String name;
}
