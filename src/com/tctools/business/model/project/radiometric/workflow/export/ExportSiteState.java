package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.ExportCommon;
import com.vantar.database.common.Db;
import com.vantar.exception.*;
import com.vantar.util.file.FileUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.*;
import java.io.*;
import java.util.*;


public class ExportSiteState extends ExportCommon {

    private static final Logger log = LoggerFactory.getLogger(ExportSiteState.class);
    private final Map<String, CellStyle> styles = new HashMap<>(5);
    private int rowIndex;
    private int i;


    public static void createExcelFile() throws VantarException {
        new ExportSiteState().excel();
    }

    public void excel() throws VantarException {
        log.info(" >> start creating site-states.xlsx");
        String filename = Param.RADIO_METRIC_FILES + "site-states.xlsx";
        FileUtil.removeFile(filename);
        try (Workbook wb = new XSSFWorkbook();
             OutputStream outputStream = new FileOutputStream(filename)) {

            Sheet sheetSp = wb.createSheet("Sites states");
            Row row = sheetSp.createRow(rowIndex);

            row.setHeight((short) 1400);

            int c = 0;
            setHeader(wb, row, c, "Type");
            sheetSp.setColumnWidth(c++, 3500);
            setHeader(wb, row, c, "State");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, row, c, "Site Code");
            sheetSp.setColumnWidth(c++, 3000);
            setHeader(wb, row, c, "استان");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, row, c, "شهر");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, row, c, "Address");
            sheetSp.setColumnWidth(c++, 23000);
            setHeader(wb, row, c, "Latitude");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, row, c, "Longitude");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, row, c, "Status");
            sheetSp.setColumnWidth(c++, 5000);

            Db.modelMongo.forEach(new RadioMetricFlow.Viewable(), dto -> {
                RadioMetricFlow.Viewable flow = (RadioMetricFlow.Viewable) dto;

                Row row1 = sheetSp.createRow(++rowIndex);
                int c1 = 0;
                setDataPink(wb, row1, c1++, flow.isCc ? "CC" : "Normal");
                setDataPink(wb, row1, c1++, flow.lastState.toString());
                setDataPink(wb, row1, c1++, flow.site.code);

                setDataYellow(wb, row1, c1++, flow.site.province == null ? "" : getValue(flow.site.province.name));
                setDataYellow(wb, row1, c1++, flow.site.city == null ? "" : getValue(flow.site.city.name));
                setDataYellow(wb, row1, c1++, getValue(flow.site.address));
                setDataYellow(wb, row1, c1++, flow.site.location == null ? "" : getValue(flow.site.location.latitude));
                setDataYellow(wb, row1, c1++,   flow.site.location == null ? "" : getValue(flow.site.location.longitude));
                setDataYellow(wb, row1, c1++,   getValue(flow.site.btsStatus == null ? "" : flow.site.btsStatus.name));
                ++i;
                return true;
            });

            wb.write(outputStream);
        } catch (IOException e) {
            log.error("", e);
            throw new ServerException(AppLangKey.EXPORT_FAIL);
        }
        log.info(" end creating site-states.xlsx <<");
    }

    private void setHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleHeader(wb));
        cell.setCellValue(value);
    }

    private void setDataPink(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStylePink(wb));
        cell.setCellValue(value);
    }

    private void setDataYellow(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleYellow(wb));
        cell.setCellValue(value);
    }

    private CellStyle getCellStyleHeader(Workbook workbook) {
        CellStyle style = styles.get("Header");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            style.setWrapText(true);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(231, 230, 230), new DefaultIndexedColorMap()));
            styles.put("Header", style);
        }
        return style;
    }

    private CellStyle getCellStylePink(Workbook workbook) {
        CellStyle style = styles.get("Pink");
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
            styles.put("Pink", style);
        }
        return style;
    }

    private CellStyle getCellStyleYellow(Workbook workbook) {
        CellStyle style = styles.get("Yellow");
        if (style == null) {
            style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(251, 255, 106), new DefaultIndexedColorMap()));
            styles.put("Yellow", style);
        }
        return style;
    }

}