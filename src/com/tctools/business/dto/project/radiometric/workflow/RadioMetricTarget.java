package com.tctools.business.dto.project.radiometric.workflow;

import com.vantar.database.dto.*;

@Cache
@Mongo
public class RadioMetricTarget extends DtoBase {

    public Long id;

    public Integer year;
    public Integer month;
    public Integer value;

}