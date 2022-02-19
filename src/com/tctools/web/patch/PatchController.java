package com.tctools.web.patch;

import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.model.project.radiometric.workflow.WorkFlowModel;
import com.tctools.common.Param;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.file.DirUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@WebServlet({
    "/patch/fix/omni",
    "/patch/missing/radiometric",
    "/patch/invalid/selected/sector",
    "/patch/invalid/selected/sector/fix",
    "/patch/invalid/questionnaire/no/subcontractor",
    "/patch/invalid/questionnaire/missing/subcontractor",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class PatchController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(PatchController.class);


    /**
     * 20 Jan 2022
     * update radiometric sectors omni and directional with site data
     */
    public void fixOmni(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);
        ui.addMessage("start...").write();

        List<Site> sites;
        List<RadioMetricFlow> flows;
        try {
            sites = CommonRepoMongo.getAll(new Site());
            flows = CommonRepoMongo.getAll(new RadioMetricFlow());
        } catch (NoContentException | DatabaseException e) {
            ui.addErrorMessage(e).finish();
            return;
        }

        for (RadioMetricFlow flow : flows) {
            for (Site site : sites) {
                if (flow.site.code.equals(site.code)) {
                    if (flow.site.sectors == null || site.sectors == null) {
                        break;
                    }

                    for (Sector flowSector : flow.site.sectors) {
                        for (Sector sector : site.sectors) {
                            if (sector.title.equals(flowSector.title)) {
                                flowSector.isOmni = sector.isOmni;
                                flowSector.isDirectional = sector.isDirectional;
                                if (flowSector.antennaCount == null) {
                                    flowSector.antennaCount = sector.antennaCount;
                                }
                                if (flowSector.locationTypeId == null) {
                                    flowSector.locationTypeId = sector.locationTypeId;
                                }
                                if (flowSector.height == null) {
                                    flowSector.height = sector.height;
                                }
                                if (flowSector.onSiteHeight == null) {
                                    flowSector.onSiteHeight = sector.onSiteHeight;
                                }
                                if (flowSector.electricalTilt == null) {
                                    flowSector.electricalTilt = sector.electricalTilt;
                                }
                                break;
                            }
                        }
                    }
                    if (flow.sectors != null) {
                        for (Sector flowSector : flow.sectors) {
                            for (Sector sector : site.sectors) {
                                if (sector.title.equals(flowSector.title)) {
                                    flowSector.isOmni = sector.isOmni;
                                    flowSector.isDirectional = sector.isDirectional;
                                    if (flowSector.antennaCount == null) {
                                        flowSector.antennaCount = sector.antennaCount;
                                    }
                                    if (flowSector.locationTypeId == null) {
                                        flowSector.locationTypeId = sector.locationTypeId;
                                    }
                                    if (flowSector.height == null) {
                                        flowSector.height = sector.height;
                                    }
                                    if (flowSector.onSiteHeight == null) {
                                        flowSector.onSiteHeight = sector.onSiteHeight;
                                    }
                                    if (flowSector.electricalTilt == null) {
                                        flowSector.electricalTilt = sector.electricalTilt;
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    break;
                }
            }

            try {
                CommonRepoMongo.update(flow);
                ui.addMessage(flow.site.code).write();
            } catch (DatabaseException e) {
                ui.addErrorMessage(e).finish();
            }
        }

        ui.addMessage("finished!").finish();
    }

    /**
     * 22 aug 2021
     * find missing radiometric records with existing folder
     */
    public void missingRadiometric(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        DirUtil.browseDir(Param.RADIO_METRIC_FILES, rDir -> {
            DirUtil.browseDir(rDir + "/measurement/", mDir -> {
                String siteCode = rDir.getName();
                Long id = StringUtil.toLong(mDir.getName());

                try {
                    RadioMetricFlow flow = new RadioMetricFlow();
                    flow.id = id;
                    CommonRepoMongo.getById(flow);
                } catch (DatabaseException e) {
                    ui.addErrorMessage(e).write();
                } catch (NoContentException e) {
                    ui.addMessage("dir exists, flow not exists code=" + siteCode + " id=" + id).write();
                }
            });
        });

        ui.addMessage("finished!").write();
    }

    /**
     * 24 aug 2021
     * find radiometric records that stored selected sector not matches calculated selected sector
     */
    public void invalidSelectedSector(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().in(
            "lastState",
            RadioMetricFlowState.Completed,
            RadioMetricFlowState.Verified,
            RadioMetricFlowState.Returned,
            RadioMetricFlowState.Revise,
            RadioMetricFlowState.Approved
        );
        try {
            List<RadioMetricFlow> flows = CommonRepoMongo.getData(q);
            for (RadioMetricFlow f : flows) {
                if (f.sectors == null || f.sectors.isEmpty()) {
                    continue;
                }
                String selected = null;
                for (Sector s : f.sectors) {
                    if (s.selected) {
                        selected = s.title;
                    }
                }

                String calculatedSelected = null;
                String calculatedSelectedToShow = null;
                WorkFlowModel.setNearestSector(f);
                for (Sector s : f.sectors) {
                    if (s.selected) {
                        calculatedSelected = StringUtil.remove(s.title, '1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
                        calculatedSelectedToShow = calculatedSelected + " (" + s.title + ")";
                    }
                }

                if (selected == null && calculatedSelected == null) {
                    continue;
                }

                if (selected == null) {
                    ui.addErrorMessage(f.site.code + " (" + f.id + ") NO SELECTED SECTOR " + " calculated=" + calculatedSelectedToShow);
                    continue;
                }

                if (!selected.equals(calculatedSelected)) {
                    ui.addErrorMessage(f.site.code + " (" + f.id + ") MISS-MATCH stored=" + selected + " calculated=" + calculatedSelectedToShow);
                }
            }
        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e).write();
        }

        ui.addMessage("finished!").write();
    }

    /**
     * 24 aug 2021
     * find radiometric records that stored selected sector not matches calculated selected sector then fix to calculated
     */
    public void invalidSelectedSectorFix(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().in(
            "lastState",
            RadioMetricFlowState.Completed,
            RadioMetricFlowState.Verified,
            RadioMetricFlowState.Returned,
            RadioMetricFlowState.Revise,
            RadioMetricFlowState.Approved
        );
        try {
            List<RadioMetricFlow> flows = CommonRepoMongo.getData(q);
            for (RadioMetricFlow f : flows) {
                if (f.sectors == null || f.sectors.isEmpty()) {
                    continue;
                }
                String selected = null;
                for (Sector s : f.sectors) {
                    if (s.selected) {
                        selected = s.title;
                    }
                }

                String calculatedSelected = null;
                WorkFlowModel.setNearestSector(f);
                for (Sector s : f.sectors) {
                    if (s.selected) {
                        calculatedSelected = s.title;
                    }
                }

                if (calculatedSelected == null) {
                    continue;
                }

                calculatedSelected = StringUtil.remove(calculatedSelected, '1', '2', '3', '4', '0');

                if (selected == null) {
                    ui.addErrorMessage(f.site.code + " (" + f.id + ") NO SELECTED SECTOR " + " > " + calculatedSelected);

                    for (Sector s : f.sectors) {
                        s.selected = s.title.equals(calculatedSelected);
                    }
                    CommonRepoMongo.update(f);

                    continue;
                }

                if (!selected.equals(calculatedSelected)) {
                    ui.addErrorMessage(f.site.code + " (" + f.id + ") " + selected + " > " + calculatedSelected);

                    for (Sector s : f.sectors) {
                        s.selected = s.title.equals(calculatedSelected);
                    }
                    CommonRepoMongo.update(f);
                }
            }
        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e).write();
        }

        ui.addMessage("finished!").write();
    }

    /**
     * 2 sep 2021
     * find hse records that dont have sub contractor
     */
    public void invalidQuestionnaireNoSubcontractor(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire(), new HseAuditQuestionnaire.Viewable());
        q.condition().in("lastState", HseAuditFlowState.PreApproved, HseAuditFlowState.Approved);
        try {
            List<HseAuditQuestionnaire.Viewable> flows = CommonRepoMongo.getData(q, "fa");
            for (HseAuditQuestionnaire.Viewable f : flows) {
                if (f.subContractor == null) {
                    ui.addErrorMessage(f.site.code + " (" + f.id + ")").write();
                }
            }
        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e).write();
        }

        ui.addMessage("finished!").write();
    }

    /**
     * 7 jan 2022
     * find hse records that dont have sub contractor
     */
    public void invalidQuestionnaireMissingSubcontractor(Params params, HttpServletResponse response) throws FinishException, ServiceException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        Map<Long, Dto> subContractor = Services.get(ServiceDtoCache.class).getMap(SubContractor.class);

        Set<Long> subIds = new HashSet<>();

        try {
            List<HseAuditQuestionnaire> flows = CommonRepoMongo.getAll(new HseAuditQuestionnaire(), "fa");
            for (HseAuditQuestionnaire f : flows) {
                if (f.subContractorId != null && !subContractor.containsKey(f.subContractorId)) {
                    ui.addErrorMessage(f.site.code + " (" + f.id + ") > " + f.subContractorId).write();
                    subIds.add(f.subContractorId);
                }
            }
        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e).write();
        }

        log.error(">>>>>>>>{}", subIds);
        ui.addMessage("finished!").write();
    }

}