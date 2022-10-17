package com.tctools.common.util;

import com.tctools.common.Param;
import com.vantar.service.log.LogEvent;
import org.apache.commons.io.FileUtils;
import java.io.*;


public class TempDir {

    public static void delete() {
        try {
            FileUtils.cleanDirectory(new File(Param.TEMP_DIR));
        } catch (IOException e) {
            LogEvent.error(TempDir.class, e);
        }
    }
}
