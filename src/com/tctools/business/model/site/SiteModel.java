package com.tctools.business.model.site;

import com.tctools.business.dto.site.Site;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.business.*;
import com.vantar.database.datatype.Location;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import java.util.*;


public class SiteModel {

    private static final double DEFAULT_NEAR_SITE_RADIUS_METER = 1000D;


    public static ResponseMessage insert(Params params) throws InputException, ServerException {
        return CommonModelMongo.insertJson(params, new Site());
    }

    public static ResponseMessage update(Params params) throws InputException, ServerException {
        return CommonModelMongo.updateJson(params, new Site());
    }

    public static ResponseMessage delete(Params params) throws InputException, ServerException {
        return CommonModelMongo.delete(params, new Site());
    }

    public static Site.Viewable getByIdOrCode(Params params) throws VantarException {
        Long id = params.getLong("id");
        String code = params.getString("code");
        if (NumberUtil.isIdInvalid(id) && StringUtil.isEmpty(code)) {
            throw new InputException(AppLangKey.INVALID_SITE);
        }

        QueryBuilder q = new QueryBuilder(new Site.Viewable());
        if (id != null) {
            q.condition().equal("id", id);
        } else {
            q.condition().equal("code", code);
        }
        return CommonModelMongo.getFirst(params, q);
    }

    public static Object search(Params params) throws VantarException {
        return CommonModelMongo.searchX(params, new Site(), new Site.Viewable());
    }

    public static List<Site.ViewableTiny> autoComplete(Params params) throws ServerException {
        String code = params.getString("code");
        if (StringUtil.isEmpty(code) || code.length() < 3) {
            return new ArrayList<>(1);
        }

        QueryBuilder q = new QueryBuilder(new Site(), new Site.ViewableTiny());
        q.sort("code asc");
        q.condition().like("code", code);

        try {
            return CommonModelMongo.getData(params, q);
        } catch (NoContentException e) {
            return new ArrayList<>(1);
        }
    }

    public static List<Site.Viewable> getNearSites(Params params) throws ServerException, InputException {
        Location location = new Location(params.getDouble("latitude"), params.getDouble("longitude"));
        if (location.isEmpty()) {
            throw new InputException(AppLangKey.INVALID_LOCATION);
        }

        boolean exclude = params.getBoolean("exclude", false);

        QueryBuilder q = new QueryBuilder(new Site.Viewable());
        q.condition(QueryOperator.AND).near("location", location, params.getDouble("radius", DEFAULT_NEAR_SITE_RADIUS_METER));

        try {
            List<Site.Viewable> items = CommonModelMongo.getData(params, q);
            if (exclude) {
                for (int i = 0, l = items.size(); i < l; ++i) {
                    Site.Viewable item = items.get(i);
                    if (item.location.equals(location, Location.M1)) {
                        items.remove(i);
                        break;
                    }
                }
            }
            return items;
        } catch (NoContentException e) {
            return new ArrayList<>(1);
        }
    }

    public static List<String> sitesCodes(Params params) throws ServerException {
        List<String> codes = new ArrayList<>(40000);
        try {
            List<Site> sites = CommonRepoMongo.getAll(new Site());
            for (Site site : sites) {
                codes.add(site.code);
            }
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        } catch (NoContentException ignore) {

        }
        return codes;
    }
}
