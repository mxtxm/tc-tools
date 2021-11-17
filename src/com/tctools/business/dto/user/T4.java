package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import java.util.List;


@Cache
@NoStore
public class T4 extends DtoBase {

    public Long id;
    public List<T10> s;
    public String name;

}
