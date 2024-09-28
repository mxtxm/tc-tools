package com.tctools.web.patch;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlowState;
import com.tctools.business.dto.project.radiometric.workflow.State;
import com.tctools.business.dto.user.User;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.database.query.QueryCondition;
import com.vantar.database.query.QueryOperator;
import com.vantar.exception.DateTimeException;
import com.vantar.exception.VantarException;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.json.Json;
import com.vantar.util.xlsx.Xlsx;
import com.vantar.web.Params;
import com.vantar.web.RouteToMethod;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

// KH1121 -> No content.
@WebServlet({
    "/patch/complete/data",
})
public class PatchController extends RouteToMethod {

    private int ok;
    private int bad;

    public void completeData(Params params, HttpServletResponse response) throws VantarException {
        Set<String> assigneeNames = new HashSet<>(100);
        Set<String> assignorNames = new HashSet<>(100);

        List<Result> results = new ArrayList<>(10000);

        Map<String, Long> assigneeMapId = new HashMap<>(10);
        assigneeMapId.put("اشکان حسنیان", 3561L);
        assigneeMapId.put("ایمان عبدی", 76L);
        assigneeMapId.put("رضا عباسی", 87L);
        //assigneeMapId.put("محمد رضا شاه صنم", 19L);
        assigneeMapId.put("علیرضا فتوحی", 77L);
        assigneeMapId.put("حجت رکنی شیرازی", 5L);
        assigneeMapId.put("احمدرضا خلج", 74L);
        assigneeMapId.put("حامد خان سفید", 3598L);
        assigneeMapId.put("محمدرضا کمالی", 3567L);
        assigneeMapId.put("مهدی فیروزی", 78L);
        assigneeMapId.put("افشین الهی", 9L);
        assigneeMapId.put("حجت شیرازی", 75L);
        assigneeMapId.put("رضا گودرزی", 72L);
        assigneeMapId.put("سهیل شهسواری", 97L);
        assigneeMapId.put("عبدالستار جمشیدزهی", 7L);
        assigneeMapId.put("مجید کمپانی", 73L);
        assigneeMapId.put("محسن حدادی", 12L);
        assigneeMapId.put("محمد دادبخش", 41L);

        Map<String, Long> assignorMapId = new HashMap<>(10);
        Map<String, String> assignorMapName = new HashMap<>(10);
        assignorMapId.put("Saba Lotfi", 3698L);
        assignorMapId.put("Maryam Baratpour", 3700L);
        assignorMapId.put("Kosar Servati", 3701L);
        assignorMapName.put("Saba Lotfi", "صبا لطفی");
        assignorMapName.put("Maryam Baratpour", "م برات پور");
        assignorMapName.put("Kosar Servati", "ک ثروتی");

        Xlsx.Config config = new Xlsx.Config();
        config.filename = "/opt/tc-tools/patch/981.xlsx";
        config.headerRowcount = 2;
        config.addReadEvent(0, new Xlsx.ReadEvents() {
            @Override
            public boolean onReadHeader(ReadableWorkbook workbook, Row row) {
                return true;
            }

            @Override
            public boolean onReadRow(ReadableWorkbook workbook, Row row) {
                boolean isOk = true;
                // site code
                Cell siteCodeCell = row.getCell(1);
                if (siteCodeCell == null) {
                    return true;
                }
                String siteCode =  Xlsx.getString(siteCodeCell);
                if (siteCode == null) {
                    return true;
                }

                Result result = new Result();
                result.message = new ArrayList<>(5);
                result.siteCode = siteCode;
                results.add(result);

                result.state = Xlsx.getString(row.getCell(2));
                result.date = Xlsx.getString(row.getCell(3));
                result.assignee = Xlsx.getString(row.getCell(4));
                result.assignor = Xlsx.getString(row.getCell(6));
                result.description = Xlsx.getString(row.getCell(5));

                QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
                q.condition()
                    .equal("site.code", siteCode)
                    .addCondition(new QueryCondition(QueryOperator.OR)
                        .equal("comments", "OLD WORKFLOW")
                        .in("patch", "FIX OLD WORKFLOW")
                    );

                try {
                    List<Dto> data = Db.modelMongo.getData(q);
                    if (data.size() > 1) {
                        result.message.add("more than one records found");
                        return true;
                    }

                    // assign date
                    DateTime assignDateTime = null;
                    RadioMetricFlow flow = (RadioMetricFlow) data.get(0);
                    if (result.date != null) {
                        assignDateTime = result.getDate();
                    }
                    if (assignDateTime != null) {
                        flow.assignDateTime = assignDateTime;
                    }
                    assignDateTime = flow.assignDateTime;

                    // assignee
                    Long assigneeId = null;
                    String assigneeName = result.assignee;
                    if (result.assignee != null) {
                        assigneeId = assigneeMapId.get(result.assignee);
                    }
                    if (assigneeId != null) {
                        flow.assigneeId = assigneeId;
                    }
                    assigneeId = flow.assigneeId;
                    if (flow.assigneeId != null && assigneeName == null) {
                        assigneeName = ServiceDtoCache.asDto(User.class, flow.assigneeId).fullName;
                    }

                    // assignor
                    Long assignorId = null;
                    String assignorName = result.assignor;
                    if (result.assignor != null) {
                        assignorId = assignorMapId.get(result.assignor);
                    }
                    if (assignorId != null) {
                        flow.assignorId = assignorId;
                    }
                    assignorId = flow.assignorId;
                    if (flow.assignorId != null && assignorName == null) {
                        assignorName = ServiceDtoCache.asDto(User.class, flow.assignorId).fullName;
                    }

                    assigneeNames.add(assigneeName);
                    assignorNames.add(assignorName);

                    // check
                    if (flow.assignDateTime == null) {
                        result.message.add("date");
                    }
                    if (flow.assigneeId == null) {
                        result.message.add("assigneeId");
                    }
                    if (flow.assignorId == null) {
                        result.message.add("assignorId");
                    }
                    if (assigneeName == null) {
                        result.message.add("assigneeName");
                    }
                    if (assignorName == null) {
                        result.message.add("assignorName");
                    }
                    if (!result.message.isEmpty()) {
                        ++bad;
                        return true;
                    }

                    // set
                    flow.assignable = false;
                    flow.lastState = RadioMetricFlowState.Verified;
                    flow.state = new ArrayList<>(10);
                    State pending = new State();
                    pending.state = RadioMetricFlowState.Pending;
                    pending.dateTime = new DateTime(assignDateTime).decreaseDays(2);

                    State planned = new State();
                    planned.state = RadioMetricFlowState.Planned;
                    planned.dateTime = new DateTime(assignDateTime).decreaseDays(1);
                    planned.assigneeId = assigneeId;
                    planned.assigneeName = assigneeName;
                    planned.assignorId = assignorId;
                    planned.assignorName = assignorName;

                    State completed = new State();
                    completed.state = RadioMetricFlowState.Completed;
                    completed.dateTime = assignDateTime;
                    completed.assignorId = planned.assigneeId;
                    completed.assignorName = planned.assigneeName;

                    State verified = new State();
                    verified.state = RadioMetricFlowState.Verified;
                    verified.dateTime = new DateTime(assignDateTime).addDays(1);
                    verified.assignorId = assignorId;
                    verified.assignorName = assignorName;

                    flow.state.add(pending);
                    flow.state.add(planned);
                    flow.state.add(completed);
                    flow.state.add(verified);

                    flow.assignDateTime = assignDateTime;
                    flow.assignorId = assignorId;
                    flow.assigneeId = assigneeId;
                    flow.lastStateDateTime = verified.dateTime;
                    flow.measurementDateTime = completed.dateTime;
                    flow.comments = null;
                    flow.addNullProperties("comments");
                    flow.patch = new ArrayList<>(1);
                    flow.patch.add("FIX OLD WORKFLOW");

                    Db.modelMongo.update(new ModelCommon.Settings(flow).dtoHasFullData(true).mutex(false).logEvent(false));
                    ++ok;

                } catch (VantarException e) {
                    result.message.add(e.getMessage());
                    ++bad;
                }

                return true;
            }

            @Override
            public void onError(ReadableWorkbook workbook, Exception e, boolean rowError, int sheetIndex) {
                log.error(">>>", e);
            }
        });
        Xlsx.read(config);

//        Response.writeString(response, "ok:  " + ok + "<br>");
//        Response.writeString(response, "bad: " + bad + "<br>");
        log.error(Json.d.toJsonPretty(assigneeNames));
        log.error(Json.d.toJsonPretty(assignorNames));


        Xlsx.Config xlxs = new Xlsx.Config(response, "result.xlsx", "result");
        xlxs.addWriteEvent("RESULT", (context) -> {
            context.sheet.fitToWidth((short) 1);
            context.sheet.rowHeight(0, 13 * 3);
            XlsxStyleX.setHeader(context);
            Xlsx.setCell(context, "Code");
            Xlsx.setCell(context, "Assign date");
            Xlsx.setCell(context, "Assignee");
            Xlsx.setCell(context, "Assignor");
            Xlsx.setCell(context, "State");
            Xlsx.setCell(context, "Description");
            Xlsx.setCell(context, "Result");

            XlsxStyleX.setDefault(context);
            for (Result r : results) {
                context.nextRow();
                String color = r.message.isEmpty() ? XlsxStyleX.WHITE : XlsxStyleX.RED;
                Xlsx.setCellColorWidth(context, color, 15, r.siteCode);
                Xlsx.setCellColorWidth(context, color, 15, r.date);
                Xlsx.setCellColorWidth(context, color, 20, r.assignee);
                Xlsx.setCellColorWidth(context, color, 20, r.assignor);
                Xlsx.setCellColorWidth(context, color, 10, r.state);
                Xlsx.setCellColorWidth(context, color, 20, r.description);
                Xlsx.setCellColorWidth(context, color, 30, CollectionUtil.join(r.message, ", "));
            }
        });
        Xlsx.create(xlxs);
    }


    private static class Result {
        public String siteCode;
        public String state;
        public String date;
        public String assignee;
        public String assignor;
        public String description;
        public List<String> message;

        public DateTime getDate() {
            try {
                return new DateTime(date);
            } catch (DateTimeException e) {
                return null;
            }
        }
    }

}