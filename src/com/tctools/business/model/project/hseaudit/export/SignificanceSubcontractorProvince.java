package com.tctools.business.model.project.hseaudit.export;

import com.tctools.business.dto.location.Province;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.ExportCommon;
import com.vantar.business.*;
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


public class SignificanceSubcontractorProvince extends ExportCommon {

    private static final String LANG = "fa";
    private final Map<String, CellStyle> styles = new HashMap<>(10);


    private void toExcel(
        HttpServletResponse response,
        DateTimeRange dateTimeRange,
        Result result
        ) throws ServerException {

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Critical & Major per sub contractor & province");
            sheet.setRightToLeft(true);

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

            for (ProvinceOrder provinceOrder : result.provinceOrdered) {
                setProvinceHeader(wb, row1, firstCol, provinceOrder.name);
                for (String title : result.statisticTitles) {
                    setStatisticsHeader(wb, row2, ++lastCol, title);
                }
                sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
                firstCol += result.statisticTitles.size();
            }
            setProvinceHeader(wb, row1, firstCol, "کل");
            setStatisticsHeader(wb, row2, ++lastCol, "بازرسی");

            int totatotal = 0;
            int r = 1;
            for (Map.Entry<String, QuestionStatistics> entry : result.questionStatistics.entrySet()) {
                int c = 0;
                Row row = sheet.createRow(++r);
                setContractor(wb, row, c, entry.getKey());

                int tRowAuditCount = 0;
                for (ProvinceOrder provinceOrder : result.provinceOrdered) {
                    QuestionStatistics.Statistics s = entry.getValue().statistics.get(provinceOrder.name);

                    int t = s.provinceAuditCount;
                    setTotalAudit(wb, row, ++c, Integer.toString(t));
                    tRowAuditCount += t;

                    setDataCell(wb, row, ++c, Integer.toString(s.critical));
                    setDataCell(wb, row, ++c, Double.toString(
                        Math.round((s.critical * 100.0 / t) * 100.0) / 100.0
                    ));

                    setDataCell(wb, row, ++c, Integer.toString(s.major));
                    setDataCell(wb, row, ++c, Double.toString(
                        Math.round((s.major * 100.0 / t) * 100.0) / 100.0
                    ));
                }
                setDataCell(wb, row, ++c, Integer.toString(tRowAuditCount));
                totatotal += tRowAuditCount;
//                totals.put(c, totatotal * 1.0);
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

        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire.Viewable());
        q.condition().in("lastState", HseAuditFlowState.PreApproved, HseAuditFlowState.Approved);
        q.condition().between("auditDateTime", dateTimeRange);
        if (provinceIds != null && !provinceIds.isEmpty()) {
            q.condition().inNumber("site.provinceId", provinceIds);
        }
        if (subContractorIds != null && !subContractorIds.isEmpty()) {
            q.condition().inNumber("subContractorId", subContractorIds);
        }

        List<HseAuditQuestionnaire.Viewable> questionnaires = CommonModelMongo.getData(q, LANG);

        Result result = new Result();
        for (SubContractor subContractor : Services.get(ServiceDtoCache.class).getList(SubContractor.class)) {
            if (subContractorIds != null && !subContractorIds.isEmpty()) {
                if (subContractorIds.contains(subContractor.id)) {
                    result.questionStatistics.put(subContractor.name, new QuestionStatistics(provinceIds));
                }
            } else {
                result.questionStatistics.put(subContractor.name, new QuestionStatistics(provinceIds));
            }
        }

        Set<String> availableProvinces = new HashSet<>(40);
        for (HseAuditQuestionnaire.Viewable questionnaire : questionnaires) {
            if (questionnaire.subContractor == null || StringUtil.isEmpty(questionnaire.subContractor.name)) {
                continue;
            }
            availableProvinces.add(questionnaire.site.province.name);

            result.questionStatistics
                .get(questionnaire.subContractor.name).statistics
                .get(questionnaire.site.province.name)
                .set(questionnaire);
        }

        result.removeEmptyProvinces(availableProvinces);
        result.setValues();
        if (params.getBoolean("excel", false)) {
            toExcel(response, dateTimeRange, result);
            return;
        }
        Response.writeJson(response, result);
    }


    private static class Result {

        public Map<String, QuestionStatistics> questionStatistics = new LinkedHashMap<>(50);
        public List<ProvinceOrder> provinceOrdered;
        public List<String> statisticTitles = new ArrayList<>(10);

        public void removeEmptyProvinces(Set<String> availableProvinces) {
            for (QuestionStatistics s : questionStatistics.values()) {
                s.statistics.entrySet().removeIf(entry -> !availableProvinces.contains(entry.getKey()));
            }
        }

        public void setValues() {
            Map<String, ProvinceOrder> provinceOrderedMap = new HashMap<>(45);
            for (QuestionStatistics s : questionStatistics.values()) {
                for (Map.Entry<String, QuestionStatistics.Statistics> entry : s.statistics.entrySet()) {
                    String provinceName = entry.getKey();
                    QuestionStatistics.Statistics statistics = entry.getValue();

                    ProvinceOrder provinceOrder = provinceOrderedMap.get(provinceName);
                    if (provinceOrder == null) {
                        provinceOrder = new ProvinceOrder(provinceName, statistics.negativeScore);
                    } else {
                        provinceOrder.update(statistics.negativeScore);
                    }

                    provinceOrder.total += statistics.provinceAuditCount;
                    provinceOrderedMap.put(provinceName, provinceOrder);
                }
            }
            provinceOrdered = new ArrayList<>(provinceOrderedMap.values());
            provinceOrdered.sort(Comparator.comparingInt(po -> po.total == 0 ? 0 : po.score / po.total));

            statisticTitles.add("تعداد کل  بازرسی");
            statisticTitles.add("critical");
            statisticTitles.add("درصد");
            statisticTitles.add("major");
            statisticTitles.add("درصد");
        }

        public String toString() {
            return ObjectUtil.toString(this);
        }
    }


    private static class QuestionStatistics {

        // <province, statistics>
        public Map<String, Statistics> statistics = new LinkedHashMap<>(50);

        public QuestionStatistics(List<Long> provinceIds) {
            try {
                for (Province province : Services.get(ServiceDtoCache.class).getList(Province.class)) {
                    if (provinceIds != null && !provinceIds.isEmpty()) {
                        if (provinceIds.contains(province.id)) {
                            statistics.put(province.name.get(LANG), new Statistics());
                        }
                    } else  {
                        statistics.put(province.name.get(LANG), new Statistics());
                    }
                }
            } catch (ServiceException ignore) {

            }
        }

        public String toString() {
            return ObjectUtil.toString(this);
        }


        private static class Statistics {

            public int critical;
            public int major;
            public int negativeScore;
            public int provinceAuditCount;

            public void set(HseAuditQuestionnaire.Viewable questionnaire) {
                ++provinceAuditCount;
                negativeScore += questionnaire.majorNoCount + questionnaire.criticalNoCount * 6;

                if (questionnaire.criticalNoCount >= Param.HSE_CRITICAL_FAIL_THRESHOLD) {
                    ++critical;
                }
                if (questionnaire.majorNoCount >= Param.HSE_MAJOR_FAIL_THRESHOLD) {
                    ++major;
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

    private void setDateHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleDateHeader(wb));
        cell.setCellValue(StringUtil.replace(value, '-', '/'));
    }

    private void setProvinceHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleProvinceHeader(wb));
        cell.setCellValue(value);
    }

    private void setStatisticsHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleStatisticsHeader(wb));
        cell.setCellValue(value);
    }

    private void setContractor(Workbook wb, Row row, int col, String value) {
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

    private CellStyle getCellStyleProvinceHeader(Workbook workbook) {
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