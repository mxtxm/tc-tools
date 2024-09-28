package com.tctools.business.model.project.radiometric.workflow;

import com.tctools.business.dto.project.map.radiometric.RadioMetricMapFlow;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.PageData;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.number.NumberUtil;
import com.vantar.web.*;
import java.util.*;


public class MapFlowModel {

    public static List<RadioMetricMapFlow> searchForMap(Params params) throws VantarException {
        PageData data = Db.modelMongo.search(
            params,
            new RadioMetricFlow.ViewableTiny()
        );

        List<RadioMetricMapFlow> items = new ArrayList<>(data.data.size());
        for (Dto dto : data.data) {
            RadioMetricFlow.ViewableTiny flow = (RadioMetricFlow.ViewableTiny) dto;
            if (flow.site.location == null || flow.site.location.isEmpty()) {
                continue;
            }
            RadioMetricMapFlow mFlow = new RadioMetricMapFlow(flow);
            if ("OLD WORKFLOW".equals(flow.comments)) {
                mFlow.assignable = false;
            }
            items.add(mFlow);
        }
        return items;
    }

    public static ResponseMessage updateSpotLocation(Params params) throws VantarException {
        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = params.getLong("id");
        flow.spotLocation = params.getLocation("spotLocation");
        if (flow.siteLocation != null && flow.spotLocation != null) {
            flow.horizontalDistanceFromLocation = flow.spotLocation.getDistanceM(flow.siteLocation).intValue();
        }
        flow.skipBeforeUpdate();
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "RadioMetricFlow.id");
        }
        if (flow.spotLocation == null || !flow.spotLocation.isValid()) {
            throw new InputException(VantarKey.INVALID_VALUE, "RadioMetricFlow.spotLocation");
        }
        return Db.modelMongo.update(new ModelCommon.Settings(flow));
    }
}