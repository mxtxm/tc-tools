package com.tctools.business.dto.project.hseaudit;

import com.tctools.business.dto.location.*;
import com.vantar.database.dto.*;

@Cache
@Mongo
@Index({"regionId:1", "provinceId:1", "name:1",})
public class SubContractor extends DtoBase {

    public Long id;

    @Required
    public Long regionId;
    @Required
    public Long provinceId;

    @Required
    public String name;
    public String owner;
    public String manager;

    public String email;
    public String mobile;


    @Storage("SubContractor")
    public static class Viewable extends DtoBase {

        public Long id;

        @FetchCache(dto = com.tctools.business.dto.location.Region.class, field = "regionId")
        public Region.Localed region;
        @FetchCache(dto = com.tctools.business.dto.location.Province.class, field = "provinceId")
        public Province.Localed province;

        public String name;
        public String owner;
        public String manager;

        public String email;
        public String mobile;

    }
}