package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.dto.site.Sector;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.util.ExportCommon;
import com.vantar.business.CommonModelMongo;
import com.vantar.database.datatype.Location;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.util.number.NumberUtil;
import com.vantar.web.Params;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class ExportSearchControl extends ExportCommon {

    public void excel(Params params, HttpServletResponse response) throws VantarException {
        QueryBuilder q = params.getQueryBuilder(new RadioMetricFlow.Viewable());
        if (q == null) {
            throw new InputException("NO SEARCH COMMAND");
        }
        List<RadioMetricFlow.Viewable> items = CommonModelMongo.getData(q);
        try (Workbook wb = new XSSFWorkbook()) {

            Sheet sheetSp = wb.createSheet("Normal");
            Sheet sheetCc = wb.createSheet("CC");
            int rowIndexSp = 0;
            int rowIndexCc = 0;
            Row rowSp = sheetSp.createRow(rowIndexSp);
            Row rowCc = sheetCc.createRow(rowIndexCc);

            rowSp.setHeight((short) 1400);
            rowCc.setHeight((short) 1400);

            sheetSp.setDefaultRowHeight((short) 400);
            sheetCc.setDefaultRowHeight((short) 400);

            int c = 0;
            setHeader(wb, rowSp, c, "Row");
            sheetSp.setColumnWidth(c++, 1500);
            setHeader(wb, rowSp, c, "Date");
            sheetSp.setColumnWidth(c++, 3000);
            setHeader(wb, rowSp, c, "استان");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "شهر");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "Site ID");
            sheetSp.setColumnWidth(c++, 3000);
            setHeader(wb, rowSp, c, "Location Address");
            sheetSp.setColumnWidth(c++, 23000);
            setHeader(wb, rowSp, c, "Site Name");
            sheetSp.setColumnWidth(c++, 7000);
            setHeader(wb, rowSp, c, "Report Type");
            sheetSp.setColumnWidth(c++, 3500);
            setHeader(wb, rowSp, c, "Description");
            sheetSp.setColumnWidth(c++, 10000);
            setHeader(wb, rowSp, c, "Latitude");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "Longitude");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "ميانگين چگالي توان در 6 دقيقه (µ w/cm2) (100cm)");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "ميانگين چگالي توان در 6 دقيقه (µ w/cm2) (150cm)");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "ميانگين چگالي توان در 6 دقيقه (µ w/cm2) (170cm)");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "(100cm) (مقایسه با استاندارد ICNIRP) (درصد٪) \n مقدار اندازه گيري شده نسبت به حد پرتو گيري مردم");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "(150cm) (مقایسه با استاندارد ICNIRP) (درصد٪) \n مقدار اندازه گيري شده نسبت به حد پرتو گيري مردم");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "(170cm) (مقایسه با استاندارد ICNIRP) (درصد٪) \n مقدار اندازه گيري شده نسبت به حد پرتو گيري مردم");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "Measured Sector");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "Antenna Height from the measured location");
            sheetSp.setColumnWidth(c++, 4000);
            setHeader(wb, rowSp, c, "Type of Site");
            sheetSp.setColumnWidth(c, 4000);

            c = 0;
            setHeader(wb, rowCc, c, "Row");
            sheetCc.setColumnWidth(c++, 1500);
            setHeader(wb, rowCc, c, "Date");
            sheetCc.setColumnWidth(c++, 3000);
            setHeader(wb, rowCc, c, "استان");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "شهر");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "Site ID");
            sheetCc.setColumnWidth(c++, 3000);
            setHeader(wb, rowCc, c, "Location Address");
            sheetCc.setColumnWidth(c++, 23000);
            setHeader(wb, rowCc, c, "Site Name");
            sheetCc.setColumnWidth(c++, 7000);
            setHeader(wb, rowCc, c, "Report Type");
            sheetCc.setColumnWidth(c++, 3500);
            setHeader(wb, rowCc, c, "CC Number");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "Description");
            sheetCc.setColumnWidth(c++, 10000);
            setHeader(wb, rowCc, c, "Latitude");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "Longitude");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "ميانگين چگالي توان در 6 دقيقه (µ w/cm2) (100cm)");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "ميانگين چگالي توان در 6 دقيقه (µ w/cm2) (150cm)");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "ميانگين چگالي توان در 6 دقيقه (µ w/cm2) (170cm)");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "(100cm) (مقایسه با استاندارد ICNIRP) (درصد٪) \n مقدار اندازه گيري شده نسبت به حد پرتو گيري مردم");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "(150cm) (مقایسه با استاندارد ICNIRP) (درصد٪) \n مقدار اندازه گيري شده نسبت به حد پرتو گيري مردم");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "(170cm) (مقایسه با استاندارد ICNIRP) (درصد٪) \n مقدار اندازه گيري شده نسبت به حد پرتو گيري مردم");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "Measured Sector");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "Antenna Height from the measured location");
            sheetCc.setColumnWidth(c++, 4000);
            setHeader(wb, rowCc, c, "Type of Site");
            sheetCc.setColumnWidth(c, 4000);

            int iSc = 0;
            int iCc = 0;
            for (RadioMetricFlow.Viewable flow : items) {
                boolean isCc = !RadioMetricComplain.isEmpty(flow.complain);
                c = 0;

                if (isCc) {
                    rowCc = sheetCc.createRow(++rowIndexCc);
                } else {
                    rowSp = sheetSp.createRow(++rowIndexSp);
                }

                setDataPink(wb, isCc ? rowCc : rowSp, c++, Integer.toString(isCc ? ++iCc : ++iSc));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.measurementDateTime.formatter().getDatePersian()));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.reportedProvince == null ? flow.province.name : flow.reportedProvince));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.reportedCity == null ? flow.city.name : flow.reportedCity));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, flow.site.code); //کد سایت

                setDataYellow(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.spotAddress));
                setDataYellow(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.site.name));

                setDataPink(wb, isCc ? rowCc : rowSp, c++, "SP");
                if (isCc) {
                    setDataPink(wb, rowCc, c++, getValueObj(flow.complain.ccnumber));
                }
                setDataPink(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.comments));

                Location location = flow.spotLocation == null ? flow.site.location : flow.spotLocation;
                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(location == null ? 0 : location.latitude)));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(location == null ? 0 : location.longitude)));

                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(flow.densityAverage6min100)));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(flow.densityAverage6min150)));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(flow.densityAverage6min170)));

                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(NumberUtil.round(flow.densityAverage6min100 == null ? 0
                    : flow.densityAverage6min100 / 4.4, 3))));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(NumberUtil.round(flow.densityAverage6min150 == null ? 0
                    : flow.densityAverage6min150 / 4.4, 3))));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(NumberUtil.round(flow.densityAverage6min170 == null ? 0
                    : flow.densityAverage6min170 / 4.4, 3))));

                String selectedSector = "";
                if (flow.sectors == null) {

                } else {
                    for (Sector.Viewable s : flow.sectors) {
                        if (s.selected != null && s.selected) {
                            selectedSector = s.title;
                            break;
                        }
                    }
                }

                setDataPink(wb, isCc ? rowCc : rowSp, c++, getValueObj(selectedSector));
                setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(flow.measurementHeight)));
                setDataPink(wb, isCc ? rowCc : rowSp, c, getValueObj(flow.site.siteType == null ? "" : flow.site.siteType.name));
            }

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=Wave-Control-Report.xlsx");

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
        CellStyle style = workbook.createCellStyle();
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
        return style;
    }

    private CellStyle getCellStylePink(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(252, 228, 214), new DefaultIndexedColorMap()));
        return style;
    }

    private CellStyle getCellStyleYellow(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(new java.awt.Color(251, 255, 106), new DefaultIndexedColorMap()));
        return style;
    }
}