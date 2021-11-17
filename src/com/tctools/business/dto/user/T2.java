package com.tctools.business.dto.user;

import com.vantar.database.dto.*;


@Cache
@Mongo
public class T2 extends DtoBase {

    public Long id;
    public T4 t;
    public String name;

}
