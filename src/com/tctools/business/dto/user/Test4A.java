package com.tctools.business.dto.user;

import com.vantar.database.dto.*;


@Mongo
public class Test4A extends DtoBase {

    public Long id;
    public String name;

    @Depends(Test1.class)
    public Test1 test1;

}
