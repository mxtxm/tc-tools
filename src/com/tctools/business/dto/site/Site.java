package com.tctools.business.dto.site;

import com.tctools.business.dto.location.*;
import com.vantar.database.datatype.Location;
import com.vantar.database.dto.Date;
import com.vantar.database.dto.*;
import com.vantar.util.datetime.DateTime;
import java.util.*;

@Mongo
@Index({"code:1", "location:2dsphere",})
public class Site extends DtoBase {

    public Long id;
    @Required
    public String code;

    @Localized
    public Map<String, String> name;

    // BSC RNC
    public String controller;

    @Date
    public DateTime setupDate;
    @Date
    public DateTime contractStartDate;

    public String comments;

    public Long btsStatusId;
    public Long btsOwnershipId;
    public Long siteClassId;
    public Long siteTypeId;
    public Long siteOwnershipId;
    public Long btsInstallId;

    public String towerHeight;
    public String buildingHeight;
    public Long btsTowerTypeId;
    public String frequencyBand;

    // sector
    public List<Sector> sectors;

    // location > > >
    public Long regionId;
    @Required
    public Long provinceId;
    @Required
    public Long cityId;
    public Long districtId;
    public Long locationTypeId;
    public String address;
    @Required
    public Location location;

    // collocation > > >
    @Default("0")
    public Integer etcTransceiverCount;
    public CollocationType collocationType;
    public List<Collocation> collocations;


    @Override
    public boolean beforeInsert() {
        return beforeUpdate();
    }

    @Override
    public boolean beforeUpdate() {
        code = code.toUpperCase();
        return true;
    }



    @Storage("Site")
    public static class Viewable extends DtoBase {

        public Long id;
        public String code;

        @DeLocalized
        public String name;

        // BSC RNC
        public String controller;

        public DateTime setupDate;
        public String setupDateFa;
        public DateTime contractStartDate;
        public String contractStartDateFa;

        public String comments;

        @FetchCache(dto = com.tctools.business.dto.site.BtsStatus.class, field = "btsStatusId")
        public BtsStatus.Localed btsStatus;
        @FetchCache(dto = com.tctools.business.dto.site.BtsOwnership.class, field = "btsOwnershipId")
        public BtsOwnership.Localed btsOwnership;
        @FetchCache(dto = com.tctools.business.dto.site.SiteClass.class, field = "siteClassId")
        public SiteClass.Localed siteClass;
        @FetchCache(dto = com.tctools.business.dto.site.SiteType.class, field = "siteTypeId")
        public SiteType.Localed siteType;
        @FetchCache("siteOwnershipId")
        public SiteOwnership siteOwnership;
        @FetchCache(dto = com.tctools.business.dto.site.BtsInstall.class, field = "btsInstallId")
        public BtsInstall.Localed btsInstall;
        @FetchCache(dto = com.tctools.business.dto.site.BtsTowerType.class, field = "btsTowerTypeId")
        public BtsTowerType btsTowerType;

        public String buildingHeight;
        public String towerHeight;
        public String frequencyBand;

        // sectors > > >
        public List<Sector.Viewable> sectors;

        // location > > >
        @FetchCache(dto = com.tctools.business.dto.location.Region.class, field = "regionId")
        public Region.Localed region;
        @FetchCache(dto = com.tctools.business.dto.location.Province.class, field = "provinceId")
        public Province.Localed province;
        @FetchCache(dto = com.tctools.business.dto.location.City.class, field = "cityId")
        public City.Localed city;
        @FetchCache("districtId")
        public District district;
        @FetchCache(dto = com.tctools.business.dto.location.LocationType.class, field = "locationTypeId")
        public LocationType.Localed locationType;
        public String address;
        public Location location;
        // < < < location

        // collocation > > >
        public Integer etcTransceiverCount;
        public CollocationType collocationType;
        public List<Collocation.Viewable> collocations;


        @Override
        public void afterFetchData() {
            if (setupDate != null) {
                setupDateFa = setupDate.formatter().getDateTimePersianHm();
            }
            if (contractStartDate != null) {
                contractStartDateFa = contractStartDate.formatter().getDateTimePersianHm();
            }
        }
    }



    @Storage("Site")
    public static class ViewableTiny extends DtoBase {

        public Long id;
        public String code;

        public Long btsStatusId;

        @DeLocalized
        public String name;

        @FetchCache(dto = com.tctools.business.dto.location.Province.class, field = "provinceId")
        public Province.Localed province;
        @FetchCache(dto = com.tctools.business.dto.location.City.class, field = "cityId")
        public City.Localed city;
        public String address;
        public Location location;
    }



    @Storage("Site")
    public static class ViewableIdCode extends DtoBase {

        public Long id;
        public String code;
    }
}