package com.tctools.web.patch;

import com.vantar.util.xlsx.Xlsx;

public class XlsxStyleX {

    public static final String NAK_ORANGE = "FF7700";
    public static final String TYPE_HEADER = "80C4FF";
    public final static String LIGHT_GREY = "D9D9D9";
    public final static String BLUE = "75BFE4";
    public static final String YELLOW = "FFFF00";
    public static final String RED = "FA0300";
    public static final String CREAM = "F0D686";
    public static final String LIGHT_BLUE = "BEE7FB";
    public final static String DARK_PINK = "FFC49C";
    public static final String WHITE = "FFFFFF";
    public static final String GREEN = "80FF00";


    public static void setDefault(Xlsx.Context context) {
        context.style.horizontalAlignment = "center";
        context.style.verticalAlignment = "center";
        context.style.fontName = "Arial";
        context.style.fontSize = 10;
        context.style.bold = false;
        context.style.color = "#333333";
        context.style.bgColor = "#ffffff";
        context.style.borderColor = "777777";
    }

    public static void setHeader(Xlsx.Context context) {
        context.colIndex = 0;
        context.rowIndex = 0;
        setDefault(context);
        context.style.bgColor = NAK_ORANGE;
        context.style.bold = true;
    }

    public static void setHeaderType(Xlsx.Context context) {
        context.colIndex = 0;
        context.rowIndex = 1;
        setDefault(context);
        context.style.bgColor = TYPE_HEADER;
        context.style.bold = true;
    }

    public static void setNormalCenter(Xlsx.Context context) {
        setDefault(context);
        context.style.bgColor = WHITE;
    }

    public static void setNormalCenterBold(Xlsx.Context context) {
        setDefault(context);
        context.style.bgColor = WHITE;
        context.style.bold = true;
    }

    public static void setNormalLeft(Xlsx.Context context) {
        setDefault(context);
        context.style.bgColor = WHITE;
        context.style.horizontalAlignment = "left";
    }

    public static void setNormalRight(Xlsx.Context context) {
        setDefault(context);
        context.style.bgColor = WHITE;
        context.style.horizontalAlignment = "right"
        ;
    }

    public static void setHeaderImportResult(Xlsx.Context context) {
        context.colIndex = 0;
        context.rowIndex = 0;
        setDefault(context);
        context.style.bgColor = NAK_ORANGE;
        context.style.bold = true;
        context.style.fontSize = 8;
        context.sheet.fitToWidth((short) 1);
        context.sheet.freezePane(2, 1);
        context.sheet.width(0, 100);
        context.sheet.rowHeight(0, 17 * 3);
    }

    public static void setImportResult(Xlsx.Context context, String color) {
        setDefault(context);
        context.style.bgColor = color;
        context.style.horizontalAlignment = "left";
        context.style.fontSize = 9;
    }

}
