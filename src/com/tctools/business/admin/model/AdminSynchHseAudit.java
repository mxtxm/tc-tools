package com.tctools.business.admin.model;

import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.site.Site;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.Admin;
import com.vantar.business.*;
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

    public static void index(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

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
            for (Dto site : CommonModelMongo.getAll(new Site())) {
                synchWithSite((Site) site, doState, ui);
            }
        } catch (VantarException e) {
            ui.addErrorMessage(e);
        }

        ui.finish();
    }

    public static void synchWithSite(Site site, boolean allStates, WebUi ui) throws VantarException {
        String title = "(" + site.code + ") " + (site.name == null ? "???" : site.name.get(Locale.getSelectedLocale())) + "  ";

        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();

        QueryBuilder q = new QueryBuilder(flow);
        q.condition().equal("site.code", site.code);

        if (CommonModelMongo.exists(q)) {
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
                for (Dto dto : CommonModelMongo.getData(q)) {
                    flow = (HseAuditQuestionnaire) dto;
                    flow.site = site;

                    CommonModelMongo.update(flow);
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

            ResponseMessage res = CommonModelMongo.insert(flow);
            if (ui != null) {
                ui.addMessage(Locale.getString(AppLangKey.ADDED, flow.getClass().getSimpleName(), res.value + " - " + title)).write();
            }
        }
    }

    protected static void removeRemovedSited(WebUi ui) {
        try {
            for (Dto dto : CommonModelMongo.getAll(new HseAuditQuestionnaire())) {
                HseAuditQuestionnaire flow = (HseAuditQuestionnaire) dto;
                if (!AdminSiteImport.siteCodes.contains(flow.site.code)) {

//                    if(HseAuditFlowState.Completed.equals(flow.lastState)
//                    || HseAuditFlowState.Approved.equals(flow.lastState)
//                    || HseAuditFlowState.PreApproved.equals(flow.lastState)
//                    || HseAuditFlowState.MCI_Approve.equals(flow.lastState)
//                    || HseAuditFlowState.MCI_Reject.equals(flow.lastState)) {
//                        continue;
//                    }
                    if(!HseAuditFlowState.Pending.equals(flow.lastState) && !HseAuditFlowState.Planned.equals(flow.lastState)) {
                        continue;
                    }


                    try {
                        CommonModelMongo.deleteById(flow);
                        ui.addMessage("deleted " + flow.id + ":"+ flow.site.code + " from HseAuditQuestionnaire");
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
