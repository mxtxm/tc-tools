package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.Set;


@Mongo
public class Test1B extends DtoBase {

    public Long id;

    @Depends(Test1B.class)
    public Set<Long> test1Bids;
    public String name;

}
