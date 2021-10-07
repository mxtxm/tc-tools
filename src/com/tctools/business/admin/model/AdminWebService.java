package com.tctools.business.admin.model;

import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.Admin;
import com.vantar.database.dto.*;
import com.vantar.http.Url;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.file.FileUtil;
import com.vantar.util.object.ObjectUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class AdminWebService {

    public static void index(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_MENU_DOCUMENTS), params, response);
        if (ui == null) {
            return;
        }

        ui.addBlockLink("LOG", "/admin/webservice/log");

        drawStructure(ui, "Webservices", "document/webservice", FileUtil.getResourceStructure("/document/webservice"));
        ui.finish();
    }

    private static void drawStructure(WebUi ui, String path, String dir, Map<String, Object> paths) {
        ui.beginTree(path);

        List<String> files = (List<String>) paths.get("/");
        if (files != null) {
            for (String file : files) {
                String title = StringUtil.remove(file, ".md");
                ui.addBlockLink(title, "/admin/webservice/show?document=" + dir + "--" + file + "&title=" + title);
            }
            paths.remove("/");
        }

        paths.forEach((innerPath, structure) -> {
            if (innerPath.equals("common")) {
                return;
            }
            drawStructure(ui, innerPath, dir + "--" + innerPath, (Map<String, Object>) structure);
        });

        ui.containerEnd();
    }

    public static void show(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(AppLangKey.ADMIN_WEBSERVICES), params, response);
        if (ui == null) {
            return;
        }

        String documentPath = params.getString("document");
        String title = params.getString("title");
        String content = FileUtil.getFileContentFromClassPath("/" + StringUtil.replace(documentPath, "--", "/"));

        ui.addHeading(title).addEmptyLine().addEmptyLine();

        for (String line : StringUtil.split(content, '\n')) {
            if (line.startsWith("## ")) {
                title = StringUtil.remove(line, "#");
                ui.addBlockLink(
                    title,
                    "/admin/webservice/form?document=" + documentPath + "&section=" + Url.encode(line) + "&title=" + Url.encode(title)
                );
            }
        }

        ui.finish();
    }

    public static void form(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(AppLangKey.ADMIN_WEBSERVICES), params, response);
        if (ui == null) {
            return;
        }

        ui  .beginContainer("error-container", null)
            .containerEnd()
            .setJs("/js/jquery.min.js")
            .setJs("/js/webservice.js");

        String documentPath = params.getString("document");
        String section = params.getString("section");
        Map<String, WebserviceDef> getWebserviceGroup = getWebserviceGroup(documentPath);
        WebserviceDef webserviceDef = getWebserviceGroup.get(section);

        ui.addHeading(section);

        if ("insert".equals(webserviceDef.getAction())) {
            createForm(ui, getWebserviceGroup.get(section));
        } else if ("update".equals(webserviceDef.getAction())) {

        } else if ("delete".equals(webserviceDef.getAction())) {

        } else if ("search".equals(webserviceDef.getAction())) {
            showSearchData(ui, webserviceDef);

        } else if ("get".equals(webserviceDef.getAction())) {
            showGetData(ui, getWebserviceGroup.get(section));

        } else if ("keyval".equals(webserviceDef.getAction())) {
            showGetData(ui, getWebserviceGroup.get(section));

        } else if ("assign".equals(webserviceDef.getAction())) {

        } else if ("remove".equals(webserviceDef.getAction())) {

        } else {
            showGetData(ui, getWebserviceGroup.get(section));
        }

        ui.finish();
    }

    private static void showSearchData(WebUi ui, WebserviceDef webserviceDef) {
        Dto dto;
        try {
            dto = DtoDictionary.get(StringUtil.remove(webserviceDef.fields.get(0).type, '{', ')')).getDtoInstance();
        } catch (Exception e) {
            //ui.addErrorMessage(e).finish();
            //return;
        }

        ui  .beginFormPost()
            .addInput("Authentication token", "auth-token")
            .addSelect("Language", "lang", getLangs(), "en")
            .addEmptyLine()
            .addHidden("url", webserviceDef.url)
            .addInput("keyword", "keyword")
            .addInput("page no.", "page", 1)
            .addInput("records", "length", 20)
            .addInput("sort", "sort", "id:desc")
            .addButton("Apply", "apply")
            .containerEnd();


        ui.beginContainer("result", null);
        ui.setJs("/js/search.js");

    }

    private static void showGetData(WebUi ui, WebserviceDef webserviceDef) {
        if (webserviceDef.fields.isEmpty()) {
            ui  .addHidden("auto-get", "1")
                .beginFormPost()
                .addInput("Authentication token", "auth-token")
                .addSelect("Language", "lang", getLangs(), "en")
                .addEmptyLine()
                .addHidden("url", webserviceDef.url)
                .addButton("Apply", "apply")
                .containerEnd();

        } else {
            createForm(ui, webserviceDef);
        }
        ui.beginContainer("result", null);
        ui.setJs("/js/get-data.js");
    }

    private static void createForm(WebUi ui, WebserviceDef webserviceDef) {
        ui  .beginFormPost()
            .addInput("Authentication token", "auth-token")
            .addSelect("Language", "lang", getLangs(), "en")
            .addEmptyLine()
            .addHidden("url", webserviceDef.url)
            .beginContainer("webservice-data", null);

        for (FieldDef field : webserviceDef.fields) {
            if (field.name.contains("createTime") || field.name.contains("updateTime")) {
                continue;
            }

            if (field.className != null) {
                Dto dto = DtoDictionary.get(field.className).getDtoInstance();
                ServiceDtoCache cache = Services.get(ServiceDtoCache.class);
                ui.addSelect(field.className, field.name, cache.getList(dto.getClass()));
                continue;
            }


            if (   field.type.equalsIgnoreCase("string")
                || field.type.equalsIgnoreCase("integer")
                || field.type.equalsIgnoreCase("long")
                || field.type.equalsIgnoreCase("double")
                || field.type.equalsIgnoreCase("float")
                || field.type.equalsIgnoreCase("character")
                || field.type.equalsIgnoreCase("datetime")
            ) {
                ui.addInput(field.name, field.name);
                continue;
            }

            if (field.type.equalsIgnoreCase("boolean")) {
                ui.addCheckbox(field.name, field.name);
                continue;
            }

            if (field.type.equalsIgnoreCase("upload")) {
                ui.addFile(field.name, field.name);
            }
        }

        ui  .containerEnd()
            .addButton("Apply", "apply");
    }

    private static Map<String, WebserviceDef> getWebserviceGroup(String documentPath) {
        Map<String, WebserviceDef> webserviceDefs = new HashMap<>();
        String content = FileUtil.getFileContentFromClassPath("/" + StringUtil.replace(documentPath, "--", "/"));

        String subSection = "";
        WebserviceDef def = null;
        String section = null;
        for (String line : StringUtil.split(content, '\n')) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("## ")) {
                if (def != null) {
                    webserviceDefs.put(section, def);
                }
                section = line;
                def = new WebserviceDef();
                continue;
            }

            if (line.equals("### url ###")) {
                subSection = "url";
                continue;
            }
            if (line.equals("### method ###")) {
                subSection = "method";
                continue;
            }
            if (line.equals("### headers ###")) {
                subSection = "headers";
                continue;
            }
            if (line.equals("### params ###")) {
                subSection = "params";
                continue;
            }

            switch (subSection) {
                case "url":
                    def.url = StringUtil.remove(line, '"');
                    subSection = "";
                    break;
                case "method":
                    def.method = line;
                    subSection = "";
                    break;
                case "headers":
                    def.headers.add(StringUtil.remove(StringUtil.split(line, ":")[0], "*").trim());
                    break;
                case "params":
                    if (line.startsWith("* ")) {
                        String[] typeNameDescription = StringUtil.split(StringUtil.remove(line, "* "), ":");
                        String[] typeName = StringUtil.split(typeNameDescription[0], ' ');
                        FieldDef fieldDef = new FieldDef();
                        fieldDef.required = StringUtil.contains(typeName[1], "**");
                        fieldDef.name = StringUtil.remove(typeName[1], '*').trim();
                        fieldDef.type = StringUtil.remove(typeName[0], '*').trim();

                        if (typeNameDescription.length > 1) {
                            if (typeNameDescription[1].contains("{") && typeNameDescription[1].contains("}")) {
                                fieldDef.className = StringUtil.split(StringUtil.split(typeNameDescription[1], '{')[1], '}')[0];
                            }
                        }

                        def.fields.add(fieldDef);
                    }
                    break;
            }

            if (line.startsWith("### ")) {
                subSection = "";
            }
        }
        webserviceDefs.put(section, def);

        return webserviceDefs;
    }

    private static Map<String, String> getLangs() {
        Map<String, String> lang = new HashMap<>();
        lang.put("en", "English");
        lang.put("fa", "Persian");
        return lang;
    }

    private static class WebserviceDef {

        public String url = null;
        public String method = null;
        public List<String> headers = new ArrayList<>();
        public List<FieldDef> fields = new ArrayList<>();

        public String toString() {
            return ObjectUtil.toString(this);
        }

        public String getAction() {
            for (String action : new String[] { "insert", "update", "delete", "search", "get", "assign", "remove", "keyval" }) {
                if (url.contains(action)) {
                    return action;
                }
            }
            return null;
        }
    }


    private static class FieldDef {

        public String name;
        public String type;
        public boolean required;
        public String className;

        public String toString() {
            return ObjectUtil.toString(this);
        }
    }
}
