package com.tctools.business.dto.project.radiometric.complain;

import com.tctools.business.dto.location.*;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlowState;
import com.tctools.business.dto.site.Site;
import com.tctools.business.dto.user.User;
import com.tctools.common.Param;
import com.vantar.database.datatype.Location;
import com.vantar.database.dto.*;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import java.util.Map;

@Mongo
@Index({"userId:1", "siteCode:1", "siteId:1", "workFlowId:1"})
public class RadioMetricComplain extends DtoBase {

    public Long id;

    @Required
    //@Depends(User.class)
    public Long creatorId;

    // assigned > > >
    @Required
    @Default("true")
    public Boolean assignable;

    //@Depends(User.class)
    public Long assigneeId;
    // < < <  assigned

    public String ccnumber;
    //@Depends(Site.class)
    public Long siteId;
    //@Depends(RadioMetricFlow.class)
    public Long workFlowId;
    @Required
    public String siteCode;
    @Localized
    public Map<String, String> siteName;

    @Required
    public ComplainType type;

    @CreateTime
    @Timestamp
    public DateTime complainTime;
    @CreateTime
    @Timestamp
    public DateTime assignTime;

    public String comments;

    // location > > >
    public Location location;

    @Required
    //@Depends(Province.class)
    public Long provinceId;
    @Required
    //@Depends(City.class)
    public Long cityId;
    @Required
    public String address;
    // < < < location

    // complainer > > >
    public String complainerName;
    public String complainerMobile;
    public String complainerPhone;
    public String complainerAddress;
    public String complainerComments;

    public RadioMetricFlowState lastState;


    // < < < complainer

    // visit data > > >
    //@Depends(PropertyType.class)
    public Long propertyId;
    //@Depends(PropertySection.class)
    public Long propertySectionId;
    public Double buildingHeight;
    public Integer floorCount;
    public Integer floor;
    public Integer unitNumber;
    // visit data < < <

    public String imageUrl;


    private static String getImageFilenameX(Long cityId, String complainerName, String ccnumber) {
        City city = Services.get(ServiceDtoCache.class).getDto(City.class, cityId);
        return complainerName + " -  درخواست شماره " + ccnumber + " - " + (city == null ? "" : city.name.get("fa")) + ".png";
    }

    private static String getImagePathX(String filename, String siteCode, boolean isUrl, boolean exists) {
        try {
            String filePath = Param.RADIO_METRIC_FILES + siteCode + "/complain/" + filename;
            if (exists && !FileUtil.exists(filePath)) {
                return null;
            }

            return isUrl ? Param.RADIO_METRIC_URL + siteCode + "/complain/" + filename : filePath;
        } catch (Exception e) {
            return null;
        }
    }

    public String getImageFilename() {
        return getImageFilenameX(cityId, complainerName, ccnumber);
    }

    public String getImageFilePath(boolean exists) {
        return getImagePathX(getImageFilename(), siteCode, false, exists);
    }

    public String getImageUrl(boolean exists) {
        return getImagePathX(getImageFilename(), siteCode, true, exists);
    }

    @Override
    public boolean beforeInsert() {
        if (ccnumber == null) {
            ccnumber = "0";
        }
        return beforeUpdate();
    }

    @Override
    public boolean beforeUpdate() {
        address = Site.normaliseAddress(address);
        complainerAddress = Site.normaliseAddress(complainerAddress);

        assignTime = new DateTime();
        if (assigneeId != null && workFlowId != null) {
            assignable = false;
        } else {
            assignable = true;
            assigneeId = null;
            workFlowId = null;
            assignTime = null;
            addNullProperties("assigneeId", "workFlowId", "assignTime");
        }

        imageUrl = getImageUrl(true);
        if (location != null) {
            location.round(Param.LOCATION_DECIMALS);
        }

        return true;
    }

    public static boolean isEmpty(RadioMetricComplain c) {
        return c == null || c.id == null || StringUtil.isEmpty(c.ccnumber);
    }

    public static boolean isEmpty(RadioMetricComplain.Viewable c) {
        return c == null || StringUtil.isEmpty(c.ccnumber);
    }

    public static boolean isEmpty(RadioMetricComplain.ViewableTiny c) {
        return c == null || StringUtil.isEmpty(c.ccnumber);
    }


    @Storage("RadioMetricComplain")
    public static class Viewable extends DtoBase {

        public Long id;

        public Long workFlowId;

        @FetchCache("creatorId")
        public User creator;

        // assigned > > >
        public Boolean assignable;
        @FetchCache("assigneeId")
        public User assignee;
        // < < <  assigned

        public String ccnumber;
        public Long siteId;
        public String siteCode;
        @DeLocalized
        public String siteName;

        public ComplainType type;

        public DateTime complainTime;
        public DateTime assignTime;

        public String comments;

        // location > > >
        public Location location;

        @FetchCache(dto = Province.class, field = "provinceId")
        public Province.Localed province;
        @FetchCache(dto = City.class, field = "cityId")
        public City.Localed city;
        public String address;
        // < < < location

        // complainer > > >
        public String complainerName;
        public String complainerMobile;
        public String complainerPhone;
        public String complainerAddress;
        public String complainerComments;
        // < < < complainer

        // visitor data > > >
        @FetchCache("propertyId")
        public PropertyType property;
        @FetchCache("propertySectionId")
        public PropertySection propertySection;
        public Double buildingHeight;
        public Integer floorCount;
        public Integer floor;
        public Integer unitNumber;
        // visitor data < < <

        // visitor data > > >
        public String imageUrl;

        public RadioMetricFlowState lastState;


        public String getImageFilename() {
            if (city == null) {
                return null;
            }
            return getImageFilenameX(city.id, complainerName, ccnumber);
        }

        public String getImageFilePath(boolean exists) {
            return getImagePathX(getImageFilename(), siteCode, false, exists);
        }

        public String getImageUrl(boolean exists) {
            return getImagePathX(getImageFilename(), siteCode, true, exists);
        }
    }


    @Storage("RadioMetricComplain")
    public static class ViewableTiny extends DtoBase {

        public Long id;
        public Long workFlowId;
        @FetchCache("creatorId")
        public User creator;
        public String ccnumber;
        public ComplainType type;
        public DateTime complainTime;
        public DateTime assignTime;
        public Location location;
        public RadioMetricFlowState lastState;


    }
}
