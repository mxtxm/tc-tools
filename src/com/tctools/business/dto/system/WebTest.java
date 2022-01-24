package com.tctools.business.dto.system;

import com.vantar.database.dto.*;
import com.vantar.util.datetime.DateTime;


@Mongo
public class WebTest extends DtoBase {

    public Long id;

    public String title;
    @Required
    public String url;
    @Required
    public String method;
    public String data;
    public String assertData;

    @CreateTime
    public DateTime createT;

}