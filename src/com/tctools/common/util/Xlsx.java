package com.tctools.common.util;


import com.tctools.common.Param;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.dhatim.fastexcel.*;
import org.dhatim.fastexcel.reader.*;
import org.slf4j.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;


public class Xlsx {

    private static final Logger log = LoggerFactory.getLogger(Xlsx.class);

    public static void create(String filename, String title, WriteEvents e) throws VantarException {
        create(null, filename, title, e);
    }

    public static void create(HttpServletResponse response, String filename, String title, WriteEvents e) throws VantarException {
        if (response != null) {
            Response.setDownloadHeaders(response, filename);
        }

        try (
            OutputStream outputStream = response == null ? new FileOutputStream(filename) : response.getOutputStream();
            Workbook workbook = new Workbook(outputStream, title, "1.0");
            Worksheet sheet = workbook.newWorksheet(title)
        ) {
            e.onCreateSheet(workbook, sheet);
        } catch (Exception x) {
            log.error(" ! create xlsx {}", filename, x);
            throw new ServerException(VantarKey.IO_ERROR);
        }
    }

    public static void create(Config config) throws VantarException {
        if (config.response != null) {
            Response.setDownloadHeaders(config.response, config.filename);
        }

        try (
            OutputStream outputStream = config.response == null ?
                new FileOutputStream(config.filename) : config.response.getOutputStream();
            Workbook workbook = new Workbook(outputStream, config.title, "1.0");
        ) {

            for (Map.Entry<String, WriteEvents> entry : config.writeSheets.entrySet()) {
                String title = entry.getKey();
                WriteEvents e = entry.getValue();
                Worksheet sheet = workbook.newWorksheet(title);
                e.onCreateSheet(workbook, sheet);
            }

        } catch (Exception x) {
            log.error(" ! create xlsx {}", config.filename, x);
            throw new ServerException(VantarKey.IO_ERROR);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static String read(Config config) throws VantarException {
        try (Params.Uploaded uploaded = config.params.upload(Param.FILE_UPLOAD)) {
            if (!uploaded.isUploadedOk()) {
                throw new InputException(uploaded.getError(), Param.FILE_UPLOAD);
            }
            if (!uploaded.isType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
                throw new InputException(VantarKey.FILE_TYPE, Param.FILE_UPLOAD, "xlsx");
            }

            String filename = StringUtil.replace(uploaded.getOriginalFilename(), ".xlsx", "-"
                + new DateTime().formatter().getDateTimePersianAsFilename() + ".xlsx");

            if (!uploaded.moveTo(config.uploadDir, filename)) {
                throw new ServerException(VantarKey.FAIL_UPLOAD);
            }

            try (InputStream inputStream = new FileInputStream(config.uploadDir + filename);
                 ReadableWorkbook workbook = new ReadableWorkbook(inputStream)) {

                for (Map.Entry<Integer, ReadEvents> entry : config.readSheets.entrySet()) {
                    Integer i = entry.getKey();
                    ReadEvents e = entry.getValue();

                    Sheet sheet;
                    try {
                        sheet = workbook.getSheet(i).get();
                    } catch (Exception sheetException) {
                        e.onError(workbook, sheetException, false, i);
                        return filename;
                    }

                    try (Stream<Row> rows = sheet.openStream()) {
                        rows.forEach(row -> {
                            if (!(row.getRowNum() > config.headerRowcount ?
                                e.onReadRow(workbook, row) :
                                e.onReadHeader(workbook, row))) {

                                // if returns false, interrupt
                                throw new EndRowReadException();
                            }
                        });
                    } catch (RuntimeException x) {
                        e.onError(workbook, x, true, i);
                    }
                }
            } catch (Exception e) {
                throw new ServerException(e);
            }
            return filename;
        }
    }


    public static class Config {

        public HttpServletResponse response;
        public Params params;
        public String title;
        public String uploadDir;
        public String filename;
        public int headerRowcount;
        public Map<String, WriteEvents> writeSheets = new LinkedHashMap<>(7, 1);
        public Map<Integer, ReadEvents> readSheets = new LinkedHashMap<>(7, 1);


        public Config() {

        }

        public Config(HttpServletResponse response, String title, String filename) {
            this.response = response;
            this.title = title;
            this.filename = filename;
        }

        public void addWriteEvent(String title, WriteEvents e) {
            writeSheets.put(title, e);
        }

        public void addReadEvent(int index, ReadEvents e) {
            readSheets.put(index, e);
        }
    }


    public interface WriteEvents {

        void onCreateSheet(Workbook workbook, Worksheet sheet) throws VantarException;
    }


    public interface ReadEvents {

        boolean onReadHeader(ReadableWorkbook workbook, Row row);

        boolean onReadRow(ReadableWorkbook workbook, Row row);

        void onError(ReadableWorkbook workbook, Exception e, boolean rowError, int sheetIndex);
    }


    public static class EndRowReadException extends RuntimeException {

    }
}
