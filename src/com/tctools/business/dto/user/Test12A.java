package com.tctools.business.dto.user;

import com.vantar.database.dto.*;


@NoStore
public class Test12A extends DtoBase {

    public Long id;
    public String name;
    public Long test1Id;


    public Test12A() {
    }

    public Test12A(Long id, String name, Long test1Id) {
        this.id = id;
        this.name = name;
        this.test1Id = test1Id;
    }

}
