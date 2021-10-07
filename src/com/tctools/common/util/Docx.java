package com.tctools.common.util;

import com.tctools.business.service.locale.AppLangKey;
import com.vantar.exception.*;
import com.vantar.util.file.FileUtil;
import com.vantar.util.json.Json;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.slf4j.*;
import java.io.*;
import java.util.Map;


public class Docx {

    private static final Logger log = LoggerFactory.getLogger(Docx.class);


    public static void createFromTemplateSimple(String templatePath, String filePath, Map<String, String> mapping) throws VantarException {
        WordprocessingMLPackage template;
        try {
            template = WordprocessingMLPackage.load(new FileInputStream(new File(templatePath)));
            VariablePrepare.prepare(template);
            template.getMainDocumentPart().variableReplace(mapping);
            template.getMainDocumentPart();
            template.save(new File(filePath));
        } catch (Exception e) {
            log.error("! {} < {}", filePath, mapping, e);
            throw new VantarException(AppLangKey.DOCX_CREATE_ERROR);
        }
    }

    public static void createFromTemplate(String templatePath, String filePath, String filename, Map<String, Object> mapping) throws VantarException {
        String jsonFilePath = filePath + filename + ".json";
        String command = "php -d memory_limit=512M " + Docx.class.getResource("/arta/app/docx/").getPath()
            + "replace.php "
            + templatePath
            + " " + filePath + filename
            + " " + jsonFilePath;

        FileUtil.giveAllPermissions(Docx.class.getResource("/arta/app/docx/").getPath());

        FileUtil.makeDirectory(filePath);
        FileUtil.write(jsonFilePath, Json.toJson(mapping));

        try {
            BufferedReader input = new BufferedReader(
                new InputStreamReader(
                    Runtime.getRuntime()
                        .exec(command)
                        .getErrorStream()
                )
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                output.append(line).append("\n");
            }
            input.close();
            log.info("command={}\n\noutput={}\n\n", command, output);

            if (output.length() > 0) {
                throw new ServerException(output.toString());
            }

        } catch (Exception e) {
            log.error("! {} < {}", filePath + filename, mapping, e);
            throw new VantarException(AppLangKey.DOCX_CREATE_ERROR);
        } finally {
            FileUtil.removeFile(jsonFilePath);
        }
    }


    public static class Picture {

        public String path;
        public int width;
        public int height;

        public Picture(String path, int height, int width) {
            this.path = path;
            this.width = width;
            this.height = height;
        }
    }
}
