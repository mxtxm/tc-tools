package com.tctools.business.model.project.hseaudit.export;

import com.tctools.business.dto.location.Province;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.ExportCommon;
import com.vantar.business.CommonRepoMongo;
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


public class SignificanceSubcontractorProvinceComplete extends ExportCommon {

    private static final int UN_SAFE_PERCENT_1 = 20;
    private static final int UN_SAFE_PERCENT_2 = 30;
    private static final int UN_SAFE_PERCENT_3 = 40;

    private static final String LANG = "fa";
    private static Map<String, CellStyle> styles;


    private static void toExcel(
        HttpServletResponse response,
        DateTimeRange dateTimeRange,
        Result result
        ) throws ServerException {

        styles = new HashMap<>(10);

        Workbook wb = new XSSFWorkbook();

        // total
        Sheet sheet = wb.createSheet("آمار کلی استان ها");
        sheet.setRightToLeft(true);
        sheet.setDefaultRowHeight((short) 450);

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

        setProvinceHeader(wb, row1, firstCol, "آمار تجمیعی  استان ها");

        for (String title : result.statisticTitles) {
            setStatisticsHeader(wb, row2, ++lastCol, title);
        }

        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        firstCol += result.statisticTitles.size();

        int r = 1;
        for (Map.Entry<String, QuestionStatistics> e : result.statistics.entrySet()) {
            int c = 0;
            Row row = sheet.createRow(++r);

            String provinceName = e.getKey();
            setContractor(wb, row, c, provinceName);

            QuestionStatistics s = e.getValue();

            int t = s.totalAuditCount;
            setTotalAudit(wb, row, ++c).setCellValue(t);

            setDataCell(wb, row, ++c).setCellValue(s.totalCritical);
            setDataCell(wb, row, ++c).setCellValue(Math.round((s.totalCritical * 100.0 / t) * 100.0) / 100.0);

            setDataCell(wb, row, ++c).setCellValue(s.totalMajor);
            setDataCell(wb, row, ++c).setCellValue(Math.round((s.totalMajor * 100.0 / t) * 100.0) / 100.0);

            setDataCell(wb, row, ++c).setCellValue(s.totalSafe);
            setDataCell(wb, row, ++c).setCellValue(s.totalUnsafe);

            int statusIndex;
            String statusValue;
            double unsafePercent = s.totalUnsafe * 100 / (double) s.totalAuditCount;
            if (unsafePercent < UN_SAFE_PERCENT_1) {
                statusIndex = 1;
                statusValue = "ایمن";
            } else if (unsafePercent < UN_SAFE_PERCENT_2) {
                statusIndex = 2;
                statusValue = "ناایمن";
            } else if (unsafePercent < UN_SAFE_PERCENT_3) {
                statusIndex = 3;
                statusValue = "بسیار ناایمن";
            } else {
                statusIndex = 4;
                statusValue = "بسیار بسیار ناایمن";
            }
            setStatus(wb, row, ++c, statusIndex).setCellValue(statusValue);
        }


        for (int i = 0 ; i < firstCol ; ++i) {
            sheet.autoSizeColumn(i);
        }
        sheet.setColumnWidth(0, 60 * 256);
        sheet.setColumnWidth(1, 18 * 256);
        sheet.setColumnWidth(8, 23 * 256);

        // per province
        for (Map.Entry<String, QuestionStatistics> e : result.statistics.entrySet()) {
            String provinceName = e.getKey();
            sheet = wb.createSheet(provinceName);
            sheet.setRightToLeft(true);
            sheet.setDefaultRowHeight((short) 450);

            QuestionStatistics stats = e.getValue();

            row1 = sheet.createRow(0);
            setDateHeader(
                wb, row1, 0,
                "از تاریخ " + dateTimeRange.dateMin.formatter().getDatePersian() + "\n"
                    + "تا تاریخ " + dateTimeRange.dateMax.formatter().getDatePersian()
            );
            row2 = sheet.createRow(1);

            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

            firstRow = 0;
            lastRow = 0;
            firstCol = 1;
            lastCol = 0;

            setProvinceHeader(wb, row1, firstCol, provinceName);

            for (String title : result.statisticTitles) {
                setStatisticsHeader(wb, row2, ++lastCol, title);
            }

            sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
            firstCol += result.statisticTitles.size();

            r = 1;
            for (Map.Entry<String, QuestionStatistics.Statistics> entry : stats.statistics.entrySet()) {
                int c = 0;
                Row row = sheet.createRow(++r);

                String subContractorName = entry.getKey();
                setContractor(wb, row, c, subContractorName);

                QuestionStatistics.Statistics s = entry.getValue();

                int t = s.auditCount;
                setTotalAudit(wb, row, ++c).setCellValue(t);

                setDataCell(wb, row, ++c).setCellValue(s.critical);
                setDataCell(wb, row, ++c).setCellValue(Math.round((s.critical * 100.0 / t) * 100.0) / 100.0);

                setDataCell(wb, row, ++c).setCellValue(s.major);
                setDataCell(wb, row, ++c).setCellValue(Math.round((s.major * 100.0 / t) * 100.0) / 100.0);

                setDataCell(wb, row, ++c).setCellValue(s.safe);
                setDataCell(wb, row, ++c).setCellValue(s.unsafe);

                int statusIndex;
                String statusValue;
                double unsafePercent = s.unsafe * 100 / (double) s.auditCount;
                if (unsafePercent < UN_SAFE_PERCENT_1) {
                    statusIndex = 1;
                    statusValue = "ایمن";
                } else if (unsafePercent < UN_SAFE_PERCENT_2) {
                    statusIndex = 2;
                    statusValue = "ناایمن";
                } else if (unsafePercent < UN_SAFE_PERCENT_3) {
                    statusIndex = 3;
                    statusValue = "بسیار ناایمن";
                } else {
                    statusIndex = 4;
                    statusValue = "بسیار بسیار ناایمن";
                }
                setStatus(wb, row, ++c, statusIndex).setCellValue(statusValue);
            }

            for (int i = 0 ; i < firstCol ; ++i) {
                sheet.autoSizeColumn(i);
            }
            sheet.setColumnWidth(0, 60 * 256);
            sheet.setColumnWidth(1, 18 * 256);
            sheet.setColumnWidth(8, 18 * 256);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=assigned-"
            + ("observation-province-" + new DateTime().formatter().getDateTimeSimple()) + ".xlsx");

        try {
            wb.write(response.getOutputStream());
            wb.close();
        } catch (IOException e) {
            log.error("", e);
            throw new ServerException(AppLangKey.EXPORT_FAIL);
        }
    }

    public static void outputAggregate(Params params, HttpServletResponse response)
        throws InputException, NoContentException, ServerException {

        DateTimeRange dateTimeRange = params.getDateTimeRange("from", "to");
        if (dateTimeRange == null || !dateTimeRange.isValid()) {
            throw new InputException(VantarKey.INVALID_DATE);
        }
        List<Long> provinceIds = params.getLongList("provinceids");
        List<Long> subContractorIds = params.getLongList("subcontractorids");
        Boolean isTci = params.getBoolean("istci");
        Boolean isNotTci = params.getBoolean("isnottci");

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire(), new HseAuditQuestionnaire.Viewable());
        q.condition().in("lastState", HseAuditFlowState.PreApproved, HseAuditFlowState.Approved);
        q.condition().between("auditDateTime", dateTimeRange);
        if (provinceIds != null && !provinceIds.isEmpty()) {
            q.condition().inNumber("site.provinceId", provinceIds);
        }
        if (subContractorIds != null && !subContractorIds.isEmpty()) {
            q.condition().inNumber("subContractorId", subContractorIds);
        }
        if (isTci != null) {
            q.condition().equal("isTci", isTci);
        }
        if (isNotTci != null) {
            q.condition().equal("isNotTci", isNotTci);
        }

        List<HseAuditQuestionnaire.Viewable> questionnaires;
        try {
            questionnaires = CommonRepoMongo.getData(q, LANG);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        Result result = new Result();
        for (Province province : Services.get(ServiceDtoCache.class).getList(Province.class)) {
            if (provinceIds != null && !provinceIds.isEmpty()) {
                if (provinceIds.contains(province.id)) {
                    result.statistics.put(province.name.get(LANG), new QuestionStatistics(subContractorIds));
                }
            } else {
                result.statistics.put(province.name.get(LANG), new QuestionStatistics(subContractorIds));
            }
        }


        for (HseAuditQuestionnaire.Viewable questionnaire : questionnaires) {
            if (questionnaire.subContractor == null || StringUtil.isEmpty(questionnaire.subContractor.name)) {
                continue;
            }
            if (questionnaire.site.province == null || StringUtil.isEmpty(questionnaire.site.province.name)) {
                continue;
            }

            result.statistics
                .get(questionnaire.site.province.name).statistics
                .get(questionnaire.subContractor.name)
                .set(questionnaire);
        }

        result.removeEmpty();
        result.setValues();
        if (params.getBoolean("excel", false)) {
            toExcel(response, dateTimeRange, result);
            return;
        }
        Response.writeJson(response, result);
    }


    private static class Result {

        // <provinceName, Stats>
        public Map<String, QuestionStatistics> statistics = new LinkedHashMap<>(50);
        public List<String> statisticTitles = new ArrayList<>(10);

        public void removeEmpty() {


            for (QuestionStatistics s : statistics.values()) {
                s.statistics.entrySet().removeIf(entry -> entry.getValue().auditCount == 0);
            }

            statistics.entrySet().removeIf(entry -> {
                QuestionStatistics v = entry.getValue();
                return v.statistics.isEmpty();
            });
        }

        public void setValues() {
            statisticTitles.add("تعداد کل  بازرسی");
            statisticTitles.add("critical");
            statisticTitles.add("درصد");
            statisticTitles.add("major");
            statisticTitles.add("درصد");
            statisticTitles.add("تعداد ایمن");
            statisticTitles.add("تعداد ناایمن");
            statisticTitles.add("وضعیت فعالیت");
        }

        public String toString() {
            return ObjectUtil.toString(this);
        }
    }


    private static class QuestionStatistics {

        // <sub-contractor, statistics>
        public Map<String, Statistics> statistics = new LinkedHashMap<>(50);
        public int totalCritical;
        public int totalMajor;
        public int totalNegativeScore;
        public int totalAuditCount;
        public int totalSafe;
        public int totalUnsafe;

        public QuestionStatistics(List<Long> subContractorIds) {
            try {
                for (SubContractor subContractor : Services.get(ServiceDtoCache.class).getList(SubContractor.class)) {
                    if (subContractorIds != null && !subContractorIds.isEmpty()) {
                        if (subContractorIds.contains(subContractor.id)) {
                            statistics.put(subContractor.name, new Statistics());
                        }
                    } else  {
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

            public int critical;
            public int major;
            public int negativeScore;
            public int auditCount;
            public int safe;
            public int unsafe;

            public void set(HseAuditQuestionnaire.Viewable questionnaire) {
                ++auditCount;
                ++totalAuditCount;

                int t = questionnaire.majorNoCount + questionnaire.criticalNoCount * 6;
                negativeScore += t;
                totalNegativeScore += t;

                boolean isSafe = true;
                if (questionnaire.criticalNoCount >= Param.HSE_CRITICAL_FAIL_THRESHOLD) {
                    ++critical;
                    ++totalCritical;
                    isSafe = false;
                }
                if (questionnaire.majorNoCount >= Param.HSE_MAJOR_FAIL_THRESHOLD) {
                    ++major;
                    ++totalMajor;
                    isSafe = false;
                }
                if (isSafe) {
                    ++safe;
                    ++totalSafe;
                } else {
                    ++unsafe;
                    ++totalUnsafe;
                }
            }

            public String toString() {
                return ObjectUtil.toString(this);
            }
        }
    }


    private static class ProvinceOrder {

        public String name;
        public int score;
        public int total;

        public ProvinceOrder(String name, int negativeScore) {
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

    private static void setDateHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleDateHeader(wb));
        cell.setCellValue(StringUtil.replace(value, '-', '/'));
    }

    private static void setProvinceHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleProvinceHeader(wb));
        cell.setCellValue(value);
    }

    private static void setStatisticsHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleStatisticsHeader(wb));
        cell.setCellValue(value);
    }

    private static void setContractor(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleQuestion(wb));
        cell.setCellValue(value);
    }

    private static Cell setTotalAudit(Workbook wb, Row row, int col) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleTotalAudit(wb));
        return cell;
    }

    private static Cell setStatus(Workbook wb, Row row, int col, int s) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleStatus(wb, s));
        return cell;
    }

    private static Cell setDataCell(Workbook wb, Row row, int col) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleDataCell(wb));
        return cell;
    }

    private static CellStyle getCellStyleDateHeader(Workbook workbook) {
        CellStyle style = styles.get("d");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setFont(workbook, style, (short) 12, true);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(241, 0, 0), new DefaultIndexedColorMap()));
            styles.put("d", style);
        }
        return style;
    }

    private static CellStyle getCellStyleProvinceHeader(Workbook workbook) {
        CellStyle style = styles.get("p");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setFont(workbook, style, (short) 12, true);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(121, 198, 235), new DefaultIndexedColorMap()));
            styles.put("p", style);
        }
        return style;
    }

    private static CellStyle getCellStyleStatisticsHeader(Workbook workbook) {
        CellStyle style = styles.get("s");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setFont(workbook, style, (short) 12, true);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(121, 198, 235), new DefaultIndexedColorMap()));
            styles.put("s", style);
        }
        return style;
    }

    private static CellStyle getCellStyleQuestion(Workbook workbook) {
        CellStyle style = styles.get("q");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setFont(workbook, style, (short) 13, false);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 234, 176), new DefaultIndexedColorMap()));
            styles.put("q", style);
        }
        return style;
    }

    private static CellStyle getCellStyleTotalAudit(Workbook workbook) {
        CellStyle style = styles.get("tq");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setFont(workbook, style, (short) 11, false);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(216, 244, 255), new DefaultIndexedColorMap()));
            styles.put("tq", style);
        }
        return style;
    }

    private static CellStyle getCellStyleDataCell(Workbook workbook) {
        CellStyle style = styles.get("ce");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setFont(workbook, style, (short) 11, false);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 255), new DefaultIndexedColorMap()));
            styles.put("ce", style);
        }
        return style;
    }

    private static CellStyle getCellStyleStatus(Workbook workbook, int status) {
        CellStyle style = styles.get("st" + status);
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setFont(workbook, style, (short) 13, false);
            if (status == 1) {
                ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(127, 255, 50), new DefaultIndexedColorMap()));
            }
            if (status == 2) {
                ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(252, 255, 0), new DefaultIndexedColorMap()));
            }
            if (status == 3) {
                ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 186, 0), new DefaultIndexedColorMap()));
            }
            if (status == 4) {
                ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 108, 0), new DefaultIndexedColorMap()));
            }
            styles.put("st" + status, style);
        }
        return style;
    }

    private static void setFont(Workbook workbook, CellStyle style, Short size, boolean bold) {
        Font font = workbook.createFont();
        font.setBold(bold);
        font.setFontName("B Mitra");
        font.setFontHeightInPoints(size);
        style.setFont(font);
    }
}