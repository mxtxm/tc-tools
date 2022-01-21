package com.tctools.business.admin.model;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.Site;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class AdminSynchRadiometric {

    public static void index(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        if (!params.contains("f")) {
            ui.beginFormPost()
                .addCheckbox("Synch all Radio Metric states", "synchallradiometric")
                .addMessage(Locale.getString(VantarKey.ADMIN_CONFIRM))
                .addEmptyLine()
                .addSubmit(Locale.getString(VantarKey.ADMIN_IMPORT))
                .finish();
            return;
        }

        boolean doState = params.isChecked("synchallradiometric");

        try {
            for (Dto site : CommonRepoMongo.getAll(new Site())) {
                synchWithSite((Site) site, doState, ui);
            }
        } catch (NoContentException | DatabaseException e) {
            ui.addErrorMessage(e);
        }

        ui.finish();
    }

    public static void synchWithSite(Site site, boolean allStates, WebUi ui) throws DatabaseException {
        String title = "(" + site.code + ") " + (site.name == null ? "???" : site.name.get(Locale.getSelectedLocale())) + "  ";

        RadioMetricFlow flow = new RadioMetricFlow();

        QueryBuilder q = new QueryBuilder(flow);
        q.condition().equal("site.code", site.code);

        if (CommonRepoMongo.exists(q)) {
            q = new QueryBuilder(flow);
            q.condition().equal("site.code", site.code);
            if (!allStates) {
                q.condition().in(
                    "lastState",
                    RadioMetricFlowState.Pending,
                    RadioMetricFlowState.Planned
                );
            }

            try {
                for (Dto dto : CommonRepoMongo.getData(q)) {
                    flow = (RadioMetricFlow) dto;

                    if (!RadioMetricFlowState.Pending.equals(flow.lastState) && !RadioMetricFlowState.Planned.equals(flow.lastState)) {
                        continue;
                    }


                    flow.site = site;

                    //if (RadioMetricFlowState.Pending.equals(flow.lastState) || RadioMetricFlowState.Planned.equals(flow.lastState)) {
                    flow.provinceId = site.provinceId;
                    flow.cityId = site.cityId;
                    flow.siteLocation = site.location;
                    flow.siteAddress = site.address;
                    //}

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

                    CommonRepoMongo.update(flow);
                    if (ui != null) {
                        ui.addMessage(Locale.getString(AppLangKey.UPDATED, flow.getClass().getSimpleName(), flow.site.code) + " " + flow.lastState).write();
                    }
                }
            } catch (NoContentException ignore) {

            }

        } else {
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

            long flowId = CommonRepoMongo.insert(flow);
            if (ui != null) {
                ui.addMessage(Locale.getString(AppLangKey.ADDED, flow.getClass().getSimpleName(), flowId + " - " + title)).write();
            }
        }
    }
}
