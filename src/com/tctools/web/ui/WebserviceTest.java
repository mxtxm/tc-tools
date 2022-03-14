package com.tctools.web.ui;

import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import org.slf4j.*;


public class WebserviceTest {

    public static final Logger log = LoggerFactory.getLogger(WebserviceTest.class);


    public void start() {
        for (String line : StringUtil.split(FileUtil.getFileContentFromClassPath("/test/webservices"), "\n")) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            String content = FileUtil.getFileContentFromClassPath(line);
        }

    }
}
