package com.tctools.business.dto.user;

import com.vantar.database.dto.*;


@Mongo
public class Test1A extends DtoBase {

    public Long id;

    @Depends(Test1A.class)
    public Long test1Aid;
    public String name;

}
