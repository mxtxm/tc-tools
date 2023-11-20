package com.tctools.common.util;

import org.dhatim.fastexcel.*;


public class XlsxStyled {

    public static final String ORANGE = "fb5d00";
    public static final String RED = "fa0300";
    public static final String BLACK = "000000";
    public static final String WHITE = "ffffff";
    public static final String GREEN = "80FF00";
    public static final String YELLOW = "FFff00";

    /**
     * columnIndex > zero based
     */
    public static int setImportHeader(Worksheet sheet, int columnIndex, Object value) {
        sheet.value(0, columnIndex, value == null ? "" : value.toString());
        sheet.style(0, columnIndex)
            .horizontalAlignment("center")
            .wrapText(true)
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(9)
            .bold()
            .fillColor("FF7700")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * columnIndex > zero based
     */
    public static int setImportHeader2(Worksheet sheet, int columnIndex, Object value) {
        sheet.value(1, columnIndex, value == null ? "" : value.toString());
        sheet.style(1, columnIndex)
            .horizontalAlignment("center")
            .wrapText(true)
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(9)
            .fillColor("80c4ff")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * columnIndex > zero based
     */
    public static int setImportResultHeader(Worksheet sheet, int columnIndex, Object value) {
        sheet.value(0, columnIndex, value == null ? "" : value.toString());
        sheet.style(0, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(9)
            .bold()
            .fillColor("FFC400")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * columnIndex > zero based
     */
    public static int setImportResultMessage(Worksheet sheet, int rowIndex, int columnIndex, Object value, String color) {
        sheet.rowHeight(rowIndex, 12 * 11);
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("left")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(9)
            .fillColor(color)
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * columnIndex > zero based
     */
    public static int setImportCell(Worksheet sheet, int rowIndex, int columnIndex, Object value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * columnIndex > zero based
     */
    public static int setHeader(Worksheet sheet, int columnIndex, Object value) {
        return setHeader(sheet, columnIndex, value, XlsxStyled.ORANGE, XlsxStyled.BLACK);
    }

    public static int setHeader(Worksheet sheet, int columnIndex, Object value, String color, String fontColor) {
        sheet.value(0, columnIndex, value == null ? "" : value.toString());
        sheet.style(0, columnIndex)
            .horizontalAlignment("center")
            .wrapText(true)
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .bold()
            .fillColor(color)
            .fontColor(fontColor)
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }


    /**
     * rowIndex > zero based
     * columnIndex > zero based
     */
    public static int setCellGrey(Worksheet sheet, int rowIndex, int columnIndex, String value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value);
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .fillColor("EEEEEE")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * rowIndex > zero based
     * columnIndex > zero based
     */
    public static int setCell(Worksheet sheet, int rowIndex, int columnIndex, Object value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .fillColor("FFFFFF")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    public static int setCellRight(Worksheet sheet, int rowIndex, int columnIndex, Object value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("right")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .fillColor("FFFFFF")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * rowIndex > zero based
     * columnIndex > zero based
     */
    public static int setCellBold(Worksheet sheet, int rowIndex, int columnIndex, Object value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .bold()
            .fillColor("FFFFFF")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    public static int setCellBoldOrange(Worksheet sheet, int rowIndex, int columnIndex, Object value, String fontColor, String bgColor) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .bold()
            .fillColor(bgColor)
            .fontColor(fontColor)
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * rowIndex > zero based
     * columnIndex > zero based
     */
    public static int setCellSide(Worksheet sheet, int rowIndex, int columnIndex, String value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value);
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("left")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .fillColor("f5f5f5")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * rowIndex > zero based
     * columnIndex > zero based
     */
    public static int setCellSideBold(Worksheet sheet, int rowIndex, int columnIndex, String value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value);
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("left")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .bold()
            .fillColor("f5f5f5")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * rowIndex > zero based
     * columnIndex > zero based
     */
    public static int setCellBoldOrange(Worksheet sheet, int rowIndex, int columnIndex, String value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value);
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("right")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .bold()
            .fillColor(ORANGE)
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }




    /**
     * HSE CUSTOM  > > >
     */

    public static int setHseHeader(Worksheet sheet, int columnIndex, Object value, String color, double width) {
        sheet.width(columnIndex, width);
        sheet.value(0, columnIndex, value == null ? "" : value.toString());
        sheet.style(0, columnIndex)
            .horizontalAlignment("center")
            .wrapText(true)
            .verticalAlignment("center")
            .fontName("Times New Roman")
            .fontSize(10)
            .bold()
            .fillColor(color)
            .fontColor(Color.BLACK)
            .borderStyle(BorderStyle.THIN)
            .borderColor("111111")
            .set();
        return ++columnIndex;
    }

    public static int setHse1BoldOrangeThin(Worksheet sheet, int columnIndex, String value, String color) {
        sheet.value(1, columnIndex, value == null ? "" : value);
        sheet.style(1, columnIndex)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Times New Roman")
            .fontSize(9)
            .bold()
            .fontColor(Color.BLACK)
            .fillColor(color)
            .borderStyle(BorderStyle.THIN)
            .borderColor("111111")
            .set();
        return ++columnIndex;
    }

    public static int setHseCell(Worksheet sheet, int rowIndex, int columnIndex, Object value, String color) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Times New Roman")
            .fontSize(9)
            .fillColor(color)
            .borderStyle(BorderStyle.THIN)
            .borderColor("111111")
            .set();
        return ++columnIndex;
    }

    public static int setHseCell(Worksheet sheet, int rowIndex, int columnIndex, Object value, String color, double width) {
        sheet.width(columnIndex, width);
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Times New Roman")
            .fontSize(9)
            .fillColor(color)
            .borderStyle(BorderStyle.THIN)
            .borderColor("111111")
            .set();
        return ++columnIndex;
    }


    /**
     * HSE CUSTOM  < < <
     */





    /**
     * columnIndex > zero based
     */
    public static int hseRed(Worksheet sheet, int rowIndex, int columnIndex, Object value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .horizontalAlignment("center")
            .wrapText(true)
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(11)
            .bold()
            .fillColor(RED)
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    public static int hseBlue(Worksheet sheet, int rowIndex, int columnIndex, Object value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .horizontalAlignment("center")
            .wrapText(true)
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(11)
            .bold()
            .fillColor("75bfe4")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * rowIndex > zero based
     * columnIndex > zero based
     */
    public static int setCellCream(Worksheet sheet, int rowIndex, int columnIndex, Object value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .fillColor("f0d686")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    /**
     * rowIndex > zero based
     * columnIndex > zero based
     */
    public static int setCellLightBlue(Worksheet sheet, int rowIndex, int columnIndex, Object value) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .fillColor("bee7fb")
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

    public static int setCell(Worksheet sheet, int rowIndex, int columnIndex, Object value, String color) {
        sheet.value(rowIndex, columnIndex, value == null ? "" : value.toString());
        sheet.style(rowIndex, columnIndex)
            .wrapText(true)
            .horizontalAlignment("center")
            .verticalAlignment("center")
            .fontName("Arial")
            .fontSize(10)
            .fillColor(color)
            .borderStyle(BorderStyle.THIN)
            .borderColor("777777")
            .set();
        return ++columnIndex;
    }

}
