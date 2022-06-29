package com.tctools.business.dto.project.radiometric.complain;

import com.vantar.database.dto.*;

@Cache
@Mongo
public class PropertySection extends DtoBase {

    public Long id;

    @Required
    public String name;
}