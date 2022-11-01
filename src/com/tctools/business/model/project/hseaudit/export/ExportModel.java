package com.tctools.business.model.project.hseaudit.export;


import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.Docx;
import com.tctools.web.patch.TestController;
import com.vantar.business.*;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.bool.BoolUtil;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.number.NumberUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


public class ExportModel {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);
    private final Map<String, CellStyle> styles = new HashMap<>(10);


    public void auditData(Params params, HttpServletResponse response, HseAuditQuestionnaire.Viewable flow, String putDir)
        throws ServerException, InputException, NoContentException {

        if (flow == null) {
            flow = new HseAuditQuestionnaire.Viewable();
            flow.id = params.getLong("id");
            if (NumberUtil.isIdInvalid(flow.id)) {
                throw new InputException(VantarKey.INVALID_ID, "id");
            }
            try {
                flow = CommonRepoMongo.getById(flow, params.getLang());
            } catch (DatabaseException e) {
                log.error("!", e);
                throw new ServerException(VantarKey.FETCH_FAIL);
            }
        }

        Map<String, Object> mapping = new HashMap<>(100);
        mapping.put("date", flow.auditDateTime == null ? "" : flow.auditDateTime.formatter().getDatePersian());
        mapping.put("time", flow.auditDateTime == null ? "" : flow.auditDateTime.formatter().getTimeHm());
        mapping.put("contractor", flow.subContractor == null ? "" : (flow.subContractor.name + " - " + flow.subContractor.province.name));
        mapping.put("site.code", flow.site == null ? "" : flow.site.code);
        mapping.put("activity", flow.activity == null ? "" : flow.activity.toString());
        mapping.put("assignee", flow.assignee == null ? "" : flow.assignee.fullName);

        int naCount = 0;

        if (flow.answers != null) {
            for (HseAuditAnswer answer : flow.answers) {
                if (answer.questionId == 4) {
                    mapping.put("site.address", answer.comments);
                    continue;
                }
                if (answer.questionId == 38) {
                    mapping.put("nsuh", answer.answer.toString()); // nak supervisor use helmet
                    continue;
                }
                if (answer.questionId == 39) {
                    mapping.put("nsus", answer.answer.toString()); // nak supervisor use shoe
                    continue;
                }
                if (answer.questionId == 37) {
                    mapping.put("supervisor", answer.comments);
                    continue;
                }
                if (answer.questionId == 5) {
                    mapping.put("height-workers", answer.comments);
                    continue;
                }
                if (answer.questionId == 6) {
                    mapping.put("employees", answer.comments);
                    continue;
                }
                if (answer.question.questionType != HseAuditQuestionType.Option) {
                    continue;
                }
                if (answer.answer == HseAuditAnswerOption.NA) {
                    ++naCount;
                }

                mapping.put("yes" + answer.questionId, answer.answer == HseAuditAnswerOption.Yes ? "Yes" : "");
                mapping.put("no" + answer.questionId, answer.answer == HseAuditAnswerOption.No ? "No" : "");

                String naValue;
                if (answer.answer == HseAuditAnswerOption.NA) {
                    naValue = "N/A";
                } else if (answer.answer == HseAuditAnswerOption.Option3) {
                    try {
                        naValue = answer.question.optionLabel.get("en").get(HseAuditAnswerOption.Option3.toString());
                    } catch (Exception e) {
                        naValue = "N/A";
                    }
                } else if (answer.answer == HseAuditAnswerOption.Option4) {
                    try {
                        naValue = answer.question.optionLabel.get("en").get(HseAuditAnswerOption.Option4.toString());
                    } catch (Exception e) {
                        naValue = "N/A";
                    }
                } else {
                    naValue = "";
                }
                mapping.put("na" + answer.questionId, naValue);
                mapping.put("c" + answer.questionId, answer.comments == null ? "" : answer.comments);
            }
        }

        mapping.put("critical-count", flow.criticalNoCount == null ? "0" : Integer.toString(flow.criticalNoCount));
        mapping.put("major-count", flow.majorNoCount == null ? "0" : Integer.toString(flow.majorNoCount));
        mapping.put("minor-count", flow.minorNoCount == null ? "0" : Integer.toString(flow.minorNoCount));
        mapping.put("na-count", Integer.toString(naCount));

        String siteCode = flow.site == null ? "XXX" : flow.site.code.toLowerCase();
        String dir = Param.HSE_AUDIT_FILES + siteCode + "/";
        FileUtil.makeDirectory(dir);
        String zipTempDir = FileUtil.getTempDirectory();
        String zipFile = "hse-audit-" + flow.id + '-' + siteCode + ".zip";
        final long flowId = flow.id;

        try {
            Docx.createFromTemplate(
                Param.HSE_AUDIT_AUDIT_TEMPLATE,
                dir,
                "audit-" + flow.id + "-" + siteCode + ".docx",
                mapping
            );
            FileUtil.zip(dir, zipTempDir + zipFile, s -> s.contains("-" + flowId + "-"));

            if (putDir == null) {
                response.setContentType("application/zip");
                Response.download(response, zipTempDir + zipFile, zipFile);
            } else {
                FileUtil.move(zipTempDir + zipFile, putDir + zipFile);
            }
        } catch (VantarException e) {
            throw new ServerException(e);
        }
    }

    public void auditDataMany(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException {
        DateTime dateMin = params.getDateTime("dateMin");
        DateTime dateMax = params.getDateTime("dateMax");
        String states = params.getString("states", "Approved,PreApproved");
        CommonModel.validateRequired("dateMin", dateMin, "dateMax", dateMax);

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire.Viewable());
        q.condition().between("lastStateDateTime", dateMin, dateMax);
        q.condition().inString("lastState", StringUtil.splitToList(states, ','));

        String dt = new DateTime().formatter().getDateTimeAsFilename();
        String dir = Param.TEMP_DIR + dt + "/";
        FileUtil.makeDirectory(dir);

        List<Dto> data = CommonModelMongo.getData(q);
        for (Dto dto : data) {
            try {
                auditData(
                    params,
                    response,
                    (HseAuditQuestionnaire.Viewable) dto,
                    dir
                );

                Response.writeString(response, ((HseAuditQuestionnaire.Viewable) dto).site.code + "<br>");
                try {
                    response.flushBuffer();
                } catch (IOException ignore) {

                }

            } catch (ServerException | InputException | NoContentException e) {
                log.error(" ! {}", dto.getId(), e);
            }
        }

        String zipFile = "audit-" + dt + ".zip";
        String zipTempDir = Param.TEMP_DIR;
        FileUtil.zip(dir, zipTempDir + zipFile);

        if (BoolUtil.isTrue(params.getBoolean("nodownload"))) {
            Response.writeString(response, dir + " : " + zipTempDir + zipFile);
        } else {
            response.setContentType("application/zip");
            Response.download(response, zipTempDir + zipFile, zipFile);
        }
    }

    public void dailyReport(Params params, HttpServletResponse response) throws ServerException {

        // <cell, questionId>
        Map<Integer, Long> t = new HashMap<>();
        t.put(9, 40L); // supervisor
        t.put(10, 5L); // Tower climbers
        t.put(11, 6L); // Other employees

        t.put(47, 37L); // nak supervisor
        t.put(48, 38L); // nak supervisor helmet
        t.put(49, 39L); // nak supervisor shoe

        t.put(17, 7L); // q 7 > > >
        t.put(18, 8L);
        t.put(19, 9L);
        t.put(20, 10L);
        t.put(21, 11L);
        t.put(22, 12L);
        t.put(23, 13L);
        t.put(24, 14L);
        t.put(25, 15L);
        t.put(26, 16L);
        t.put(27, 17L);
        t.put(28, 18L);
        t.put(29, 19L);
        t.put(30, 20L);
        t.put(31, 21L);
        t.put(32, 22L);
        t.put(33, 23L);
        t.put(34, 24L);
        t.put(35, 25L);
        t.put(36, 26L);
        t.put(37, 27L);
        t.put(38, 28L);
        t.put(39, 29L);
        t.put(40, 30L);
        t.put(41, 31L);
        t.put(42, 32L);
        t.put(43, 33L);
        t.put(44, 34L);
        t.put(45, 35L);
        t.put(46, 36L); // < < < q36

        try (
            FileInputStream inputStream = new FileInputStream(new File(Param.HSE_AUDIT_DAILY_TEMPLATE));
            Workbook wb = WorkbookFactory.create(inputStream)
            ) {

            Sheet sheet = wb.getSheetAt(0);

            QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire(), new HseAuditQuestionnaire.Viewable());
            q.condition().in("lastState", HseAuditFlowState.Approved, HseAuditFlowState.PreApproved);
            List<Dto> items;
            try {
                items = CommonRepoMongo.getData(q, params.getLang());
            } catch (DatabaseException e) {
                log.error("!", e);
                throw new ServerException(VantarKey.FETCH_FAIL);
            } catch (NoContentException e) {
                throw new ServerException(VantarKey.FETCH_FAIL);
            }

            int i = 1;
            for (Dto dto : items) {
                HseAuditQuestionnaire.Viewable flow = (HseAuditQuestionnaire.Viewable) dto;

                if (flow.answers == null || flow.answers.isEmpty()) {
                    log.error("!!! no answers {} > {}", flow.id, flow.site.code);
                    continue;
                }

                Row row = sheet.createRow(i + 1);

                Cell cell = row.createCell(0); // A
                cell.setCellStyle(getCellStyleIndex(wb));
                cell.setCellValue(i);

                setData(wb, row, 1, flow.auditDateTime == null ? "" : flow.auditDateTime.formatter().getDatePersian()); // B
                if (flow.site != null) {
                    setData(wb, row, 2, flow.site.province == null ? "" : flow.site.province.name); // C
                    setData(wb, row, 3, flow.site.city == null ? "" : flow.site.city.name); // D
                    setData(wb, row, 4, flow.site.code); // E
                }
                setData(wb, row, 5, flow.subContractor == null ? "" : flow.subContractor.name); // F

                setData(wb, row, 6, flow.assignee == null ? "" : flow.assignee.fullName); // G
                setData(wb, row, 7, flow.activity == null ? "" : flow.activity.toString()); // H
                setData(wb, row, 8, ""); // I project
                setData(wb, row, 12, flow.activity == null ? "" : flow.activity.toString()); // M action
                setData(wb, row, 13, ""); // N description

                // O(14) P(15) Q(16) > FAILED NUMBERS
                int criticalNo = 0;
                int majorNo = 0;
                int minorNo = 0;

                // R:AX > QUESTIONS
                Map<Long, HseAuditAnswer> answers = new HashMap<>(flow.answers.size());
                for (HseAuditAnswer answer : flow.answers) {
                    answers.put(answer.questionId, answer);
                }

                for (Map.Entry<Integer, Long> entry : t.entrySet()) {
                    Integer cellNo = entry.getKey();
                    Long questionId = entry.getValue();
                    HseAuditAnswer answer = answers.get(questionId);

                    if (answer == null) {
                        continue;
                    }

                    Cell c = row.createCell(cellNo);

                    String v;
                    if (answer.question.questionType == HseAuditQuestionType.TextOnly) {
                        v = answer.comments;
                    } else if (answer.question.questionType == HseAuditQuestionType.Option) {

                        if (answer.question.significance == HseAuditQuestionSignificance.Critical) {
                            c.setCellStyle(getCellStyleCritical(wb));
                            if (answer.answer == HseAuditAnswerOption.No) {
                                ++criticalNo;
                            }
                        } else if (answer.question.significance == HseAuditQuestionSignificance.Major) {
                            c.setCellStyle(getCellStyleMajor(wb));
                            if (answer.answer == HseAuditAnswerOption.No) {
                                ++majorNo;
                            }
                        } else if (answer.question.significance == HseAuditQuestionSignificance.Minor) {
                            c.setCellStyle(getCellStyleMinor(wb));
                            if (answer.answer == HseAuditAnswerOption.No) {
                                ++minorNo;
                            }
                        }

                        if (answer.answer == HseAuditAnswerOption.Yes) {
                            v = "1";
                        } else if (answer.answer == HseAuditAnswerOption.No) {
                            v = "0";
                        } else {
                            v = "NA";
                        }
                    } else {
                        v = "";
                    }
                    c.setCellValue(v);
                }

                cell = row.createCell(14);
                if (criticalNo >= Param.HSE_CRITICAL_FAIL_THRESHOLD) {
                    cell.setCellStyle(getCellStyleFailed(wb));
                }
                cell.setCellValue(criticalNo);

                cell = row.createCell(15);
                if (majorNo >= Param.HSE_MAJOR_FAIL_THRESHOLD) {
                    cell.setCellStyle(getCellStyleFailed(wb));
                }
                cell.setCellValue(majorNo);

                cell = row.createCell(16);
                cell.setCellValue(minorNo);

                ++i;
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=daily-report-"
                + (new DateTime().formatter().getDateTimeSimple()) + ".xlsx");

            wb.write(response.getOutputStream());

        } catch (IOException e) {
            log.error("", e);
            throw new ServerException(AppLangKey.EXPORT_FAIL);
        } finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException ignore) {

            }
        }
    }

    private void setData(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleNormal(wb));
        cell.setCellValue(value);
    }

    private CellStyle getCellStyleNormal(Workbook wb) {
        CellStyle style = styles.get("getCellStyleNormal");
        if (style == null) {
            style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            styles.put("getCellStyleNormal", style);
        }
        return style;
    }

    private CellStyle getCellStyleIndex(Workbook workbook) {
        CellStyle style = styles.get("getCellStyleIndex");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(248, 203, 173), new DefaultIndexedColorMap()));
            styles.put("getCellStyleIndex", style);
        }
        return style;
    }

    private CellStyle getCellStyleCritical(Workbook workbook) {
        CellStyle style = styles.get("getCellStyleCritical");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(217, 217, 217), new DefaultIndexedColorMap()));
            styles.put("getCellStyleCritical", style);
        }
        return style;
    }

    private CellStyle getCellStyleMajor(Workbook workbook) {
        CellStyle style = styles.get("getCellStyleMajor");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(252, 228, 214), new DefaultIndexedColorMap()));
            styles.put("getCellStyleMajor", style);
        }
        return style;
    }

    private CellStyle getCellStyleMinor(Workbook workbook) {
        CellStyle style = styles.get("getCellStyleMinor");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(231, 230, 230), new DefaultIndexedColorMap()));
            styles.put("getCellStyleMinor", style);
        }
        return style;
    }

    private CellStyle getCellStyleFailed(Workbook workbook) {
        CellStyle style = styles.get("getCellStyleFailed");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(239, 23, 57), new DefaultIndexedColorMap()));
            styles.put("getCellStyleFailed", style);
        }
        return style;
    }
}