package com.tctools.business.dto.user;

import com.vantar.database.dto.*;


@Mongo
public class Test1C extends DtoBase {

    public Long id;

    @Depends(Test1C.class)
    public Test1C test1C;
    public String name;

}
