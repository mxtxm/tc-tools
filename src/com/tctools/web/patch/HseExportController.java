package com.tctools.web.patch;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.user.User;
import com.tctools.common.util.*;
import com.vantar.business.ModelMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryResultBase;
import com.vantar.exception.VantarException;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.file.FileUtil;
import com.vantar.util.json.Json;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@WebServlet({
    "/patch/hse/user/export",
    "/patch/hse/export",
    "/patch/hse/subcon/export",
})
public class HseExportController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(HseExportController.class);

    private int rowIndex = 0;

    public void hseSubconExport(Params params, HttpServletResponse response) throws VantarException {

        Xlsx.create(
            response,
            "radiometric-search-export.xlsx",
            "Radio metric",
            (book, sheet) -> {
                sheet.rowHeight(0, 13 * 5);
                int colIndex = 0;
                colIndex = XlsxStyled.setHeader(sheet, colIndex, "name");
                colIndex = XlsxStyled.setHeader(sheet, colIndex, "owner");
                colIndex = XlsxStyled.setHeader(sheet, colIndex, "manager");
                colIndex = XlsxStyled.setHeader(sheet, colIndex, "email");
                colIndex = XlsxStyled.setHeader(sheet, colIndex, "mobile");

                ModelMongo.forEach(new SubContractor(), new QueryResultBase.EventForeach() {
                    @Override
                    public void afterSetData(Dto dto) throws VantarException {
                        int colIndex = 0;
                        ++rowIndex;

                        SubContractor s = (SubContractor) dto;
                        colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, s.name);
                        colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, s.owner);
                        colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, s.manager);
                        colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, s.email);
                        colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, s.mobile);


                    }
                });

            }
        );

    }

    public void hseUserExport(Params params, HttpServletResponse response) throws VantarException {
        String filename = "/opt/hse-user.dump";

        WebUi ui = new WebUi(params, response);
        try {
            FileUtil.removeFile(filename);
            File file = new File(filename);
            file.createNewFile();
        } catch(Exception e) {
            ui.addErrorMessage(e).finish();
            return;
        }

        ModelMongo.forEach(new User(), dto -> {
            User user = (User) dto;
            if (user.projectTypes == null || !user.projectTypes.contains(ProjectType.HseAudit)) {
                return;
            }

            String lines =
                    Json.d.toJson(user) + "\n";
            try {
                Files.write(Paths.get(filename), lines.getBytes(), StandardOpenOption.APPEND);
                ui.addMessage(user.fullName).write();
            } catch (IOException e) {
                ui.addErrorMessage(e).write();
            }
        });


        ui.addMessage("finish!").write();
    }


    public void hseExport(Params params, HttpServletResponse response) throws VantarException {
        String filename = "/opt/hse.dump";

        WebUi ui = new WebUi(params, response);
        try {
            FileUtil.removeFile(filename);
            File file = new File(filename);
            file.createNewFile();
        } catch(Exception e) {
            ui.addErrorMessage(e).finish();
            return;
        }

        Map<String, String> activityMap = new HashMap<>();
        activityMap.put("TowerChange", "1");
        activityMap.put("NewSite", "4");
        activityMap.put("Relocation", "5");
        activityMap.put("AddL2100", "10");
        activityMap.put("AntennaChange", "2");
        activityMap.put("Modernization", "3");
        activityMap.put("Swap", "6");
        activityMap.put("PM", "7");
        activityMap.put("MakingSafe", "8");
        activityMap.put("CleanUp", "9");
        activityMap.put("QA", "11");

        Map<Long, User> users = ServiceDtoCache.asMap(User.class);
        Map<Long, SubContractor> subContractors = ServiceDtoCache.asMap(SubContractor.class);
        List<SubContractor> usedSubContractors = new ArrayList<>(2000);

        Set<String> siteCodes = new HashSet<>();

        ModelMongo.forEach(new HseAuditQuestionnaire(), dto -> {
            HseAuditQuestionnaire audit = (HseAuditQuestionnaire) dto;

            if (audit.lastState.equals(HseAuditFlowState.Pending)) {
                return;
            }

            String activity = audit.activity == null ? "" : activityMap.get(audit.activity.name());
            User assignee = audit.assigneeId == null ? null : users.get(audit.assigneeId);
            User assignor = audit.assignorId == null ? null : users.get(audit.assignorId);

            SubContractor subContractor = audit.subContractorId == null ? null : subContractors.get(audit.subContractorId);
            usedSubContractors.add(subContractor);
            boolean dup = siteCodes.contains(audit.site.code);
            siteCodes.add(audit.site.code);

            String lines =
                //toolId
                //projectId
                //quarter
                //assignmentComplete
                //siteId
                //regionId
                //provinceId
                //cityId
                //location
                "siteCode===" + audit.site.code + "" + (dup ? "DUPDUP" : "") + "\n" +
                "subContractorId===" + (subContractor == null ? "" : StringUtil.remove(Json.d.toJson(subContractor), '\n', '\r')) + "\n" +
                "assignorId===" + (audit.assignorId == null ? "" : StringUtil.remove(Json.d.toJson(assignor), '\n', '\r')) + "\n" +
                "assigneeId===" + (audit.assigneeId == null ? "" : StringUtil.remove(Json.d.toJson(assignee), '\n', '\r')) + "\n" +
                "planDate===" + audit.scheduledDateTimeFrom + "\n" +
                "planExpireDate===" + audit.scheduledDateTimeTo + "\n" +
                "lastStateDateTime===" + audit.lastStateDateTime + "\n" +
                "assignDateTime===" + audit.assignDateTime + "\n" +
                "auditDateTime===" + audit.auditDateTime + "\n" +
                "activities===" + activity + "\n" +
                "states===" + (audit.state == null ? "" : StringUtil.remove(Json.d.toJson(audit.state), '\n', '\r')) + "\n" +
                "lastState===" + (audit.lastState == null ? "" : audit.lastState.name()) + "\n" +
                "extraData.id===" + audit.id + "\n" +
                "answers===" + (audit.answers == null ? "" : StringUtil.remove(Json.d.toJson(audit.answers), '\n', '\r')) + "\n" +
                "inCompleteImages===" + (audit.inCompleteImages == null ? "" : StringUtil.remove(Json.d.toJson(audit.inCompleteImages), '\n', '\r')) + "\n" +
                "criticalNoCount===" + audit.criticalNoCount + "\n" +
                "majorNoCount===" + audit.majorNoCount + "\n" +
                "minorNoCount===" + audit.minorNoCount + "\n" +
                "criticalYesCount===" + audit.criticalYesCount + "\n" +
                "majorYesCount===" + audit.majorYesCount + "\n" +
                "minorYesCount===" + audit.minorYesCount + "\n" +
                "criticalNaCount===" + audit.criticalNaCount + "\n" +
                "majorNaCount===" + audit.majorNaCount + "\n" +
                "minorNaCount===" + audit.minorNaCount + "\n" +
                "isFailed===" + audit.isFailed + "\n" +
            "---------------------------------------\n";
            try {
                Files.write(Paths.get(filename), lines.getBytes(), StandardOpenOption.APPEND);
                ui.addMessage(audit.site.code).write();
            } catch (IOException e) {
                ui.addErrorMessage(e).write();
            }
        });


        FileUtil.write("/opt/subs.json", Json.d.toJson(usedSubContractors));


        ui.addMessage("finish!").write();
    }
}