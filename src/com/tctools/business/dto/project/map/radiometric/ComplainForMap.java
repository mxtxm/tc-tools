package com.tctools.business.dto.project.map.radiometric;

import com.tctools.business.dto.project.map.UserForMap;
import com.tctools.business.dto.project.radiometric.complain.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.object.ObjectUtil;


public class ComplainForMap {

    public Long id;
    public UserForMap creator;
    public String ccnumber;
    public ComplainType type;
    public DateTime complainTime;
    public DateTime assignTime;
    public Double[] location;


    public ComplainForMap(RadioMetricComplain.ViewableTiny complain) {
        id = complain.id;
        if (complain.creator != null) {
            creator = new UserForMap(complain.creator);
        }
        ccnumber = complain.ccnumber;
        type = complain.type;
        complainTime = complain.complainTime;
        assignTime = complain.assignTime;
        if (complain.location != null && !complain.location.isEmpty()) {
            location = new Double[] {complain.location.latitude, complain.location.longitude};
        }
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}
