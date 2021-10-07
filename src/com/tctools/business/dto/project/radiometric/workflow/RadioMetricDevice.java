package com.tctools.business.dto.project.radiometric.workflow;

import com.vantar.database.dto.*;
import com.vantar.util.datetime.DateTime;

@Mongo
public class RadioMetricDevice extends DtoBase {

    public Long id;
    public String title;
    public String manufacturer;
    public String serialNumber;
    public DateTime calibrationExpire;

}
