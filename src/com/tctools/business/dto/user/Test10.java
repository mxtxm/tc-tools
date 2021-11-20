package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.List;


// 15,16,17,18
@Mongo
public class Test10 extends DtoBase {

    public Long id;
    public String name;

    public List<Test6> test6;

}
