package com.tctools.business.admin.model;


import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.common.Param;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonRepoMongo;
import com.vantar.exception.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;


public class AdminTools {

    public static void index(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi("Tools", params, response);
        if (ui == null) {
            return;
        }

        ui  .beginBox("Radio Metric")
            .addBlockLink("CHANGE/ADD STATE", "/admin/tools/state/add")
            .addBlockLink("UPDATE STATE", "/admin/tools/state/update")
            .addLine()
            .addBlockLink("UPDATE all records", "/admin/tools/update/all")
            .addLine()
            .addBlockLink("Docx templates", "/admin/tools/radiometric/templates")
            .addLine()
            .addBlockLink("Upload signature", "/admin/tools/radiometric/signature")
            .containerEnd();

        ui.finish();
    }

    public static void addState(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi("Add state", params, response);
        if (ui == null) {
            return;
        }

        String s = params.getString("codes");
        String st = params.getString("state");
        DateTime dt = params.getDateTime("dt");

        boolean cc = params.isChecked("cc");
        boolean regular = params.isChecked("regular");
        boolean synch = params.isChecked("synch");

        boolean tr = params.isChecked("trend");

        String comment = params.getString("comment");
        Long assignor = params.getLong("assignor");
        Long assignee = params.getLong("assignee");

        ui.beginFormGet();
        ui.addTextArea("code sites", "codes", s);
        ui.addTextArea("state", "state", st);
        ui.addInput("date time", "dt", dt);
        ui.addEmptyLine();
        ui.addCheckbox("update cc", "cc", cc);
        ui.addCheckbox("update regular", "regular", regular);
        ui.addCheckbox("synch state", "synch", synch);
        ui.addEmptyLine();
        ui.addCheckbox("add trend", "trend", tr);
        ui.addInput("trend comment", "comment", comment);
        ui.addInput("assignor user id", "assignor", assignor);
        ui.addInput("assignee user id", "assignee", assignee);

        ui.addSubmit();
        ui.containerEnd().write();

        if (s == null) {
            return;
        }

        Set<String> codeSites = StringUtil.splitToSet(
            StringUtil.replace(
                StringUtil.replace(
                    StringUtil.remove(s, ' ', '\r'),
                    ',',
                    '\n'
                ),
                "\n\n",
                "\n"
            ),
            '\n'
        );
        RadioMetricFlowState state = st == null ? null : RadioMetricFlowState.valueOf(st);

        try {
            List<RadioMetricFlow> items = CommonRepoMongo.getData(new RadioMetricFlow());

            for (RadioMetricFlow f: items) {
                if (!codeSites.contains(f.site.code)) {
                    continue;
                }
                if (cc && !f.isCc) {
                    continue;
                }
                if (regular && f.isCc) {
                    continue;
                }

                if (synch) {
                    f.lastState = f.state.get(f.state.size() - 1).state;

                } else if (state != null) {
                    f.lastState = state;
                    if (dt != null) {
                        f.lastStateDateTime = dt;

                        if (state.equals(RadioMetricFlowState.Planned)) {
                            f.assignDateTime = dt;
                        }
                        if (state.equals(RadioMetricFlowState.Completed)) {
                            f.measurementDateTime = dt;
                        }
                    }

                    if (tr) {
                        State stateTrend = new State();
                        stateTrend.state = state;
                        stateTrend.dateTime = dt;
                        stateTrend.comments = comment;
                        stateTrend.assigneeId = assignee;
                        stateTrend.assignorId = assignor;
                        f.state.add(stateTrend);
                    }
                }

                CommonRepoMongo.update(f);
                ui.addMessage(f.site.code).write();
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }

    public static void updateState(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi("Update state", params, response);
        if (ui == null) {
            return;
        }

        String s = params.getString("codes");
        String st = params.getString("state");
        DateTime dt = params.getDateTime("dt");

        String comment = params.getString("comment");
        Long assignor = params.getLong("assignor");
        Long assignee = params.getLong("assignee");

        ui.beginFormGet();
        ui.addTextArea("code sites", "codes", s);
        ui.addTextArea("state", "state", st);
        ui.addInput("date time", "dt", dt);

        ui.addInput("trend comment", "comment", comment);
        ui.addInput("assignor user id", "assignor", assignor);
        ui.addInput("assignee user id", "assignee", assignee);

        ui.addSubmit();
        ui.containerEnd().write();

        if (s == null) {
            return;
        }

        Set<String> codeSites = StringUtil.splitToSet(
            StringUtil.replace(
                StringUtil.replace(
                    StringUtil.remove(s, ' ', '\r'),
                    ',',
                    '\n'
                ),
                "\n\n",
                "\n"
            ),
            '\n'
        );
        RadioMetricFlowState state = RadioMetricFlowState.valueOf(st);

        try {
            List<RadioMetricFlow> items = CommonRepoMongo.getData(new RadioMetricFlow());

            for (RadioMetricFlow f: items) {
                if (!codeSites.contains(f.site.code)) {
                    continue;
                }

                if (dt != null) {
                    if (state.equals(f.lastState)) {
                        f.lastStateDateTime = dt;
                    }
                    if (state.equals(RadioMetricFlowState.Planned)) {
                        f.assignDateTime = dt;
                    }
                    if (state.equals(RadioMetricFlowState.Completed)) {
                        f.measurementDateTime = dt;
                    }
                }

                for (State stateX : f.state) {
                    if (state.equals(stateX.state)) {
                        if (dt != null) {
                            stateX.dateTime = dt;
                        }
                        if (comment != null) {
                            stateX.comments = comment;
                        }
                        if (assignee != null) {
                            stateX.assigneeId = assignee;
                        }
                        if (assignor != null) {
                            stateX.assignorId = assignor;
                        }
                    }
                }

                CommonRepoMongo.update(f);
                ui.addMessage(f.site.code).write();
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }

    public static void updateAll(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi("Update all", params, response);
        if (ui == null) {
            return;
        }
        try {
            List<RadioMetricFlow> items = CommonRepoMongo.getData(new RadioMetricFlow());

            for (RadioMetricFlow f: items) {
                CommonRepoMongo.update(f);
                ui.addMessage(f.site.code).write();
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }

    public static void radiometricTemplates(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi("Radiometric templates", params, response);
        if (ui == null) {
            return;
        }

        Params.Uploaded file = params.upload("file");
        if (file != null && file.isUploaded() && !file.isIoError()) {
            file.moveTo("/opt/tc-tools/templates/radiometric/site-radiometric.docx");
            ui.addMessage("uploaded successfully").write();
            return;
        }

        ui.addLink("site-radiometric.docx", "/admin/tools/radiometric/templates/docx");
        ui.beginUploadForm();
        ui.addFile("New template", "file");
        ui.addSubmit();
        ui.write();
    }

    public static void signature(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi("User signature", params, response);
        if (ui == null) {
            return;
        }

        Params.Uploaded file = params.upload("file");
        if (file != null && file.isUploaded() && !file.isIoError()) {
            file.moveTo("/opt/tc-tools/files/user/" + file.getOriginalFilename());
            ui.addMessage("uploaded successfully");
        }

        ui.beginUploadForm();
        ui.addMessage("Filename example: 49-signature.jpg");
        ui.addFile("File", "file");
        ui.addSubmit();

        drawSignatures(ui);

        ui.write();
    }

    private static void drawSignatures(WebUi ui) {
        try {
            Files.list(Paths.get("/opt/tc-tools/files/user"))
                .filter(Files::isRegularFile)
                .forEach((file) -> {
                    String fileUrl = Param.USERS_URL + file.getFileName();
                    String filePath = "/opt/tc-tools/files/user/" + file.getFileName();
                    ui.addBlockLink(
                        fileUrl + "   (" + FileUtil.getSizeMb(filePath) + "MB)",
                        fileUrl
                    );
                });
        } catch (IOException ignore) {

        }
    }
}