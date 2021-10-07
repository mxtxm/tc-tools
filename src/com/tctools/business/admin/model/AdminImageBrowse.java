package com.tctools.business.admin.model;

import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.Admin;
import com.vantar.locale.Locale;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.*;


public class AdminImageBrowse {

    public static void index(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(AppLangKey.ADMIN_IMAGE_BROWSE), params, response);
        if (ui == null) {
            return;
        }

        try {
            drawStructure(ui, "/static/", "/opt/tc-tools/files/");
        } catch (IOException e) {
            ui.addErrorMessage(e);
        }

        ui.finish();
    }

    private static void drawStructure(WebUi ui, String path, String dir) throws IOException {
        ui.beginTree(path);

        Files.list(Paths.get(dir))
            .filter(Files::isDirectory)
            .forEach((file) -> {
                try {
                    drawStructure(ui, path + file.getFileName().toString() + "/", file.toAbsolutePath().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        Files.list(Paths.get(dir))
            .filter(Files::isRegularFile)
            .forEach((file) -> {
                String fileUrl = path + file.getFileName();
                String filePath = dir + '/' + file.getFileName();
                DateTime lastModify = FileUtil.getLastModify(filePath);
                ui.addBlockLink(
                    fileUrl + "   (" + FileUtil.getSizeMb(filePath) + "MB"
                        + (lastModify == null ? "" : " - " + lastModify.toString()) + ")",
                    fileUrl
                );
            });

        ui.containerEnd();
    }
}
