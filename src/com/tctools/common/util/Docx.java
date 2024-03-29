package com.tctools.common.util;

import com.tctools.business.service.locale.AppLangKey;
import com.vantar.exception.*;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.file.*;
import com.vantar.util.json.Json;
import org.slf4j.*;
import java.io.*;
import java.util.Map;


public class Docx {

    private static final Logger log = LoggerFactory.getLogger(Docx.class);

    public static void createFromTemplate(String templatePath, String filePath, String filename, Map<String, Object> mapping) throws VantarException {
        //String jsonFilePath = StringUtil.replace(filePath + filename + ".json", " ", "_");
        String jsonFilePath = filePath + "mapping.json";
        String[] command = new String[] {
            "php", "-d", "memory_limit=512M",
                Docx.class.getResource("/arta/app/docx/").getPath() + "replace.php",
                templatePath,
                filePath + filename,
                jsonFilePath
        };

        DirUtil.giveAllPermissions(Docx.class.getResource("/arta/app/docx/").getPath());

        DirUtil.makeDirectory(filePath);

        FileUtil.write(jsonFilePath, Json.d.toJson(mapping));

        try (
            BufferedReader input = new BufferedReader(
                new InputStreamReader(
                    Runtime.getRuntime()
                        .exec(command)
                        .getErrorStream()
                )
            )
        ) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                output.append(line).append("\n");
            }
            log.info("command={}\n\noutput={}\n\n", CollectionUtil.join(command, ' '), output);

            if (output.length() > 0) {
                throw new ServerException("data:" + output.toString());
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
