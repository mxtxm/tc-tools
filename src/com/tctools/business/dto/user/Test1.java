package com.tctools.business.dto.user;

import com.vantar.database.dto.*;

@Mongo
public class Test1 extends DtoBase {

    public Long id;
    public String name;


    public Test1() {
    }

    public Test1(long l) {
        id = l;
    }
}
