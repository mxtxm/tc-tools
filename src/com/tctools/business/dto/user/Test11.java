package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.*;


@NoStore
public class Test11 extends DtoBase {

    public Long id;
    public String name;

    @Depends(Test1.class)
    public List<Long> test1Ids;

    public Test11() {

    }

    public Test11(long l, String pool, long a, long b) {
        id = l;
        name = pool;
        test1Ids = new ArrayList<>();
        test1Ids.add(a);
        test1Ids.add(b);
    }
}
