package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.List;


@Mongo
public class Test1D extends DtoBase {

    public Long id;

    @Depends(Test1D.class)
    public List<Test1D> test1Ds;
    public String name;

}
