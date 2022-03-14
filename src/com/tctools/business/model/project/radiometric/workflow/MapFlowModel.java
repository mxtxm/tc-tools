package com.tctools.business.model.project.radiometric.workflow;

import com.tctools.business.dto.project.map.radiometric.RadioMetricMapFlow;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.vantar.business.*;
import com.vantar.database.query.QueryData;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.number.NumberUtil;
import com.vantar.web.*;
import java.util.*;


public class MapFlowModel {

    @SuppressWarnings({"unchecked"})
    public static List<RadioMetricMapFlow> searchForMap(Params params) throws ServerException, NoContentException, InputException {
        QueryData queryData = params.getQueryData();
        if (queryData == null) {
            throw new InputException(VantarKey.NO_SEARCH_COMMAND);
        }
        queryData.setDto(new RadioMetricFlow(), new RadioMetricFlow.ViewableTiny());

        List<RadioMetricMapFlow> items = new ArrayList<>();
        try {
            for (RadioMetricFlow.ViewableTiny flow : (List<RadioMetricFlow.ViewableTiny>) CommonRepoMongo.search(queryData, params.getLang())) {
                if (flow.site.location == null || flow.site.location.isEmpty()) {
                    continue;
                }
                items.add(new RadioMetricMapFlow(flow));
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
        return items;
    }

    public static ResponseMessage updateSpotLocation(Params params) throws InputException, ServerException {
        RadioMetricFlow flow = new RadioMetricFlow();
        flow.id = params.getLong("id");
        flow.spotLocation = params.getLocation("spotLocation");
        flow.skipBeforeUpdate();
        if (NumberUtil.isIdInvalid(flow.id)) {
            throw new InputException(VantarKey.INVALID_ID, "RadioMetricFlow.id");
        }
        if (flow.spotLocation == null || !flow.spotLocation.isValid()) {
            throw new InputException(VantarKey.INVALID_VALUE, "RadioMetricFlow.spotLocation");
        }

        return CommonModelMongo.update(flow);
    }

}