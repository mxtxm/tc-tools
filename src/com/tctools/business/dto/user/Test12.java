package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.*;

@Mongo
public class Test12 extends DtoBase {

    public Long id;
    public String name;

    public Map<Long, Test12A> test12AMap;

}
