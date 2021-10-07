package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.util.ExportCommon;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.datatype.Location;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.string.StringUtil;
import com.vantar.web.Params;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class ExportWaveControl extends ExportCommon {

    private static Map<String, CellStyle> styles;


    public static void excel(Params params, HttpServletResponse response) throws ServerException {
        styles = new HashMap<>(5);

        DateTime date = params.getDateTime("minDate", "1399-12-01");

        List<RadioMetricFlow.Viewable> items;
        try {
            if (params.getString("all") == null) {
                QueryBuilder q = new QueryBuilder(new RadioMetricFlow.Viewable());
                q.sort("measurementDateTime:asc");
                q.condition().in("lastState", RadioMetricFlowState.Approved, RadioMetricFlowState.Verified);
                q.condition().greaterThan("measurementDateTime", date);
                items = CommonRepoMongo.getData(q, "fa");
            } else {
                items = CommonRepoMongo.getData(new RadioMetricFlow.Viewable(), "fa");
            }
        } catch (DatabaseException | NoContentException e) {
            log.error("!", e);
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        Workbook wb = new XSSFWorkbook();

        Sheet sheetSp = wb.createSheet("Controller Sites");
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

        Map<Long, Dto> siteTypesDic = Services.get(ServiceDtoCache.class).getMap(SiteType.class);

        int iSc = 0;
        int iCc = 0;
        for (RadioMetricFlow.Viewable flow : items) {
            if (flow.comments != null && flow.comments.contains("CONVERT")) {
                continue;
            }

            boolean isCc = !RadioMetricComplain.isEmpty(flow.complain);
            c = 0;

            if (isCc) {
                rowCc = sheetCc.createRow(++rowIndexCc);
            } else {
                rowSp = sheetSp.createRow(++rowIndexSp);
            }

            setDataPink(wb, isCc ? rowCc : rowSp, c++, Integer.toString(isCc ? ++iCc : ++iSc));
            setDataPink(wb, isCc ? rowCc : rowSp, c++, flow.measurementDateTime == null ?
                "" : StringUtil.replace(getValueObj(flow.measurementDateTime.formatter().getDatePersian()), '-', '/'));
            setDataPink(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.reportedProvince == null ? flow.province.name : flow.reportedProvince));
            setDataPink(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.reportedCity == null ? flow.city.name : flow.reportedCity));
            setDataPink(wb, isCc ? rowCc : rowSp, c++, flow.site.code); //کد سایت

            setDataYellow(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.spotAddress));
            setDataYellow(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.site.name));

            setDataPink(wb, isCc ? rowCc : rowSp, c++, isCc ? "CC" : "SP");
            if (isCc) {
                setDataPink(wb, rowCc, c++, getValueObj(flow.complain.ccnumber));
            }
            setDataPink(wb, isCc ? rowCc : rowSp, c++, getValueObj(flow.comments));

            Location location = flow.spotLocation == null ? null : flow.spotLocation;
            setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(location == null ? 0 : location.latitude)));
            setDataPink(wb, isCc ? rowCc : rowSp, c++, Double.toString(getValueObj(location == null ? 0 : location.longitude)));

            setDataPink(wb, isCc ? rowCc : rowSp, c++, getValue(flow.densityAverage6min100));
            setDataPink(wb, isCc ? rowCc : rowSp, c++, getValue(flow.densityAverage6min150));
            setDataPink(wb, isCc ? rowCc : rowSp, c++, getValue(flow.densityAverage6min170));

            setDataPink(
                wb,
                isCc ? rowCc : rowSp,
                c++,
                getValue(flow.densityAverageDivMinRadiation100)
            );
            setDataPink(
                wb,
                isCc ? rowCc : rowSp,
                c++,
                getValue(flow.densityAverageDivMinRadiation150)
            );
            setDataPink(
                wb,
                isCc ? rowCc : rowSp,
                c++,
                getValue(flow.densityAverageDivMinRadiation170)
            );

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
            setDataPink(wb, isCc ? rowCc : rowSp, c++, getValue(flow.measurementHeight));

            String siteTypeName;
            if (flow.site.siteType == null) {
                siteTypeName = "";
            } else {
                SiteType siteType = (SiteType) siteTypesDic.get(flow.site.siteType.id);
                siteTypeName = siteType == null ? "" : siteType.name.get("en");
                if (siteTypeName == null) {
                    siteTypeName = "";
                }
            }
            setDataPink(wb, isCc ? rowCc : rowSp, c, siteTypeName);
        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=Wave-Control-Report.xlsx");

        try {
            wb.write(response.getOutputStream());
            wb.close();
        } catch (IOException e) {
            log.error("", e);
            throw new ServerException(AppLangKey.EXPORT_FAIL);
        }
    }

    private static void setHeader(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleHeader(wb));
        cell.setCellValue(value);
    }

    private static void setDataPink(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStylePink(wb));
        cell.setCellValue(value);
    }

    private static void setDataYellow(Workbook wb, Row row, int col, String value) {
        Cell cell = row.createCell(col);
        cell.setCellStyle(getCellStyleYellow(wb));
        cell.setCellValue(value);
    }

    private static CellStyle getCellStyleHeader(Workbook workbook) {
        CellStyle style = styles.get("Header");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("Header", style);
        }
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

    private static CellStyle getCellStylePink(Workbook workbook) {
        CellStyle style = styles.get("Pink");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("Pink", style);
        }
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

    private static CellStyle getCellStyleYellow(Workbook workbook) {
        CellStyle style = styles.get("Yellow");
        if (style == null) {
            style = workbook.createCellStyle();
            styles.put("Yellow", style);
        }
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