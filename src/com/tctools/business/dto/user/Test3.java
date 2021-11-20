package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.List;

// 2, 3
@Mongo
public class Test3 extends DtoBase {

    public Long id;
    public String name;

    @Depends(Test1.class)
    public List<Long> test1Ids;

}
