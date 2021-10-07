package com.tctools.business.dto.site;

import com.vantar.database.dto.*;

// WLL MCI
@Cache
@Mongo
public class SiteOwnership extends DtoBase {

    public Long id;

    @Required
    public String name;

}