package com.tctools.business.model.project.hseaudit.export;

import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.util.ExportCommon;
import com.vantar.business.CommonModelMongo;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.datetime.*;
import com.vantar.util.object.ObjectUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class QuestionSubcontractor extends ExportCommon {

    private static final String LANG = "fa";
    private final Map<String, CellStyle> styles = new HashMap<>(10);


    private void toExcel(
        HttpServletResponse response,
        DateTimeRange dateTimeRange,
        Result result
        ) throws ServerException {

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Observation per Sub contractor");
            sheet.setRightToLeft(true);

            //sheet.setColumnWidth(0, 100 * 256);

            Row row1 = sheet.createRow(0);
            setDateHeader(
                wb, row1, 0,
                "از تاریخ " + dateTimeRange.dateMin.formatter().getDatePersian() + "\n"
                + "تا تاریخ " + dateTimeRange.dateMax.formatter().getDatePersian()
            );
            Row row2 = sheet.createRow(1);

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

            int firstRow = 0;
            int lastRow = 0;
            int firstCol = 1;
            int lastCol = 0;

            for (SubContractorOrder subContractorOrder : result.subContractorOrdered) {
                setSubContractorHeader(wb, row1, firstCol, subContractorOrder.name);

                for (String title : result.statisticTitles) {
                    setStatisticsHeader(wb, row2, ++lastCol, title);
                }

                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
                firstCol += result.statisticTitles.size();
            }

            int r = 1;
            for (Map.Entry<String, QuestionStatistics> entry : result.questionStatistics.entrySet()) {
                int c = 0;
                Row row = sheet.createRow(++r);
                setQuestion(wb, row, c, entry.getKey());

                for (SubContractorOrder provinceOrder : result.subContractorOrdered) {
                    int t = result.subContractorAuditCount.get(provinceOrder.name);

                    setTotalAudit(wb, row, ++c, Integer.toString(t));

                    QuestionStatistics.Statistics s = entry.getValue().statistics.get(provinceOrder.name);

                    setDataCell(wb, row, ++c, Integer.toString(s.no));
                    setDataCell(wb, row, ++c, Double.toString(
                        Math.round((s.no * 100.0 / t) * 100.0) / 100.0
                    ));

                    setDataCell(wb, row, ++c, Integer.toString(s.yes));
                    setDataCell(wb, row, ++c, Double.toString(
                        Math.round((s.yes * 100.0 / t) * 100.0) / 100.0
                    ));

                    setDataCell(wb, row, ++c, Integer.toString(s.na));

                    for (String st : result.statisticTitlesOthers) {
                        String value;
                        if (st.equals(s.option3Label)) {
                            value = Integer.toString(s.option3);
                        } else if (st.equals(s.option4Label)) {
                            value = Integer.toString(s.option4);
                        } else {
                            value = "";
                        }
                        setDataCell(wb, row, ++c, value);
                    }
                }
            }

            for (int i = 0 ; i < firstCol ; ++i) {
                sheet.autoSizeColumn(i);
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=assigned-"
                + ("observation-province-" + new DateTime().formatter().getDateTimeSimple()) + ".xlsx");

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

    public void outputAggregate(Params params, HttpServletResponse response)
        throws VantarException {

        DateTimeRange dateTimeRange = params.getDateTimeRange("from", "to");
        if (dateTimeRange == null || !dateTimeRange.isValid()) {
            throw new InputException(VantarKey.INVALID_DATE);
        }
        List<Long> provinceIds = params.getLongList("provinceids");
        List<Long> subContractorIds = params.getLongList("subcontractorids");
        List<Long> questionIds = params.getLongList("questionids");

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire.Viewable());
        q.condition().in("lastState", HseAuditFlowState.PreApproved, HseAuditFlowState.Approved);
        q.condition().between("auditDateTime", dateTimeRange);
        if (provinceIds != null && !provinceIds.isEmpty()) {
            q.condition().inNumber("site.provinceId", provinceIds);
        }
        if (subContractorIds != null && !subContractorIds.isEmpty()) {
            q.condition().inNumber("subContractorId", subContractorIds);
        }

        List<HseAuditQuestionnaire.Viewable> questionnaires;
        questionnaires = CommonModelMongo.getData(q, LANG);

        Result result = new Result();
        // for all option type questions
        for (HseAuditQuestion question : Services.get(ServiceDtoCache.class).getList(HseAuditQuestion.class)) {
            if (questionIds != null && !questionIds.contains(question.id)) {
                continue;
            }
            if (question.questionType.equals(HseAuditQuestionType.Option)) {
                result.questionStatistics.put(question.title.get(LANG), new QuestionStatistics(subContractorIds));
            }
        }

        Set<String> available = new HashSet<>(40);
        for (HseAuditQuestionnaire.Viewable questionnaire : questionnaires) {
            if (questionnaire.answers == null || questionnaire.answers.isEmpty()) {
                continue;
            }
            if (questionnaire.subContractor == null) {
                continue;
            }
            result.setAudit(questionnaire.subContractor.name);
            available.add(questionnaire.subContractor.name);
            for (HseAuditAnswer answer: questionnaire.answers) {
                if (questionIds != null && !questionIds.contains(answer.question.id)) {
                    continue;
                }
                if (answer.question.questionType.equals(HseAuditQuestionType.Option)) {
                    if (answer.question.title == null) {
                        continue;
                    }
                    String v = answer.question.title.get(LANG);
                    if (v == null) {
                        continue;
                    }
                    QuestionStatistics s = result.questionStatistics.get(v);
                    if (s == null) {
                        continue;
                    }
                    s.statistics
                        .get(questionnaire.subContractor.name)
                        .set(questionnaire, answer);
                }
            }
        }

        result.removeEmptySubContractors(available);
        result.setValues();
        if (params.getBoolean("excel", false)) {
            toExcel(response, dateTimeRange, result);
            return;
        }
        Response.writeJson(response, result);
    }


    private static class Result {

        public Map<String, QuestionStatistics> questionStatistics = new LinkedHashMap<>(50);
        public List<SubContractorOrder> subContractorOrdered;
        public List<String> statisticTitles = new ArrayList<>(10);
        public List<String> statisticTitlesOthers = new ArrayList<>(5);
        public Map<String, Integer> subContractorAuditCount = new HashMap<>(50);

        public void removeEmptySubContractors(Set<String> availableSubContractors) {
            for (QuestionStatistics s : questionStatistics.values()) {
                s.statistics.entrySet().removeIf(entry -> !availableSubContractors.contains(entry.getKey()));
            }
        }

        public void setAudit(String subContractor) {
            Integer c = subContractorAuditCount.get(subContractor);
            if (c == null) {
                c = 1;
            } else {
                ++c;
            }
            subContractorAuditCount.put(subContractor, c);
        }

        public void setValues() {
            Map<String, SubContractorOrder> subContractorOrderedMap = new HashMap<>(45);
            Set<String> otherStatisticTitles = new HashSet<>(5);
            for (QuestionStatistics s : questionStatistics.values()) {
                for (Map.Entry<String, QuestionStatistics.Statistics> entry : s.statistics.entrySet()) {
                    String name = entry.getKey();
                    QuestionStatistics.Statistics statistics = entry.getValue();

                    // subContractor
                    SubContractorOrder subContractorOrder = subContractorOrderedMap.get(name);
                    if (subContractorOrder == null) {
                        subContractorOrder = new SubContractorOrder(name, statistics.negativeScore);
                    } else {
                        subContractorOrder.update(statistics.negativeScore);
                    }
                    subContractorOrderedMap.put(name, subContractorOrder);

                    // titles
                    if (StringUtil.isNotEmpty(statistics.option3Label)) {
                        otherStatisticTitles.add(statistics.option3Label);
                    }
                    if (StringUtil.isNotEmpty(statistics.option4Label)) {
                        otherStatisticTitles.add(statistics.option4Label);
                    }
                }
            }
            subContractorOrdered = new ArrayList<>(subContractorOrderedMap.values());
            subContractorOrdered.sort(Comparator.comparingInt(po -> po.score));

            statisticTitles.add("تعداد کل  بازرسی");
            statisticTitles.add("ندارد");
            statisticTitles.add("درصد");
            statisticTitles.add("دارد");
            statisticTitles.add("درصد");
            statisticTitles.add("نامرتبط");
            statisticTitles.addAll(otherStatisticTitles);
            statisticTitlesOthers = new ArrayList<>(otherStatisticTitles);
        }

        public String toString() {
            return ObjectUtil.toString(this);
        }
    }


    private static class QuestionStatistics {

        // <SubContractor, statistics>
        public Map<String, Statistics> statistics = new LinkedHashMap<>(50);
        // <option-name>
        public Set<String> extraOptions = new HashSet<>(10);

        public QuestionStatistics() {

        }

        public QuestionStatistics(List<Long> subContractorIds) {
            try {
                for (SubContractor subContractor : Services.get(ServiceDtoCache.class).getList(SubContractor.class)) {
                    if (subContractorIds != null && !subContractorIds.isEmpty()) {
                        if (subContractorIds.contains(subContractor.id)) {
                            statistics.put(subContractor.name, new Statistics());
                        }
                    } else {
                        statistics.put(subContractor.name, new Statistics());
                    }
                }
            } catch (ServiceException ignore) {

            }
        }

        public String toString() {
            return ObjectUtil.toString(this);
        }


        private class Statistics {

            public int yes;
            public int no;
            public int na;
            public int option3;
            public int option4;
            public String option3Label;
            public String option4Label;
            public int negativeScore;

            public void set(HseAuditQuestionnaire.Viewable questionnaire, HseAuditAnswer answer) {
                if (answer.answer == null) {
                    return;
                }

                negativeScore += questionnaire.majorNoCount + questionnaire.criticalNoCount * 6;

                if (answer.answer.equals(HseAuditAnswerOption.Yes)) {
                    ++yes;
                } else if (answer.answer.equals(HseAuditAnswerOption.No)) {
                    ++no;
                } else if (answer.answer.equals(HseAuditAnswerOption.NA)) {
                    ++na;
                } else if (answer.answer.equals(HseAuditAnswerOption.Option3)) {
                    ++option3;
                    option3Label = answer.question.optionLabel.get(LANG).get("Option3");
                    extraOptions.add(option3Label);
                } else if (answer.answer.equals(HseAuditAnswerOption.Option4)) {
                    ++option4;
                    option4Label = answer.question.optionLabel.get(LANG).get("Option4");
                    extraOptions.add(option3Label);
                }
            }

            public String toString() {
                return ObjectUtil.toString(this);
            }
        }
    }


    private static class SubContractorOrder {

        public String name;
        public int score;

        public SubContractorOrder(String name, int negativeScore) {
            this.name = name;
            score = negativeScore;
        }

        public void update(int negativeScore) {
            score += negativeScore;
        }

        public String toString() {
            return ObjectUtil.toString(this);
        }
    }

    private void setDateHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleDateHeader(wb));
        cell.setCellValue(StringUtil.replace(value, '-', '/'));
    }

    private void setSubContractorHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleSubContractorHeader(wb));
        cell.setCellValue(value);
    }

    private void setStatisticsHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleStatisticsHeader(wb));
        cell.setCellValue(value);
    }

    private void setQuestion(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleQuestion(wb));
        cell.setCellValue(value);
    }

    private void setTotalAudit(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleTotalAudit(wb));
        cell.setCellValue(value);
    }

    private void setDataCell(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleDataCell(wb));
        cell.setCellValue(value);
    }

    private CellStyle getCellStyleDateHeader(Workbook workbook) {
        CellStyle style = styles.get("d");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("d", style);
        }
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setFont(workbook, style, (short) 11, true);
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(241, 0, 0), new DefaultIndexedColorMap()));
        return style;
    }

    private CellStyle getCellStyleSubContractorHeader(Workbook workbook) {
        CellStyle style = styles.get("p");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("p", style);
        }
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setFont(workbook, style, (short) 12, true);
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(121, 198, 235), new DefaultIndexedColorMap()));
        return style;
    }

    private CellStyle getCellStyleStatisticsHeader(Workbook workbook) {
        CellStyle style = styles.get("s");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("s", style);
        }
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setFont(workbook, style, (short) 11, true);
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(121, 198, 235), new DefaultIndexedColorMap()));
        return style;
    }

    private CellStyle getCellStyleQuestion(Workbook workbook) {
        CellStyle style = styles.get("q");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("q", style);
        }
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setFont(workbook, style, (short) 11, false);
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 234, 176), new DefaultIndexedColorMap()));
        return style;
    }

    private CellStyle getCellStyleTotalAudit(Workbook workbook) {
        CellStyle style = styles.get("tq");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("tq", style);
        }
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setFont(workbook, style, (short) 11, false);
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 244, 255), new DefaultIndexedColorMap()));
        return style;
    }

    private CellStyle getCellStyleDataCell(Workbook workbook) {
        CellStyle style = styles.get("ce");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("ce", style);
        }
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        setFont(workbook, style, (short) 11, false);
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255), new DefaultIndexedColorMap()));
        return style;
    }

    private void setFont(Workbook workbook, CellStyle style, Short size, boolean bold) {
        Font font = workbook.createFont();
        font.setBold(bold);
        font.setFontName("B Mitra");
        font.setFontHeightInPoints(size);
        style.setFont(font);
    }
}