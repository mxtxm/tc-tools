package com.tctools.business.dto.user;

import com.vantar.database.dto.*;

// 1
@Mongo
public class Test2 extends DtoBase {

    public Long id;
    public String name;

    @Depends(Test1.class)
    public Long test1Id;

}
