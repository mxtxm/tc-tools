package com.tctools.business.dto.user;

import com.vantar.database.dto.*;

@NoStore
public class Test7 extends DtoBase {

    public Long id;
    public String name;

    @Depends(Test1.class)
    public Long test1Id;

}
