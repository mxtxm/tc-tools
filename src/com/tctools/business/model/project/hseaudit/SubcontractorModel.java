package com.tctools.business.model.project.hseaudit;

import com.tctools.business.dto.project.hseaudit.SubContractor;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.Params;
import java.util.*;


public class SubcontractorModel {

    public static Object search(Params params) throws ServerException, NoContentException, InputException {
        QueryData queryData = params.getQueryData();
        if (queryData == null) {
            throw new InputException(VantarKey.NO_SEARCH_COMMAND);
        }
        queryData.setDto(new SubContractor(), new SubContractor.Viewable());

        try {
            return CommonRepoMongo.search(queryData, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static SubContractor.Viewable get(Params params) throws ServerException, NoContentException, InputException {
        SubContractor subContractor = new SubContractor();
        subContractor.id = params.getLong("id");

        if (NumberUtil.isIdInvalid(subContractor.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (SubContractor.id)");
        }

        QueryBuilder q = new QueryBuilder(subContractor, new RadioMetricFlow.Viewable());
        q.setConditionFromDtoEqualTextMatch();
        try {
            return CommonRepoMongo.getFirst(q, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static Map<Long, String> autoComplete(Params params) throws ServerException {
        String name = params.getString("name");

        QueryBuilder q = new QueryBuilder(new SubContractor(), new SubContractor.Viewable());
        q.sort("name desc");
        if (StringUtil.isNotEmpty(name)) {
            q.condition().like("name", name);
        }

        Map<Long, String> values = new HashMap<>();
        try {
            for (Dto item : CommonRepoMongo.getData(q, params.getLang())) {
                SubContractor.Viewable s = (SubContractor.Viewable) item;
                values.put(s.id, s.name + " - " + s.province.name);
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        } catch (NoContentException ignore) {

        }
        return values;
    }
}