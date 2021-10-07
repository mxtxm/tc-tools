package com.tctools.business.dto.site;

import com.vantar.database.dto.*;

// نوع دکل
@Cache
@Mongo
public class BtsTowerType extends DtoBase {

    public Long id;

    @Required
    public String name;

}