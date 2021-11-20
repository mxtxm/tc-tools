package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.List;


// 11,12,13,14
@Mongo
public class Test9 extends DtoBase {

    public Long id;
    public String name;

    public List<Test7> test7;

}
