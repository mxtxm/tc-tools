package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.List;


// 5,6 >>> NOT dependency
@Mongo
public class Test5 extends DtoBase {

    public Long id;
    public String name;

    public List<Test1> test1s;

}
