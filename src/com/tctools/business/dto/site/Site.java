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

    //@Depends(BtsStatus.class)
    public Long btsStatusId;
    //@Depends(BtsOwnership.class)
    public Long btsOwnershipId;
    //@Depends(SiteClass.class)
    public Long siteClassId;
    //@Depends(SiteType.class)
    public Long siteTypeId;
    //@Depends(SiteOwnership.class)
    public Long siteOwnershipId;
    //@Depends(BtsInstall.class)
    public Long btsInstallId;

    public String towerHeight;
    public String buildingHeight;
    //@Depends(BtsTowerType.class)
    public Long btsTowerTypeId;
    public String frequencyBand;

    // sector
    public List<Sector> sectors;

    // location > > >
    //@Depends(Region.class)
    public Long regionId;
    @Required
    //@Depends(Province.class)
    public Long provinceId;
    @Required
    //@Depends(City.class)
    public Long cityId;
    //@Depends(District.class)
    public Long districtId;
    //@Depends(LocationType.class)
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

        @FetchCache(dto = BtsStatus.class, field = "btsStatusId")
        public BtsStatus.Localed btsStatus;
        @FetchCache(dto = BtsOwnership.class, field = "btsOwnershipId")
        public BtsOwnership.Localed btsOwnership;
        @FetchCache(dto = SiteClass.class, field = "siteClassId")
        public SiteClass.Localed siteClass;
        @FetchCache(dto = SiteType.class, field = "siteTypeId")
        public SiteType.Localed siteType;
        @FetchCache("siteOwnershipId")
        public SiteOwnership siteOwnership;
        @FetchCache(dto = BtsInstall.class, field = "btsInstallId")
        public BtsInstall.Localed btsInstall;
        @FetchCache(dto = BtsTowerType.class, field = "btsTowerTypeId")
        public BtsTowerType btsTowerType;

        public String buildingHeight;
        public String towerHeight;
        public String frequencyBand;

        // sectors > > >
        public List<Sector.Viewable> sectors;

        // location > > >
        @FetchCache(dto = Region.class, field = "regionId")
        public Region.Localed region;
        @FetchCache(dto = Province.class, field = "provinceId")
        public Province.Localed province;
        @FetchCache(dto = City.class, field = "cityId")
        public City.Localed city;
        @FetchCache("districtId")
        public District district;
        @FetchCache(dto = LocationType.class, field = "locationTypeId")
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

        @FetchCache(dto = Province.class, field = "provinceId")
        public Province.Localed province;
        @FetchCache(dto = City.class, field = "cityId")
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