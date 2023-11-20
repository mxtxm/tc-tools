package com.tctools.business.model.project.radiometric;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.common.util.*;
import com.tctools.web.patch.TestController;
import com.vantar.business.CommonModelMongo;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.VantarException;
import com.vantar.web.Params;
import org.dhatim.fastexcel.Worksheet;
import org.slf4j.*;
import javax.servlet.http.HttpServletResponse;


public class ExportWorkflowModel {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);

    private QueryBuilder q;
    private int rowIndex;


    public void createXlsx(Params params, HttpServletResponse response) throws VantarException {
        q = params.getQueryBuilder("q", new RadioMetricFlow.Viewable());
        buildXlsx(response);
    }

    private void buildXlsx(HttpServletResponse response) throws VantarException {
        Xlsx.create(
            response,
            "radiometric-search-export.xlsx",
            "Radio metric",
            (book, sheet) -> {
                sheet.rowHeight(0, 13 * 5);
                setHeader(sheet);
                setData(sheet);
            }
        );
    }

    private void setHeader(Worksheet sheet) {
        int colIndex = 0;
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Row");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Site code");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Site name");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Province");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "City");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Location");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "State");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Measurement date");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Technician");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Type");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "Complainer");
        colIndex = XlsxStyled.setHeader(sheet, colIndex, "CC number");
    }

    private void setData(Worksheet sheet) throws VantarException {
        CommonModelMongo.forEach(q, dto -> {
            RadioMetricFlow.Viewable flow = (RadioMetricFlow.Viewable) dto;
            ++rowIndex;

            int colIndex = 0;
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, rowIndex);
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.site.code);
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.site.name);
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.site.province.name);
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.site.city.name);
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.site.address);
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.lastState.name());
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex,
                flow.measurementDateTime == null ? "-" : flow.measurementDateTime.formatter().getDatePersian());
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.assignee == null ? "-" : flow.assignee.fullName);
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.isCc ? "CC" : "Normal");
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.complain == null ? "-" : flow.complain.complainerName);
            colIndex = XlsxStyled.setCell(sheet, rowIndex, colIndex, flow.complain == null ? "-" : flow.complain.ccnumber);
        });
    }
}