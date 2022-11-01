package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.util.ExportCommon;
import com.vantar.business.CommonModelMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryResultBase;
import com.vantar.exception.ServerException;
import com.vantar.web.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class ExportSiteState extends ExportCommon {

    private final Map<String, CellStyle> styles = new HashMap<>(5);
    private int rowIndex;


    public void excel(Params params, HttpServletResponse response) throws ServerException {
        try (Workbook wb = new XSSFWorkbook()) {

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
            sheetSp.setColumnWidth(c, 4000);

            CommonModelMongo.forEach(new RadioMetricFlow.Viewable(), new QueryResultBase.Event() {
                @Override
                public void afterSetData(Dto dto) {
                    RadioMetricFlow.Viewable flow = (RadioMetricFlow.Viewable) dto;

                    Row row = sheetSp.createRow(++rowIndex);
                    int c = 0;
                    setDataPink(wb, row, c++, flow.isCc ? "CC" : "Normal");
                    setDataPink(wb, row, c++, flow.lastState.toString());
                    setDataPink(wb, row, c++, flow.site.code);

                    setDataYellow(wb, row, c++, flow.site.province == null ? "" : getValue(flow.site.province.name));
                    setDataYellow(wb, row, c++, flow.site.city == null ? "" : getValue(flow.site.city.name));
                    setDataYellow(wb, row, c++, getValue(flow.site.address));
                    setDataYellow(wb, row, c++, flow.site.location == null ? "" : getValue(flow.site.location.latitude));
                    setDataYellow(wb, row, c,   flow.site.location == null ? "" : getValue(flow.site.location.longitude));
                }

                @Override
                public void afterSetData(Dto dto, List<?> list) {

                }
            });

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=radiometrics-state.xlsx");

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