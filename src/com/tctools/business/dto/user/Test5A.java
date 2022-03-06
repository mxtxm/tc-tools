package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.List;


@Mongo
public class Test5A extends DtoBase {

    public Long id;
    public String name;

    @Depends(Test1.class)
    public List<Test1> test1s;

}
