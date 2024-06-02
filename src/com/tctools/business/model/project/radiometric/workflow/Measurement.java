package com.tctools.business.model.project.radiometric.workflow;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.*;
import com.vantar.database.common.ValidationError;
import com.vantar.database.datatype.Location;
import com.vantar.exception.*;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.*;
import java.io.*;
import java.util.*;


public class Measurement {

    private static final Logger log = LoggerFactory.getLogger(Measurement.class);
    private static final int ROUND_TO_DECIMALS = 3;
    private static final int MEASUREMENT_COUNT = 360;
    private static final int MEASUREMENT_MAX_HOUR = 14;
    private static final int MIN_RADIATION_LEVEL = 440;
    public static final double MIN_RADIATION_LEVEL_DIV = 4.4;

    boolean isMwCm2 = false;


    public void applyCsv(String csvPath, RadioMetricFlow flow, String height, List<ValidationError> errors) {
        boolean isMeasurementTimeAcceptable = true;
        boolean isMeasurementGpsDataAvailable = true;
        String[] record = null;
        int i = 0;
        int measurementCount = 0;
        double avg = 0;
        double densityMin = -110;
        double densityMax = -110;
        double sumValue2 = 0;
        DateTime logDateTime = null;
        Location location = new Location();

        DateTime startDateTime = null;
        DateTime endDateTime = null;

        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            while ((record = reader.readNext()) != null) {
                ++i;
                switch (i) {
                    // SMP reader version 3.5.0,,,,,,,,,
                    case 1:
                        flow.deviceModel = StringUtil.split(record[0], ' ')[0];
                        break;
                    // SMP serial = 14SM0335 FW =SMP 3.6.2,,,,,,,,,
                    case 2:
                        flow.deviceSerialNumber = StringUtil.split(StringUtil.split(record[0], "=")[1].trim(), ' ')[0];
                        break;
                    // Probe = 17WP090275,,,,,,,,,
                    case 3:
                        flow.deviceProbe = StringUtil.split(record[0], '=')[1].trim();
                        break;
                    // Frequencies = ,,,,,,,,,
                    case 4:
                        break;
                    // Field = Electric,,,,,,,,,
                    case 5:
                        break;
                    // Measurement type = IS,,,,,,,,,
                    case 6:
                        break;
                    // Log interval in seconds = 1,,,,,,,,,
                    case 7:
                        break;
                    // Average interval in seconds = 360,,,,,,,,,
                    case 8:
                        break;
                    // #,Date,Time,ValueV/m,AverageV/m,Latitude,Longitude,Height,
                    case 9:
                        isMwCm2 = record[3].contains("uW/cm2");
                        break;
                    // RECORDS FOR: #,Date,Time,ValueV/m,AverageV/m,Latitude,Longitude,Height,,
                    default:
                        if (record.length != 8 && record.length != 5 && record.length != 6) {
                            errors.add(new ValidationError(Param.FILE_UPLOAD + height, AppLangKey.INVALID_MEASURE_CSV_DATA));
                            continue;
                        }
                        boolean gpsDataMissing = record.length == 5 || record.length == 6;
                        if (record.length == 5 || record.length == 6) {
                            isMeasurementGpsDataAvailable = false;
                        }

                        Double avgTemp = StringUtil.toDouble(record[4]);
                        Double value = StringUtil.toDouble(record[3]);
                        if (value == null) {
                            continue;
                        }
                        if (avgTemp > 0) {
                            avg = avgTemp;
                        }

                        if (height.equals("150")) {
                            if (!gpsDataMissing && (i < 10 || location.isEmpty() || !location.isValid())) {
                                location.latitude = StringUtil.toDouble(record[5]);
                                location.longitude = StringUtil.toDouble(record[6]);
                            }
                        }

                        try {
                            endDateTime = new DateTime(record[1] + " " + record[2]);
                            if (endDateTime.formatter().hour <= 0 || endDateTime.formatter().hour > MEASUREMENT_MAX_HOUR) {
                                isMeasurementTimeAcceptable = false;
                            }

                            if (i >= 10 && logDateTime == null) {
                                logDateTime = endDateTime;
                            }
                            if (startDateTime == null) {
                                startDateTime = endDateTime;
                            }

                        } catch (DateTimeException e) {
                            log.error("!invalid date ({}, {})", record[1], record[2], e);
                            continue;
                        }

                        if (value > 0) {
                            densityMin = densityMin == -110 ? value : Math.min(value, densityMin);
                            densityMax = densityMax == -110 ? value : Math.max(value, densityMax);
                        }

                        sumValue2 += Math.pow(value, 2);
                        ++measurementCount;
                }
            }
        } catch (IOException | CsvValidationException | ArrayIndexOutOfBoundsException e) {
            log.error("! {}", CollectionUtil.join(record, ", "), e);
            errors.add(new ValidationError(Param.FILE_UPLOAD + height, AppLangKey.INVALID_MEASURE_CSV_DATA));
        }

        if (!location.isEmpty()) {
            flow.spotLocation = location;
        } else if (!location.isValid()) {
            isMeasurementGpsDataAvailable = false;
        }

        flow.setPropertyValue("isMeasurementTimeAcceptable" + height, isMeasurementTimeAcceptable);
        flow.setPropertyValue("isMeasurementGpsDataAvailable" + height, isMeasurementGpsDataAvailable);
        flow.setPropertyValue("isMeasurementRecordCountAcceptable" + height, measurementCount == MEASUREMENT_COUNT);

        double calculatedAvg;
        if (!isMwCm2) {
            densityMin = vmToWcm2(densityMin);
            densityMax = vmToWcm2(densityMax);
            avg = vmToWcm2(avg);
            calculatedAvg = measurementCount == 0 ?
                0 :
                ModelUtil.round(((sumValue2 / measurementCount) / 377) * 100, ROUND_TO_DECIMALS);
        } else {
            calculatedAvg = ModelUtil.round(avg, ROUND_TO_DECIMALS);
        }

        double densityAverageDivMinRadiation = ModelUtil.round(avg / MIN_RADIATION_LEVEL_DIV, ROUND_TO_DECIMALS);

        flow.setPropertyValue("isMwCm2" + height, isMwCm2);
        flow.setPropertyValue("densityMin" + height, densityMin);
        flow.setPropertyValue("densityMax" + height, densityMax);
        flow.setPropertyValue("densityAverage6min" + height, calculatedAvg);
        flow.setPropertyValue("densityAverageDevice6min" + height, avg);
        flow.setPropertyValue("minRadiationLevel" + height, MIN_RADIATION_LEVEL);
        flow.setPropertyValue("densityAverageDivMinRadiation" + height, densityAverageDivMinRadiation);
        flow.setPropertyValue("logDateTime" + height, logDateTime);
        flow.setPropertyValue("radiationStatus" + height, RadioMetricRadiationStatus.Compatible);

        // > > > validation
        Set<String> msg = new HashSet<>(10);
        if (flow.validationMessage != null) {
            Set<String> msgs = StringUtil.splitToSet(flow.validationMessage, '\n');
            if (msgs != null) {
                for (String m : msgs) {
                    if (!m.startsWith(height + ":")) {
                        msg.add(m);
                    }
                }
            }
        }

        long mins = endDateTime == null || startDateTime == null ? 0 : endDateTime.diffSeconds(startDateTime) / 60;
        if (mins < 3) {
            msg.add(height + ": too quick (" + mins + "minutes)");
        } else if (mins > 6) {
            msg.add(height + ": too slow (" + mins + "minutes)");
        }

        // compare with each-other
        if (flow.logDateTime100 != null && flow.logDateTime150 != null && flow.logDateTime170 != null) {
            long minA = flow.logDateTime100.diffSeconds(flow.logDateTime150) / 60;
            long minB = flow.logDateTime100.diffSeconds(flow.logDateTime170) / 60;
            long minC = flow.logDateTime170.diffSeconds(flow.logDateTime150) / 60;
            if (minA >= 10) {
                msg.add("100~150: (" + minA + "minutes)");
            }
            if (minB >= 10) {
                msg.add("100~170: (" + minB + "minutes)");
            }
            if (minC >= 10) {
                msg.add("150~170:  (" + minC + "minutes)");
            }
        }

        flow.validationMessage = CollectionUtil.join(msg, "\n");
        // validation < < <
    }

    public void createOkExcel(String csvPath) throws ServerException {
        String okTempFilename = StringUtil.replace(csvPath, ".csv", "__OK.temp");
        String okFilename = StringUtil.replace(csvPath, ".csv", "__OK.xlsx");

        FileUtil.removeFile(okTempFilename);
        FileUtil.removeFile(okFilename);

        if (isMwCm2) {
            return;
        }

        try {
            Excel.csvToExcel(csvPath, okTempFilename);
        } catch (IOException e) {
            log.error("! {}", csvPath, e);
            throw new ServerException(e);
        }

        try (
            XSSFWorkbook workbook = new XSSFWorkbook(okTempFilename);
            FileOutputStream out = new FileOutputStream(new File(okFilename))
        ) {

            int r = 9;
            XSSFSheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(r);
            Cell cell = row.createCell(8);
            cell.setCellValue("E²");

            for (r = 9 ; r < 369 ; ++r) {
                row = sheet.getRow(r);
                if (row != null) {
                    cell = row.createCell(8);
                    cell.setCellFormula("D" + (r + 1) + "*D" + (r + 1));
                }
            }

            row = sheet.createRow(++r);
            cell = row.createCell(0);
            cell.setCellValue("E² Average");
            cell = row.createCell(1);
            cell.setCellFormula("AVERAGE(I10:I370)");

            row = sheet.createRow(++r);
            cell = row.createCell(0);
            cell.setCellValue("E Average");
            cell = row.createCell(1);
            cell.setCellFormula("SQRT(B371)");

            row = sheet.createRow(++r);
            cell = row.createCell(0);
            cell.setCellValue("S Average [W/m²]");
            cell = row.createCell(1);
            cell.setCellFormula("B371/377");

            row = sheet.createRow(++r);
            cell = row.createCell(0);
            cell.setCellValue("S Average [uW/cm²]");
            cell = row.createCell(1);
            cell.setCellFormula("100*B373");

            row = sheet.createRow(++r);
            cell = row.createCell(0);
            cell.setCellValue("Rounded S Average [uW/cm²]");
            cell = row.createCell(1);
            cell.setCellFormula("ROUND(B374,3)");

            row = sheet.createRow(++r);
            cell = row.createCell(0);
            cell.setCellValue("Rounded S Average [uW/cm²]/4.4");
            cell = row.createCell(1);
            cell.setCellFormula("ROUND(B375/4.4,3)");


            //FileUtil.removeFile(okTempFilename);
//log.error(">>>>>>>>{}",csvPath);
            workbook.write(out);
        } catch (Exception e) {
            log.error("! {}", csvPath, e);
            throw new ServerException(e);
        } finally {
            FileUtil.removeFile(okTempFilename);
        }
    }

    private static double vmToWcm2(double value) {
        return ModelUtil.round(Math.pow(value, 2) / 377 * 100, ROUND_TO_DECIMALS);
    }
}
