package com.tctools.business.admin.model;

import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.site.Site;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.datetime.DateTime;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class AdminSynchHseAudit {

    public static void index(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }

        if (!params.contains("f")) {
            ui.beginFormPost()
                .addCheckbox("Synch all HSE Audit states", "synchallhse")
                .addMessage(Locale.getString(VantarKey.ADMIN_CONFIRM))
                .addEmptyLine()
                .addSubmit(Locale.getString(VantarKey.ADMIN_IMPORT))
                .finish();
            return;
        }

        boolean doState = params.isChecked("synchallhse");

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

        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();

        QueryBuilder q = new QueryBuilder(flow);
        q.condition().equal("site.code", site.code);

        if (CommonRepoMongo.exists(q)) {
            q = new QueryBuilder(flow);
            q.condition().equal("site.code", site.code);
            if (allStates) {
                q.condition().in(
                    "lastState",
                    HseAuditFlowState.Pending,
                    HseAuditFlowState.Planned,
                    HseAuditFlowState.Expired,
                    HseAuditFlowState.Restricted
                );
            }

            try {
                for (Dto dto : CommonRepoMongo.getData(q)) {
                    flow = (HseAuditQuestionnaire) dto;
                    flow.site = site;

                    CommonRepoMongo.update(flow);
                    if (ui != null) {
                        ui.addMessage(Locale.getString(AppLangKey.UPDATED, flow.getClass().getSimpleName(), flow.site.code)).write();
                    }
                }
            } catch (NoContentException ignore) {

            }

        } else {
            flow.site = site;
            flow.lastState = HseAuditFlowState.Pending;
            flow.lastStateDateTime = new DateTime();
            flow.state = new ArrayList<>();
            flow.state.add(new State(flow.lastState, flow.lastStateDateTime));

            if (site.provinceId != null) {
                List<SubContractor> contractors;
                try {
                    contractors = Services.get(ServiceDtoCache.class).getList(SubContractor.class);
                } catch (ServiceException e) {
                    ui.addErrorMessage(e);
                    return;
                }
                for (SubContractor contractor : contractors) {
                    if (site.provinceId.equals(contractor.provinceId)) {
                        flow.subContractorId = contractor.id;
                        break;
                    }
                }
            }

            long flowId = CommonRepoMongo.insert(flow);
            if (ui != null) {
                ui.addMessage(Locale.getString(AppLangKey.ADDED, flow.getClass().getSimpleName(), flowId + " - " + title)).write();
            }
        }
    }
}
