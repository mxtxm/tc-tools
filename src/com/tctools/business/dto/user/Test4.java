package com.tctools.business.dto.user;

import com.vantar.database.dto.*;

// 4 >>> NOT dependency
@Mongo
public class Test4 extends DtoBase {

    public Long id;
    public String name;

    public Test1 test1;

}
