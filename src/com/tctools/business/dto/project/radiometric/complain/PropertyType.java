package com.tctools.business.dto.project.radiometric.complain;

import com.vantar.database.dto.*;

@Cache
@Mongo
public class PropertyType extends DtoBase  {

    public Long id;

    @Required
    public String name;

}
