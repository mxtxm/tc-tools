package com.tctools.business.dto.system;

import com.vantar.database.dto.*;

@Cache
@Mongo
public class Settings extends DtoBase {

    public static final String KEY_ARAS_UPDATE = "aras-update";

    public Long id;

    public String key;
    public String value;

}