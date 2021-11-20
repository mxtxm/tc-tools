package com.tctools.business.dto.project.hseaudit;

import com.tctools.business.dto.location.*;
import com.vantar.database.dto.*;

@Cache
@Mongo
@Index({"regionId:1", "provinceId:1", "name:1",})
public class SubContractor extends DtoBase {

    public Long id;

    @Required
    @Depends(Region.class)
    public Long regionId;
    @Required
    @Depends(Province.class)
    public Long provinceId;

    @Required
    public String name;
    public String owner;
    public String manager;

    public String email;
    public String mobile;

    public Boolean isTci;
    public Boolean isNotTci;


    @Storage("SubContractor")
    public static class Viewable extends DtoBase {

        public Long id;

        @FetchCache(dto = Region.class, field = "regionId")
        public Region.Localed region;
        @FetchCache(dto = Province.class, field = "provinceId")
        public Province.Localed province;

        public String name;
        public String owner;
        public String manager;

        public String email;
        public String mobile;

    }
}