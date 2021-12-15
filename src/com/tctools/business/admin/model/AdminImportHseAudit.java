package com.tctools.business.admin.model;

import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.user.User;
import com.tctools.business.service.locale.AppLangKey;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class AdminImportHseAudit {

    private static final Logger log = LoggerFactory.getLogger(AdminImportHseAudit.class);


    // done work probably deprecated
    public static void work(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }

        if (!params.contains("f")) {
            ui.beginUploadForm()
                .addFile(Locale.getString(AppLangKey.IMPORT_BTS_FILE), "file")
                .addEmptyLine()
                .addSubmit(Locale.getString(VantarKey.ADMIN_IMPORT))
                .finish();
            return;
        }

        Map<Integer, HseAuditQuestion> questions = new HashMap<>();
        try {
            for (HseAuditQuestion q : Services.get(ServiceDtoCache.class).getList(HseAuditQuestion.class)) {
                questions.put(q.order, q);
            }
        } catch (ServiceException e) {
            ui.addErrorMessage(e);
            return;
        }

        String filepath = FileUtil.getTempFilename();
        Params.Uploaded uploaded = params.upload("file");
        uploaded.moveTo(filepath);

        Workbook wb;
        try {
            FileInputStream inputStream = new FileInputStream(filepath);
            wb = WorkbookFactory.create(inputStream);
        } catch (IOException e) {
            log.error("!", e);
            return;
        }

        Sheet sheet = wb.getSheetAt(0);

        AtomicInteger i = new AtomicInteger(0);
        sheet.rowIterator().forEachRemaining((cells) -> {
            i.incrementAndGet();
            if (i.get() < 2) {
                return;
            }
            HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
            QueryBuilder q = new QueryBuilder(flow);
            String siteCode = cells.getCell(4).getStringCellValue().toUpperCase();
            q.condition().equal("site.code", siteCode);
            try {
                List<Dto> flows = CommonRepoMongo.getData(q);
                if (flows.size() != 1) {
                    ui.addErrorMessage(siteCode + ": " + flows.size() + " items found!").write();
                    return;
                }
                flow = (HseAuditQuestionnaire) flows.get(0);
            } catch (DatabaseException e) {
                ui.addErrorMessage(e).write();
                return;
            } catch (NoContentException e) {
                ui.addErrorMessage(siteCode + ": no data").write();
                return;
            }

            boolean addState = flow.lastState != HseAuditFlowState.Approved;

            try {
                flow.auditDateTime = new DateTime("13" + StringUtil.replace(cells.getCell(1).getStringCellValue(), '.', '-'));
            } catch (DateTimeException e) {
                ui.addErrorMessage("! invalid date : " + cells.getCell(1).getStringCellValue());
            }

            flow.assigneeId = getUserId(cells.getCell(6).getStringCellValue());
            flow.subContractorId = getContractorId(cells.getCell(5).getStringCellValue());

            try {
                flow.activity = HseAuditActivity.valueOf(StringUtil.remove(cells.getCell(7).getStringCellValue(), ' '));
            } catch (Exception e) {
                ui.addErrorMessage("! invalid activity : " + cells.getCell(7).getStringCellValue());
            }

            if (flow.assigneeId == null) {
                ui.addErrorMessage("! assignee not exists : " + cells.getCell(6).getStringCellValue());
                return;
            }
            if (flow.subContractorId == null) {
                ui.addErrorMessage("! sub contractor not exists : " + cells.getCell(5).getStringCellValue());
                return;
            }

            flow.criticalNoCount = ((Number) cells.getCell(14).getNumericCellValue()).intValue();
            flow.majorNoCount = ((Number) cells.getCell(15).getNumericCellValue()).intValue();
            flow.minorNoCount = ((Number) cells.getCell(16).getNumericCellValue()).intValue();

            String supervisor = cells.getCell(9).getStringCellValue(); //73
            String climbers = cells.getCell(10).getStringCellValue(); // 9
            String employees = cells.getCell(11).getStringCellValue(); // 11

            flow.answers = new ArrayList<>();

            for (int k = 1; k < 13; ++k) {
                HseAuditQuestion question = questions.get(k);
                if (question == null) {
                    continue;
                }
                HseAuditAnswer answer = new HseAuditAnswer();
                answer.questionId = question.id;
                answer.question = question;
                flow.answers.add(answer);
            }

            for (int k = 13, m = 17; k <= 77; k+=2, ++m) {
                HseAuditQuestion question = questions.get(k);

                if (k == 73) {
                    HseAuditAnswer answer = new HseAuditAnswer();
                    answer.questionId = question.id;
                    answer.question = question;
                    flow.answers.add(answer);
                    continue;
                }

                String v;
                try {
                    v = cells.getCell(m).getStringCellValue();
                } catch (IllegalStateException e) {
                    v = Integer.toString(new Double(cells.getCell(m).getNumericCellValue()).intValue());
                }

                HseAuditAnswer answer = new HseAuditAnswer();

                answer.questionId = question.id;
                answer.question = question;
                if (v.equals("1")) {
                    answer.answer = HseAuditAnswerOption.Yes;
                } else if (v.equals("0")) {
                    answer.answer = HseAuditAnswerOption.No;
                } else {
                    answer.answer = HseAuditAnswerOption.NA;
                }
                flow.answers.add(answer);
            }

            flow.assignorId = 1L;
            flow.lastState = HseAuditFlowState.Approved;
            flow.lastStateDateTime = flow.auditDateTime;
            flow.assignDateTime = flow.lastStateDateTime;
            flow.scheduledDateTimeFrom = flow.lastStateDateTime;
            flow.scheduledDateTimeTo = flow.lastStateDateTime;
            flow.auditDateTime = flow.lastStateDateTime;

            if (flow.state == null) {
                flow.state = new ArrayList<>();
            }

            if (addState) {
                State state = new State();
                state.dateTime = flow.lastStateDateTime;
                state.state = HseAuditFlowState.Planned;
                state.userId = 1L;
                flow.state.add(state);

                state = new State();
                state.dateTime = flow.lastStateDateTime;
                state.state = HseAuditFlowState.Completed;
                state.userId = flow.assigneeId;
                flow.state.add(state);

                state = new State();
                state.dateTime = flow.lastStateDateTime;
                state.state = HseAuditFlowState.Approved;
                state.userId = 1L;
                flow.state.add(state);
            }
            try {
                CommonRepoMongo.update(flow);
                ui.addMessage("Inserted : " + flow.site.code);
            } catch (DatabaseException e) {
                ui.addErrorMessage(e);
            }
        });

        ui.finish();
    }

    private static Long getUserId(String name) {
        Map<String, Long> map = new HashMap<>();
        map.put("Negahban", 14L);
        map.put("Davarian", 13L);
        map.put("Poshtdar", 24L);
        map.put("Shahhoseini", 17L);
        map.put("Doosti", 6L);
        map.put("Mirzaei", 16L);
        map.put("Hatami", 18L);
        return map.get(name);
    }

    private static Long getContractorId(String name) {
        Map<String, Long> map = new HashMap<>();
        map.put("Porsoo", 125L);
        map.put("Farhikhtegan", 123L);
        map.put("Asre Ertebatat", 220L);
        map.put("Radmehr", 212L);
        map.put("Media", 127L);
        map.put("Sanam", 210L);
        map.put("Arkaertebet", 214L);
        map.put("Farafan", 21L);
        map.put("Kaspian", 68L);
        return map.get(name);
    }

    public static void fix(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(AppLangKey.ADMIN_FIX), params, response);
        if (ui == null) {
            return;
        }

        if (!params.contains("f")) {
            ui.beginFormGet()
                .addSubmit(Locale.getString(VantarKey.ADMIN_CONFIRM))
                .finish();
            return;
        }

        List<HseAuditQuestion> questions;
        try {
            questions = Services.get(ServiceDtoCache.class).getList(HseAuditQuestion.class);
        } catch (ServiceException e) {
            return;
        }
        Map<Long, HseAuditQuestion> questionMap = new HashMap<>();
        for (HseAuditQuestion q : questions) {
            questionMap.put(q.id, q);
        }

        List<User> users;
        try {
            users = Services.get(ServiceDtoCache.class).getList(User.class);
        } catch (ServiceException e) {
            return;
        }
        Map<Long, User> userMap = new HashMap<>();
        for (User u : users) {
            userMap.put(u.id, u);
        }

        HseAuditQuestionnaire flow = new HseAuditQuestionnaire();
        try {
            for (Dto dto : CommonRepoMongo.getAll(flow)) {
                boolean updated = false;
                flow = (HseAuditQuestionnaire) dto;

                if (flow.lastState == HseAuditFlowState.Completed || flow.lastState == HseAuditFlowState.Approved
                    || flow.lastState == HseAuditFlowState.Terminated) {
                    updated = true;
                }

                if (flow.answers != null) {
                    if (flow.answers.size() > 50) {
                        ui.addErrorMessage("invalid question number > " + flow.site.code);
                        Set<Long> included = new HashSet<>();
                        List<HseAuditAnswer> answers = new ArrayList<>();
                        for (HseAuditAnswer answer : flow.answers) {
                            if (included.contains(answer.questionId)) {
                                continue;
                            }
                            included.add(answer.questionId);
                            answers.add(answer);
                        }
                        flow.answers = answers;
                        ui.addMessage("fixed invalid questions > " + flow.site.code);
                    }
                    for (HseAuditAnswer answer : flow.answers) {
                        updated = true;
                        if (answer.question.questionType == HseAuditQuestionType.Option && answer.question.optionLabel == null) {
                            answer.question = questionMap.get(answer.questionId);
                        }
                    }

                    flow.answers.sort((a1, a2) ->
                        a1.question == null || a2.question == null || a1.question.order == null || a2.question.order == null ?
                            0 : a1.question.order.compareTo(a2.question.order));
                }

                if (flow.state != null) {
                    for (State state : flow.state) {
                        if (state.userId != null) {
                            updated = true;
                            User user = userMap.get(state.userId);
                            if (user != null) {
                                state.userName = user.fullName;
                            }
                        }
                    }
                }

                if (updated) {
                    CommonRepoMongo.update(flow);
                    ui.addMessage("updated HSE > " + flow.site.code);
                }
            }
        } catch (NoContentException | DatabaseException e) {
            ui.addErrorMessage(e);
        }

        ui.finish();
    }

    public static void fixFiles(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(AppLangKey.ADMIN_FIX), params, response);
        if (ui == null) {
            return;
        }

        if (!params.contains("f")) {
            ui.beginFormGet()
                .addCheckbox("RENAME", "rename")
                .addSubmit(Locale.getString(VantarKey.ADMIN_CONFIRM))
                .finish();
            return;
        }

        try {
            drawStructure(ui, "/static/tc-tools/", "/opt/tc-tools/files/hse-audit/");
        } catch (IOException e) {
            ui.addErrorMessage(e);
        }


        ui.finish();
    }

    private static List<Dto> dtos;

    private static void drawStructure(WebUi ui, String path, String dir) throws IOException {
        ui.beginTree(dir).write();

        Files.list(Paths.get(dir))
            .filter(Files::isDirectory)
            .forEach((file) -> {
                try {
                    drawStructure(ui, path + file.getFileName().toString() + "/", file.toAbsolutePath().toString());
                } catch (IOException e) {
                    ui.addErrorMessage(e);
                }
            });

        final boolean rename = ui.params.isChecked("rename");
        dtos = null;

        Files.list(Paths.get(dir))
            .filter(Files::isRegularFile)
            .forEach((file) -> {

                String filename = file.getFileName().toString();
                String msg = " ??? ";
                String[] parts = StringUtil.split(filename, '-');
                String newFilename = null;

                String filePath = dir + '/' + filename;
                DateTime lastModify = FileUtil.getLastModify(filePath);

                if (parts.length == (StringUtil.contains(filename, "-r-") ? 5: 4) || !filename.endsWith("jpg")) {
                    msg = " > OK ";

                } else {
                    if (dtos == null) {
                        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
                        q.condition().equal("site.code", parts[0].toUpperCase());
                        try {
                            dtos = CommonRepoMongo.getData(q);
                        } catch (DatabaseException | NoContentException e) {
                            ui.addErrorMessage(e).write();
                            return;
                        }
                    }

                    if (dtos.size() == 1) {
                        parts[0] += "-" + dtos.get(0).getId();
                        newFilename = CollectionUtil.join(parts, '-');
                        msg = " > " + newFilename + " ";
                    } else {
                        int ix = StringUtil.contains(filename, "-r-") ? 3 : 2;
                        Integer index;
                        if (parts.length > ix) {
                            index = StringUtil.toInteger(parts[ix].replace(".jpg", ""));
                        } else {
                            index = null;
                        }

                        if (index != null) {
                            --index;
                            for (int i = 0, dtosSize = dtos.size(); i < dtosSize; i++) {
                                Dto dto = dtos.get(i);
                                if (index == i) {
                                    parts[0] += "-" + dto.getId();
                                    newFilename = CollectionUtil.join(parts, '-');
                                    msg = " > " + newFilename + " ";
                                    break;
                                }
                            }
                        }
                    }
                }

                if (rename && newFilename != null) {
                    FileUtil.move(filePath, dir + '/' + newFilename);
                }

                ui.addPre(
                    (dtos == null ? "" : (dtos.size() > 1 ? "*** " : ""))
                        + filename + msg
                        + " .... (" + FileUtil.getSizeMb(filePath) + "MB - "
                        + lastModify.toString() + ")"
                ).write();
            });

        ui.containerEnd().write();
    }
}
