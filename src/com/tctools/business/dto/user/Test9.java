package com.tctools.business.dto.user;

import com.vantar.database.dto.*;

@NoStore
public class Test9 extends DtoBase {

    public Long id;
    public String name;

    @Depends(Test1.class)
    public Long test1Id;

    public Test9() {

    }

    public Test9(long l, String pool, long l1) {
        id = l;
        name = pool;
        test1Id = l1;
    }
}
