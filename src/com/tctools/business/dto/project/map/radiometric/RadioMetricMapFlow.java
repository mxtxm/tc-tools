package com.tctools.business.dto.project.map.radiometric;

import com.tctools.business.dto.project.map.*;
import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.object.ObjectUtil;


public class RadioMetricMapFlow {

    public Long id;
    public Boolean assignable;
    public SiteForMap site;
    public ComplainForMap complain;

    public UserForMap assignor;
    public UserForMap assignee;
    public RadioMetricFlowState lastState;
    public DateTime lastStateDateTime;

    public String siteAddress;
    public Double[] siteLocation;

    public String spotAddress;
    public Double[] spotLocation;

    public Double coverage;

    public Double densityAverage6min100;
    public Double densityAverage6min150;
    public Double densityAverage6min170;

    public boolean isCc;
    public boolean isRe;


    public RadioMetricMapFlow(RadioMetricFlow.ViewableTiny flow) {
        id = flow.id;

        // probably redundant
        assignable = RadioMetricFlow.isAssignable(flow.lastState, flow.site.btsStatusId == null ? null : flow.site.btsStatusId);

        site = new SiteForMap(flow.site);
        if (!RadioMetricComplain.isEmpty(flow.complain)) {
            complain = new ComplainForMap(flow.complain);
        }

        if (flow.assignor != null) {
            assignor = new UserForMap(flow.assignor);
        }
        if (flow.assignee != null) {
            assignee = new UserForMap(flow.assignee);
        }
        lastState = flow.lastState;
        lastStateDateTime = flow.lastStateDateTime;

        siteAddress = flow.siteAddress;
        if (flow.siteLocation != null && !flow.siteLocation.isEmpty()) {
            siteLocation = new Double[] {flow.siteLocation.latitude, flow.siteLocation.longitude};
        }

        spotAddress = flow.spotAddress;
        if (flow.spotLocation != null && !flow.spotLocation.isEmpty()) {
            spotLocation = new Double[] {flow.spotLocation.latitude, flow.spotLocation.longitude};
        }

        if (flow.densityAverage6min150 != null && flow.densityAverage6min100 != null && flow.densityAverage6min170 != null) {
            coverage = Math.max(Math.max(flow.densityAverage6min150, flow.densityAverage6min100), flow.densityAverage6min170);
        } else {
            coverage = 0D;
        }

        if (flow.complain == null) {
            isCc = false;
            isRe = false;
        } else {
            if (flow.complain.type == null || flow.complain.type.equals(ComplainType.NormalRequest)) {
                isCc = false;
                isRe = true;
            } else {
                isCc = true;
                isRe = false;
            }
        }
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}
