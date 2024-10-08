package com.tctools.business.admin.model;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.Site;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.index.Admin;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.service.log.ServiceLog;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class AdminSynchRadiometric {

    public static void index(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        if (!params.contains("f")) {
            ui.beginFormPost()
                .addMessage(Locale.getString(VantarKey.ADMIN_CONFIRM))
                .addEmptyLine()
                .addSubmit(Locale.getString(VantarKey.ADMIN_IMPORT))
                .finish();
            return;
        }

        try {
            for (Dto site : Db.modelMongo.getAll(new Site())) {
                synchWithSite((Site) site, ui);
            }
        } catch (VantarException e) {
            ui.addErrorMessage(e);
        }

        ui.finish();
    }

    public static void synchWithSite(Site site, WebUi ui) throws VantarException {
        String title = "(" + site.code + ") " + (site.name == null ? "???" : site.name.get(Locale.getSelectedLocale())) + "  ";

        RadioMetricFlow flow = new RadioMetricFlow();

        QueryBuilder q = new QueryBuilder(flow);
        q.condition().equal("site.code", site.code);

        if (Db.modelMongo.exists(q)) {
            q = new QueryBuilder(flow);
            q.condition().equal("site.code", site.code);

            try {
                for (Dto dto : Db.modelMongo.getData(q)) {
                    flow = (RadioMetricFlow) dto;

                    if (!(
                        flow.lastState.equals(RadioMetricFlowState.Pending)
                            || flow.lastState.equals(RadioMetricFlowState.Planned)
                            || flow.lastState.equals(RadioMetricFlowState.Problematic)
                            || flow.lastState.equals(RadioMetricFlowState.Revise)
                            || flow.lastState.equals(RadioMetricFlowState.Terminated)
                            || flow.lastState.equals(RadioMetricFlowState.Returned)
                    )) {
                        continue;
                    }

                    flow.site = site;
                    flow.provinceId = site.provinceId;
                    flow.cityId = site.cityId;
                    flow.siteLocation = site.location;
                    flow.siteAddress = site.address;

                    Set<RadioMetricProximityType> types = new HashSet<>(5);
                    if (flow.proximities == null) {
                        flow.proximities = new ArrayList<>(5);
                    } else {
                        for (Proximity p : flow.proximities) {
                            types.add(p.proximityType);
                        }
                    }
                    for (RadioMetricProximityType pt : RadioMetricProximityType.values()) {
                        if (!types.contains(pt)) {
                            Proximity p = new Proximity();
                            p.proximityType = pt;
                            flow.proximities.add(p);
                        }
                    }

                    if (flow.collocationType == null) {
                        flow.collocationType = flow.site.collocationType;
                    }
                    if (flow.collocations == null) {
                        flow.collocations = flow.site.collocations;
                    }

                    Db.modelMongo.update(new ModelCommon.Settings(flow).logEvent(false).mutex(false));
                    if (ui != null) {
                        ui.addMessage(
                            Locale.getString(AppLangKey.UPDATED, flow.getClass().getSimpleName(),
                                flow.site.code) + " " + flow.lastState + " " + flow.id
                        ).write();
                    } else {
                        ServiceLog.log.info("synched radiometric {} {}", flow.site.code, flow.site.id);
                    }
                }
            } catch (NoContentException ignore) {

            }

        } else {
            if (site.address != null) {
                site.address = StringUtil.replace(site.address, " ,", ",");
                site.address = StringUtil.replace(site.address, ", ", ",");
                site.address = StringUtil.replace(site.address, ",", "-");
            }
            flow.site = site;
            flow.state = new ArrayList<>();
            flow.lastStateDateTime = new DateTime();
            flow.state.add(new State(RadioMetricFlowState.Pending, flow.lastStateDateTime));
            flow.lastState = RadioMetricFlowState.Pending;
            flow.provinceId = site.provinceId;
            flow.cityId = site.cityId;
            flow.siteLocation = site.location;
            flow.siteAddress = site.address;

            flow.proximities = new ArrayList<>(5);
            for (RadioMetricProximityType pt : RadioMetricProximityType.values()) {
                Proximity p = new Proximity();
                p.proximityType = pt;
                flow.proximities.add(p);
            }

            if (flow.collocationType == null) {
                flow.collocationType = flow.site.collocationType;
            }
            if (flow.collocations == null) {
                flow.collocations = flow.site.collocations;
            }

            ResponseMessage res = Db.modelMongo.insert(new ModelCommon.Settings(flow).mutex(false).logEvent(false));
            if (ui != null) {
                ui.addMessage(Locale.getString(AppLangKey.ADDED, flow.getClass().getSimpleName(), res.value + " - " + title)).write();
            }
        }
    }

    protected static void removeRemovedSited(WebUi ui) {
        try {
            for (Dto dto : Db.modelMongo.getAll(new RadioMetricFlow())) {
                RadioMetricFlow flow = (RadioMetricFlow) dto;
                if (!AdminSiteImport.siteCodes.contains(flow.site.code)) {
//                    if (RadioMetricFlowState.Completed.equals(flow.lastState)
//                        || RadioMetricFlowState.Approved.equals(flow.lastState)
//                        || RadioMetricFlowState.Verified.equals(flow.lastState)) {
//                        continue;
//                    }
                    if (!RadioMetricFlowState.Pending.equals(flow.lastState) && !RadioMetricFlowState.Planned.equals(flow.lastState)) {
                        continue;
                    }
                    try {
                        Db.modelMongo.delete(new ModelCommon.Settings(flow));
                        ui.addMessage("deleted " + flow.id + ":"+ flow.site.code + " from RadioMetricFlow");
                    } catch (VantarException e) {
                        ui.addErrorMessage(e);
                    }
                }
            }
        } catch (VantarException e) {
            ui.addErrorMessage(e);
        }
    }
}
