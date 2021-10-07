package com.tctools.common.util;


import com.vantar.util.string.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.*;
import org.apache.poi.xssf.usermodel.*;
import java.io.*;
import java.nio.file.*;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;


public class Excel {

    public static void excelToCSV(String xlsxPath, String csvPath, int sheetIdx) throws IOException {
        excelToCSV(xlsxPath, csvPath, sheetIdx, null);
    }

    public static void excelToCSV(String xlsxPath, String csvPath, int sheetIdx, Integer colCount) throws IOException {
        FileInputStream fileInStream = new FileInputStream(new File(xlsxPath));
        FileWriter fw = new FileWriter(csvPath);

        XSSFWorkbook workBook = new XSSFWorkbook(fileInStream);
        XSSFSheet selSheet = workBook.getSheetAt(sheetIdx);
        for (Row row : selSheet) {
            int next = 1;
            StringBuilder sb = new StringBuilder();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int current = cell.getColumnIndex();

                if (current >= next) {
                    for (int k = 0, loop = current - next + 1; k < loop; ++k, ++next) {
                        sb.append(',');
                    }
                }
                ++next;

                switch (cell.getCellType()) {
                    case STRING:
                        sb.append('"').append(StringUtil.replace(cell.getStringCellValue(), '"', '\'')).append('"');
                        break;
                    case NUMERIC:
                        sb.append(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        sb.append(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        sb.append(cell.getCellFormula());
                        break;
                    default:
                }
                sb.append(",");
            }
            sb.setLength(sb.length() - 1);
            for (int k = next; k <= colCount; ++k) {
                sb.append(",");
            }
            sb.append("\n");
            fw.write(sb.toString());
        }
        workBook.close();
        fw.close();
    }

    public static void csvToExcel(String csvLocation, String xlsLocation) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("Sheet");
        AtomicReference<Integer> row = new AtomicReference<>(0);
        Files.readAllLines(Paths.get(csvLocation)).forEach(line -> {
            Row currentRow = sheet.createRow(row.getAndSet(row.get() + 1));
            String[] nextLine = line.split(",");
            Stream.iterate(0, i -> i + 1).limit(nextLine.length)
                .forEach(i -> currentRow.createCell(i).setCellValue(nextLine[i]));
        });
        FileOutputStream fos = new FileOutputStream(new File(xlsLocation));
        workbook.write(fos);
        fos.flush();
    }
}