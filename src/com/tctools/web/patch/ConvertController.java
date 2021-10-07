package com.tctools.web.patch;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.tctools.business.dto.location.*;
import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.radiometric.workflow.Measurement;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.Param;
import com.tctools.common.util.*;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.common.ValidationError;
import com.vantar.database.datatype.Location;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.service.Services;
import com.vantar.service.auth.AccessStatus;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.json.Json;
import com.vantar.util.object.ObjectUtil;
import com.vantar.util.string.*;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.util.*;


@WebServlet({
    "/convert/d",
    "/convert/e",
    "/convert/index",
    "/convert/create/sh",
    "/convert/compare",
    "/convert/fix",
    "/convert/log",
    "/convert/users",
    "/convert/fix/state",
    "/convert/fix/assign",
    "/convert/change/state",
    "/convert/merge/user",

    // ?
    "/convert/fix/file/name",

    // do
    "/convert/fix/date",
    "/convert/fix/sector/one",

})
public class ConvertController extends RouteToMethod {

    private static final Logger log = LoggerFactory.getLogger(ConvertController.class);
    private static final long SHAHSANAM = 19L;
    private static final User shahsanam = Services.get(ServiceDtoCache.class).getDto(User.class, SHAHSANAM);


    public void fixSectorOne(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }


        ui.write();
        try {
            for (RadioMetricFlow f : CommonRepoMongo.getData(new RadioMetricFlow())) {
                if (f.sectors == null) {
                    ui.addErrorMessage(f.site.code).write();
                    continue;
                }
                int count = 0;
                int bestFitIndex = -1;
                List<Sector> sectors = f.sectors;
                for (int i = 0, sectorsSize = sectors.size(); i < sectorsSize; ++i) {
                    if (!sectors.get(i).isEmpty()) {
                        ++count;
                        bestFitIndex = i;
                    }
                }
                if (count != 1) {
                    continue;
                }
                f.sectors.get(bestFitIndex).selected = true;
                CommonRepoMongo.update(f);
                ui.addMessage(f.site.code).write();
            }
        } catch (DatabaseException | NoContentException ee) {
            ui.addErrorMessage(ee).write();
        }

        ui.addMessage("< < <").write();

    }

    public void fixDate(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }

        DateTime d;
        try {
            d = new DateTime("1391-12-01");
        } catch (DateTimeException e) {
            ui.addErrorMessage(e).write();
            return;
        }

        ui.write();
        try {
            for (RadioMetricFlow f : CommonRepoMongo.getData(new RadioMetricFlow())) {
                if (f.comments == null || (!f.comments.contains("MANU") && !f.comments.contains("CONVERT"))) {
                    continue;
                }

                if (f.state != null) {
                    DateTime d2 = null;
                    DateTime d3 = null;
                    DateTime dp = new DateTime(d);

                    List<State> state = f.state;

                    ListIterator<State> li = state.listIterator(state.size());
                    while (li.hasPrevious()) {
                        State s = li.previous();
                        s.dateTime = new DateTime(dp);
                        if (s.state.equals(RadioMetricFlowState.Completed)) {
                            d3 = new DateTime(dp);
                        } else if (s.state.equals(RadioMetricFlowState.Planned)) {
                            d2 = new DateTime(dp);
                        }
                        dp.decreaseDays(1);
                    }

                    f.lastStateDateTime = d;
                    f.assignDateTime = d2 == null ? d : d2;
                    f.measurementDateTime = d3 == null ? d: d3;

                    CommonRepoMongo.update(f);
                    ui.addMessage(f.site.code).write();
                }
            }
        } catch (DatabaseException | NoContentException ee) {
            ui.addErrorMessage(ee).write();
        }

        ui.addMessage("< < <").write();

    }

    public void mergeUser(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.beginFormPost();
        ui.addTextArea("User", "user");
        ui.addSubmit();
        ui.write();

        String u = params.getString("user");
        if (u == null) {
            return;
        }
        String[] oldNew = StringUtil.split(u, '>');
        long oldId = StringUtil.toLong(oldNew[0]);
        long newId = StringUtil.toLong(oldNew[1]);
        ui.addMessage("> > > " + oldId + " > " + newId).write();

        ui.write();
        try {
            for (RadioMetricFlow f : CommonRepoMongo.getData(new RadioMetricFlow())) {
                boolean update = false;
                if (f.assigneeId != null && oldId == f.assigneeId) {
                    f.assigneeId = newId;
                    update = true;
                }

                if (f.state != null) {
                    for (State s : f.state) {
                        if (s.assigneeId != null && oldId == s.assigneeId) {
                            s.assigneeId = newId;
                            update = true;
                        }
                        if (s.assignorId != null && oldId == s.assignorId) {
                            s.assignorId = newId;
                            update = true;
                        }
                    }
                }

                if (update) {
                    CommonRepoMongo.update(f);
                    ui.addMessage(f.site.code).write();
                }
            }
        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e).write();
        }

        ui.addMessage("< < <").write();

    }


    public void changeState(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }

        ui.beginFormPost();
        ui.addTextArea("Site codes", "codes");
        ui.addSubmit();
        ui.write();

        String codesS = params.getString("codes");
        if (codesS == null) {
            return;
        }
        String[] codes = StringUtil.split(codesS, '\n');
        ui.addMessage("> > >").write();


        DateTime d = new DateTime();

        for (String code : codes) {
            code = code.trim();
            if (StringUtil.isEmpty(code)) {
                continue;
            }
            QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
            q.condition().equal("site.code", code);
            try {
                List<RadioMetricFlow> data = CommonRepoMongo.getData(q);
                for (RadioMetricFlow f : data) {
                    ui.addMessage("found: " + f.id + " " + code).write();

                    if (!RadioMetricComplain.isEmpty(f.complain)) {
                        continue;
                    }
                    if (f.lastState.equals(RadioMetricFlowState.Approved)) {
                        continue;
                    }


                    f.lastState = RadioMetricFlowState.Approved;
                    f.comments = "MANUALLY APPROVED";
                    f.assignorId = 1L;
                    f.assigneeId = 1L;
                    f.assignDateTime = d;
                    f.measurementDateTime = d;

                    State s5 = new State(RadioMetricFlowState.Approved, d);
                    s5.assignorId = 1L;
                    s5.assigneeId = 1L;
                    s5.comments = f.comments;
                    f.state.add(s5);

                    CommonRepoMongo.update(f);

                    ui.addMessage("done: " + f.id + " " + code).write();
                }
            } catch (DatabaseException | NoContentException e) {
                ui.addErrorMessage("error " + code);
            }
        }

        ui.addMessage("< < <").write();
   }











    public void fixFileName(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.addMessage("> > >").write();

        Map<String, String> m = new HashMap<>();
        m.put("/1.jpg", "/" + RadioMetricPhotoType.TowerView.getFilename() + ".jpg");
        m.put("/2.jpg", "/" + RadioMetricPhotoType.ProbeViewInFrontOfMeasuredSector.getFilename() + ".jpg");
        m.put("/3.jpg", "/" + RadioMetricPhotoType.TripodInLocation.getFilename() + ".jpg");
        m.put("/4.jpg", "/" + RadioMetricPhotoType.Address.getFilename() + ".jpg");
        m.put("/5.jpg", "/" + RadioMetricPhotoType.Extra1.getFilename() + ".jpg");
        m.put("/6.jpg", "/" + RadioMetricPhotoType.Extra2.getFilename() + ".jpg");

        Callback callback = path -> {
            if (!path.endsWith(".jpg")) {
                return;
            }

            FileUtil.removeFile(path + 'x');
            path = StringUtil.replace(path, " ", "\\ ");
            String[] parts = StringUtil.split(path, '/');
            parts[parts.length-1] = "";

            String newPath = path;
            for (Map.Entry<String, String> entry : m.entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                newPath = StringUtil.replace(newPath, k, v);
            }
            ui.addMessage(path + " > " + newPath).write();
            FileUtil.move(path, newPath);
        };

        processFile("/opt/tc-tools/files/radiometric/", callback);
    }

    public void fixAssign(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.addMessage("> > >").write();

        String directory = "/opt/tc-tools/convert/files/";
        Set<String> sp;
        try {
            sp = getSpData("/opt/tc-tools/convert/sp.csv");
        } catch (IOException | CsvValidationException e) {
            ui.addErrorMessage(e).addMessage("failed to convert !!!").write();
            log.error("!", e);
            return;
        }

        DateTime d = new DateTime();
        try {
            List<RadioMetricFlow> data = CommonRepoMongo.getData(new RadioMetricFlow());
            for (RadioMetricFlow f : data) {
                if (f.lastState == null || f.site == null) {
                    ui.addErrorMessage("" + f.id);
                    continue;
                }

                if (!RadioMetricComplain.isEmpty(f.complain)) {
                    CommonRepoMongo.update(f);
                    continue;
                }
                if (f.lastState.equals(RadioMetricFlowState.Approved)) {
                    CommonRepoMongo.update(f);
                    continue;
                }
                if (!sp.contains(f.site.code)) {
                    CommonRepoMongo.update(f);
                    continue;
                }

                f.lastState = RadioMetricFlowState.Approved;
                f.comments = "CONVERT MSS MATCH";
                f.assignorId = 1L;
                f.assigneeId = 1L;
                f.assignDateTime = d;
                f.measurementDateTime = d;


                State s5 = new State(RadioMetricFlowState.Approved, d);
                s5.assignorId = 1L;
                s5.assigneeId = 1L;
                s5.comments = f.comments;
                f.state.add(s5);

                CommonRepoMongo.update(f);

                ui.addMessage(f.site.code).write();
            }
        } catch (DatabaseException | NoContentException e) {
            log.error(">>>>>>>>>?????????", e);
        }


        ui.addMessage("< < <").write();
    }

    public void fixState(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.write();

        try {
            for (RadioMetricFlow f : CommonRepoMongo.getData(new RadioMetricFlow())) {
                if (f.state == null || f.isEmpty()) {
                    continue;
                }
                f.lastState = f.state.get(f.state.size() - 1).state;
                if (f.lastState != RadioMetricFlowState.Approved) {
                    continue;
                }

                CommonRepoMongo.update(f);
                ui.addMessage(f.site.code).write();
            }
        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e).write();
        }
    }

    public void users(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.write();
        try {
            List<User> data = CommonRepoMongo.getData(new User());

            Map<String, List<Long>> users = new HashMap<>();
            Map<String, User> usersBase = new HashMap<>();
            for (User u : data) {
                if (u.projectTypes != null && u.projectTypes.contains(ProjectType.HseAudit)) {
                    continue;
                }
                List<Long> ids = users.get(u.fullName);
                if (ids == null) {
                    ids = new ArrayList<>();
                    users.put(u.fullName, ids);
                }
                ids.add(u.id);
                if (!usersBase.containsKey(u.fullName)) {
                    usersBase.put(u.fullName, u);
                }
            }

            users.forEach((k, v) -> {
                if (v.size() == 1) {
                    usersBase.remove(k);
                }
            });
            Map<String, List<Long>> users2 = new HashMap<>();
            usersBase.forEach((k, v) -> {
                users2.put(k, users.get(k));
            });


//            log.error("{}", Json.toJsonPretty(usersBase));
  //          log.error("{}", Json.toJsonPretty(users2));

            Map<Long, Long> idMap = new HashMap<>();
            usersBase.forEach((k, u) -> {
                List<Long> ids = users2.get(k);
                for (Long id : ids) {
                    if (id.equals(u.id)) {
                        continue;
                    }
                    idMap.put(id, u.id);
                }
            });

            log.error("{}", Json.toJsonPretty(idMap));


            for (RadioMetricFlow f : CommonRepoMongo.getData(new RadioMetricFlow())) {
                boolean nakon = true;
                Long id = idMap.get(f.assigneeId);
                if (id != null) {
                    f.assigneeId = id;
                    nakon = false;
                }
                id = idMap.get(f.assignorId);
                if (id != null) {
                    f.assignorId = id;
                    nakon = false;
                }

                if (f.state == null) {
                    log.error(">>>>{}", f.id);
                } else {
                    for (State s : f.state) {
                        id = idMap.get(s.assigneeId);
                        if (id != null) {
                            s.assigneeId = id;
                            nakon = false;
                        }
                        id = idMap.get(s.assignorId);
                        if (id != null) {
                            s.assignorId = id;
                            nakon = false;
                        }
                    }
                }

                if (nakon) {
                    continue;
                }
                CommonRepoMongo.update(f);
                ui.addMessage(f.site.code).write();
            }



            for (RadioMetricComplain f : CommonRepoMongo.getData(new RadioMetricComplain())) {
                boolean nakon = true;
                Long id = idMap.get(f.assigneeId);
                if (id != null) {
                    f.assigneeId = id;
                    nakon = false;
                }
                id = idMap.get(f.creatorId);
                if (id != null) {
                    f.creatorId = id;
                    nakon = false;
                }

                if (nakon) {
                    continue;
                }
                CommonRepoMongo.update(f);
                ui.addMessage(f.siteCode).write();
            }

            idMap.forEach((k, x) -> {
                User u = new User();
                u.id = k;
                try {
                    CommonRepoMongo.delete(u);
                } catch (DatabaseException e) {
                    e.printStackTrace();
                }
            });


        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e).write();
        }
    }

    public void log(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.write();
        try {
            List<RadioMetricFlow> data = CommonRepoMongo.getData(new RadioMetricFlow());
            for (RadioMetricFlow f : data) {

                boolean pass = false;
                List<ValidationError> errors = new ArrayList<>();
                for (String height : new String[] {"100", "150", "170"}) {
                    String path = RadioMetricFlow.getMeasurementPath(f.site.code, f.id, height, false, false);
                    if (path != null && !FileUtil.exists(path)) {
                        pass = true;
                        continue;
                    }
                    try {
                        Measurement.applyCsv(path, f, height, errors);
                    } catch (Exception e) {
                        ui.addErrorMessage(f.site.code).write();
                        ui.addErrorMessage(e).write();
                        errors.add(null);
                    }
                }
                if (pass) {
                    continue;
                }

                if (!errors.isEmpty()) {
                    ui.addErrorMessage(f.site.code + " " + CollectionUtil.join(errors, "\n")).write();
                    continue;
                }

                CommonRepoMongo.update(f);
                ui.addMessage(f.site.code).write();
            }
        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e).write();
        }
    }

    public void fix(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        try {
            List<RadioMetricFlow> data = CommonRepoMongo.getData(new RadioMetricFlow());
            for (RadioMetricFlow f : data) {

                List<Proximity> proximities = f.proximities;
                Set<RadioMetricProximityType> availableProximities = new HashSet<>(5);
                if (proximities == null) {
                    proximities = new ArrayList<>(5);
                } else {
                    for (Proximity p : proximities) {
                        availableProximities.add(p.proximityType);
                    }
                }
                for (RadioMetricProximityType t : RadioMetricProximityType.values()) {
                    if (availableProximities.contains(t)) {
                        continue;
                    }
                    Proximity p = new Proximity();
                    p.proximityType = t;
                    proximities.add(p);
                }
                f.proximities = proximities;



                CommonRepoMongo.update(f);
                ui.addErrorMessage(f.site.code).write();
            }
        } catch (DatabaseException | NoContentException ignore) {
            ui.addErrorMessage(ignore).write();
        }

    }

    public void d(Params params, HttpServletResponse response) {
        Map<String, String> y = extractData(params.getString("path", "/opt/tc-tools/convert/files/Regular New/98.03.19/تهران/TH1380/TH1380.docx"), null);
        String data = Json.toJsonPretty(y);
        log.info("{}", data);
        Response.writeString(response, "<pre>" + data);
    }

    public void e(Params params, HttpServletResponse response) {
        log.info(docToDox(params.getString("path"), null));
    }

    public void createSh(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.addMessage("doc to dox > > >").write();
        String directory = "/opt/tc-tools/convert/files/";
        List<String> commands = new ArrayList<>();
        Callback callback = path -> {
            if (!path.endsWith(".doc")) {
                return;
            }

            FileUtil.removeFile(path + 'x');
            path = StringUtil.replace(path, " ", "\\ ");
            String[] parts = StringUtil.split(path, '/');
            parts[parts.length-1] = "";
            String dir = CollectionUtil.join(parts, '/');

            commands.add("lowriter run many  --headless --convert-to docx " + path + " --outdir " + dir);
        };

        processFile(directory, callback);
        FileUtil.write("/opt/tc-tools/convert/doctodocx.sh", "#!/bin/bash\n" + CollectionUtil.join(commands, '\n'));

        ui  .addMessage("finished < < <")
            .addMessage("/opt/tc-tools/convert/doctodocx.sh")
            .write();
    }

    public void compare(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.addMessage("> > >").write();

        String directory = "/opt/tc-tools/convert/files/";
        Map<String, SPCC> sp;
        Map<String, SPCC> cc;
        Map<String, String> statuses;
        try {
            statuses = getStatusData("/opt/tc-tools/convert/status.csv", ui);
            sp = getSpData("/opt/tc-tools/convert/sp.csv", statuses, ui);
            cc = getCcData("/opt/tc-tools/convert/cc.csv", statuses, ui);
        } catch (IOException | CsvValidationException e) {
            ui.addErrorMessage(e).addMessage("failed to convert !!!").write();
            log.error("!", e);
            return;
        }

        Set<String> siteCodes = new HashSet<>(35000);
        try {
            List<RadioMetricFlow> data = CommonRepoMongo.getData(new RadioMetricFlow());
            for (RadioMetricFlow f : data) {
                siteCodes.add(f.site.code);
            }
        } catch (DatabaseException | NoContentException ignore) {

        }

        ui.addErrorMessage("-------------------------");
        sp.forEach((siteCode, d) -> {
            String[] parts = StringUtil.split(siteCode, "__");
            if (!statuses.containsKey(parts[0])) {
                ui.addErrorMessage(siteCode + " exists in SP not in status");
            }
            if (!siteCodes.contains(parts[0])) {
                ui.addErrorMessage(siteCode + " exists in SP not in db sites");
            }
        });

        ui.addErrorMessage("-------------------------");
        cc.forEach((siteCode, status) -> {
            String[] parts = StringUtil.split(siteCode, "__");
            if (!statuses.containsKey(parts[0])) {
                ui.addErrorMessage(siteCode + " exists in CC not in status");
            }
            if (!siteCodes.contains(parts[0])) {
                ui.addErrorMessage(siteCode + " exists in SP not in db sites");
            }
        });

        ui.addErrorMessage("-------------------------");
        statuses.forEach((siteCode, status) -> {
            if (!siteCodes.contains(siteCode)) {
                ui.addErrorMessage(siteCode + " exists in status not in db sites");
            }
        });

        ui.addErrorMessage("-------------------------");
        Map<String, List<String>> pt = new HashMap<>(35000);
        Callback callback = path -> {
            if (!path.endsWith(".docx")) {
                return;
            }

            boolean isCC = path.contains("C.C");
            String dateStr = null;
            String[] pp = StringUtil.split(path, '/');
            for (String p : pp) {
                p = StringUtil.remove(p, "C.C").trim();
                if (StringUtil.countMatches(p, '.') == 2) {
                    dateStr = normalizeDate(p);
                    break;
                }
            }

            String[] partss = StringUtil.split(StringUtil.remove(pp[pp.length - 1], ".docx"), '_');
            List<String> dt = pt.get(partss[0]);
            if (dt == null) {
                dt = new ArrayList<>();
            }
            dt.add(path);
            pt.put(partss[0], dt);

            ui.addErrorMessage((isCC ? "CC " : "SP ") + dateStr + " > " + path);

            if (isCC) {
                cc.forEach((siteCode, d) -> {
                    String[] parts = StringUtil.split(siteCode, "__");
                    if (path.contains(parts[0])) {
                        ui.addErrorMessage("------- " + d.getDate() + " " + d.getStatus() + " " + d.getCcnumber());
                    }
                });
            } else {
                sp.forEach((siteCode, d) -> {
                    String[] parts = StringUtil.split(siteCode, "__");
                    if (path.contains(parts[0])) {
                        ui.addErrorMessage("------- " + d.getDate() + " " + d.getStatus() + " ");
                    }
                });
            }
        };
        processFile(directory, callback);

        ui.addErrorMessage("-------------------------");

        pt.forEach((siteCode, dates) -> {
            if (!statuses.containsKey(siteCode)) {
                ui.addErrorMessage(siteCode + " exists in files not in status excel " + dates.toString());
            }
        });

        ui.addMessage("< < <").write();
    }

    public void index(Params params, HttpServletResponse response) {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response);
        if (ui == null) {
            return;
        }
        ui.addMessage("begin convert > > >").write();

        Map<String, SPCC> sp;
        Map<String, SPCC> cc;
        try {
            Map<String, String> statuses = getStatusData("/opt/tc-tools/convert/status.csv", ui);
            sp = getSpData("/opt/tc-tools/convert/sp.csv", statuses, ui);
            cc = getCcData("/opt/tc-tools/convert/cc.csv", statuses, ui);
        } catch (IOException | CsvValidationException e) {
            ui.addErrorMessage(e).addMessage("failed to convert !!!").write();
            log.error("!", e);
            return;
        }


        String directory = "/opt/tc-tools/convert/files/";
        //String directory = "/opt/tc-tools/convert/files/C.C Old/";
        //String directory = "/opt/tc-tools/convert/files/Regular New/";
        List<PropertyType> propertyTypes = Services.get(ServiceDtoCache.class).getList(PropertyType.class);
        List<PropertySection> propertySections = Services.get(ServiceDtoCache.class).getList(PropertySection.class);
        List<SiteType> siteTypes = Services.get(ServiceDtoCache.class).getList(SiteType.class);
        List<Operator> operators = Services.get(ServiceDtoCache.class).getList(Operator.class);


        Set<String> doneSiteCodes = new HashSet<>(40000);
        Callback callback = path -> {
            if (path.endsWith(".doc") && !FileUtil.exists(path + "x")) {
                ui.addErrorMessage(path + " docx not exists").write();
            }
            if (!path.endsWith(".docx")) {
                return;
            }

            ui.addMessage(path + " > > >").write();

            boolean isOld = FileUtil.exists(path.substring(0, path.length() - 1));
            boolean isCC = path.contains("C.C");

            String dateStr = null;
            String[] pp = StringUtil.split(path, '/');
            for (String p : pp) {
                p = StringUtil.remove(p, "C.C").trim();
                if (StringUtil.countMatches(p, '.') == 2) {
                    dateStr = normalizeDate(p);
                    break;
                }
            }
            DateTime date = null;
            try {
                date = new DateTime(dateStr);
            } catch (DateTimeException e) {
                log.error("{}", dateStr, e);
            }

            pp[pp.length - 1] = "";
            String pathOnly = CollectionUtil.join(pp, '/');

            String[] parts = StringUtil.split(StringUtil.remove(path, ".docx"), "__");
            String ccnumber = parts.length == 2 ? parts[1] : "0";

            String complainer = null;
            if (isCC) {
                String[] temp = new String[2];
                try {
                    Files.list(Paths.get(pathOnly))
                        .filter(Files::isRegularFile)
                        .forEach((file) -> {
                            String fn = file.getFileName().toString();
                            if (fn.contains("شماره شکایت")) {
                                String[] ppp = StringUtil.split(StringUtil.remove(fn, "شماره شکایت"), '-');
                                temp[0] = ppp[0].trim();
                                temp[1] = ppp[1].trim();
                            }
                        });

                    ccnumber = temp[0];
                    complainer = temp[1];

                } catch (IOException e) {
                    log.error("!", e);
                }
            }
            try {
                ccnumber = StringUtil.isEmpty(ccnumber) ? "0" : Integer.toString(StringUtil.toInteger(Persian.Number.toLatin(ccnumber)));
            } catch (Exception e) {
                ccnumber = "0";
            }


            Map<String, String> data = extractData(path, ui);
            if (data == null) {
                ui.addErrorMessage("    docx: data extract failed").write();
                return;
            }


            boolean isIndexed = data.containsKey("__type");
            data.remove("__type");

            String siteCode = getData(data, "siteCode", isIndexed, isOld);
            if (StringUtil.isEmpty(siteCode)) {
                ui.addErrorMessage("    docx: siteCode not found " + siteCode).write();
                return;
            }

            if (!isCC) {
                if (doneSiteCodes.contains(siteCode)) {
                    isCC = true;
                }
                doneSiteCodes.add(siteCode);
            }

            User user = getTechnician(getData(data, "technician", isIndexed, isOld), ui);
            if (user == null) {
                ui.addErrorMessage("    docx: technician not found").write();
            }


            SPCC spcc = isCC ? cc.get(siteCode + "__" + dateStr) : sp.get(siteCode + "__" + dateStr);
            if (spcc == null) {
                ui.addErrorMessage("    " + siteCode + " " + (isCC ? (" CC(" + ccnumber + ")") : "SP")
                    + ": has files but not recorded in excel").write();
                return;
            }

            RadioMetricFlow flow;
            if (isCC) {
                RadioMetricComplain complain = getComplain(
                    data,
                    siteCode,
                    ccnumber,
                    user == null ? 0 : user.id,
                    isIndexed,
                    isOld,
                    date,
                    complainer,
                    propertyTypes,
                    propertySections,
                    ui
                );
                if (RadioMetricComplain.isEmpty(complain)) {
                    ui.addErrorMessage("    complain: failed to create record").write();
                    return;
                }
                flow = getNewFlow(complain, siteCode, user == null ? 0 : user.id, ui);
                if (flow == null) {
                    ui.addErrorMessage("    flow: failed to create record").write();
                    return;
                }
                ui.addMessage("    created complain and flow");
            } else {
                flow = getWorkFlow(siteCode, ui);
                if (flow == null) {
                    ui.addErrorMessage("    flow: failed to fetch record").write();
                    return;
                }
            }

            flow.assigneeId = user == null ? 0 : user.id;
            flow.siteAddress = StringUtil.remove(getData(data, "siteAddress", isIndexed, isOld), "آدرس:");
            flow.siteLocation = stringToLocation(getData(data, "siteLocation", isIndexed, isOld));
            if (flow.siteLocation == null) {
                flow.siteLocation = flow.site.location;
            }

            // date time
            String d = getData(data, "measurementDate", isIndexed, isOld);
            String t = getData(data, "measurementTime", isIndexed, isOld);

            if (StringUtil.isEmpty(d)) {
                d = spcc.getDate();
                if (StringUtil.isEmpty(d)) {
                    d = dateStr;
                    if (StringUtil.isEmpty(d)) {
                        ui.addErrorMessage("    no date available");
                    }
                }
            }
            d = normalizeDate(d);
            if (StringUtil.isNotEmpty(d) && StringUtil.isNotEmpty(t)) {
                d = d + " " + Persian.Number.toLatin(t);
            }
            try {
                flow.measurementDateTime = new DateTime(d);
            } catch (DateTimeException e) {
                ui.addErrorMessage("    measurementDateTime date error " + d);
                return;
            }


            flow.site.siteTypeId = getSiteType(siteTypes, spcc.getType());
            if (flow.site.siteTypeId == null) {
                ui.addErrorMessage("    SiteType not found: " + spcc.getType());
                return;
            }


            flow.site.frequencyBand = getData(data, "frequencyBand", isIndexed, isOld);
            flow.reporter = getData(data, "reporter", isIndexed, isOld);
            flow.healthPhysics = getData(data, "healthPhysics", isIndexed, isOld);
            flow.comments = getData(data, "comments", isIndexed, isOld);
            flow.assignorId = SHAHSANAM;
            flow.siteFloor = StringUtil.scrapeInt(getData(data, "siteFloor", isIndexed, isOld));
            flow.horizontalDistanceFromLocation = StringUtil.toInteger(getData(data, "horizontalDistanceFromLocation", isIndexed, isOld));
            flow.measurementHeight = StringUtil.toDouble(getData(data, "measurementHeight", isIndexed, isOld));

            flow.densityMax100 = StringUtil.toDouble(getData(data, "densityMax100", isIndexed, isOld));
            flow.densityAverage6min100 = StringUtil.toDouble(getData(data, "densityAverage6min100", isIndexed, isOld));
            flow.densityMax150 = StringUtil.toDouble(getData(data, "densityMax150", isIndexed, isOld));
            flow.densityAverage6min150 = StringUtil.toDouble(getData(data, "densityAverage6min150", isIndexed, isOld));
            flow.densityMax170 = StringUtil.toDouble(getData(data, "densityMax170", isIndexed, isOld));
            flow.densityAverage6min170 = StringUtil.toDouble(getData(data, "densityAverage6min170", isIndexed, isOld));
            if (flow.densityMax100 == null) {
                flow.densityMax100 = flow.densityAverage6min100;
            }
            if (flow.densityMax150 == null) {
                flow.densityMax150 = flow.densityAverage6min150;
            }
            if (flow.densityMax170 == null) {
                flow.densityMax170 = flow.densityAverage6min170;
            }
            flow.densityMin100 = flow.densityMax100;
            flow.densityMin150 = flow.densityMax150;
            flow.densityMin170 = flow.densityMax170;
            flow.radiationStatus100 = RadioMetricRadiationStatus.Compatible;
            flow.radiationStatus150 = RadioMetricRadiationStatus.Compatible;
            flow.radiationStatus170 = RadioMetricRadiationStatus.Compatible;
            flow.logDateTime100 = flow.measurementDateTime;
            flow.logDateTime150 = flow.measurementDateTime;
            flow.logDateTime170 = flow.measurementDateTime;
            flow.isMeasurementTimeAcceptable100 = true;
            flow.isMeasurementTimeAcceptable150 = true;
            flow.isMeasurementTimeAcceptable170 = true;
            flow.isMeasurementRecordCountAcceptable100 = true;
            flow.isMeasurementRecordCountAcceptable150 = true;
            flow.isMeasurementRecordCountAcceptable170 = true;
            flow.isMeasurementGpsDataAvailable100 = true;
            flow.isMeasurementGpsDataAvailable150 = true;
            flow.isMeasurementGpsDataAvailable170 = true;

            try {
                flow.reportedProvince = Services.get(ServiceDtoCache.class).getDto(Province.class, flow.site.provinceId).name.get("fa");
                flow.reportedCity = Services.get(ServiceDtoCache.class).getDto(City.class, flow.site.cityId).name.get("fa");
            } catch (Exception ignore) {

            }

            flow.spotLocation = flow.siteLocation;

            flow.deviceManufacturer = getData(data, "deviceManufacturer", isIndexed, isOld);
            flow.deviceModel = getData(data, "deviceModel", isIndexed, isOld);
            flow.deviceSerialNumber = getData(data, "deviceSerialNumber", isIndexed, isOld);
            flow.deviceTitle = getData(data, "deviceTitle", isIndexed, isOld);
            flow.deviceProbe = getData(data, "deviceProbe", isIndexed, isOld);
            try {
                flow.deviceCalibrationExpire = new DateTime(normalizeDate(getData(data, "deviceCalibrationExpire", isIndexed, isOld)));
            } catch (DateTimeException e) {
                ui.addErrorMessage("    deviceCalibrationExpire date error " +  flow.deviceCalibrationExpire );
            }

            // sector
            String antennaHeight = getData(data, "antennaHeight", isIndexed, isOld);
            if (StringUtil.isNotEmpty(antennaHeight)) {
                flow.sectors = flow.site.sectors;
                if (flow.sectors == null) {
                    flow.sectors = new ArrayList<>(10);
                }

                Map<String, Sector> currentSectors = new HashMap<>(10);
                for (Sector s : flow.sectors) {
                    s.selected = false;
                    currentSectors.put(s.title, s);
                }

                Set<String> reportedSectors = new HashSet<>(3);
                reportedSectors.add("A");
                reportedSectors.add("B");
                reportedSectors.add("C");

                for (String sv : StringUtil.split(antennaHeight, "Sector ")) {
                    String[] sectorValue = StringUtil.split(sv, '\u003d', '=');
                    String title = sectorValue[0].trim();
                    if ((!title.equalsIgnoreCase("A") && !title.equalsIgnoreCase("B") && !title.equalsIgnoreCase("C")
                        && !title.equalsIgnoreCase("D")) || sectorValue.length != 2) {

                        continue;
                    }
                    Sector s = currentSectors.get(title);
                    if (s == null) {
                        s = new Sector(title);
                    }
                    s.height = StringUtil.toDouble(sectorValue[1].trim());
                    reportedSectors.remove(title);
                }

                for (String s : reportedSectors) {
                    Sector sec = currentSectors.get(s);
                    if (sec != null) {
                        sec.empty();
                    }
                }

                String selectedSectorSection = spcc.getSector();
                if (selectedSectorSection != null) {
                    Sector s = currentSectors.get(selectedSectorSection);
                    if (s != null) {
                        s.selected = true;
                    }
                }

                Integer azimuthA = StringUtil.toInteger(getData(data, "azimuthA", isIndexed, isOld));
                Integer mechanicalTiltA = StringUtil.toInteger(getData(data, "mechanicalTiltA", isIndexed, isOld));
                Integer electricalTiltA = StringUtil.toInteger(getData(data, "electricalTiltA", isIndexed, isOld));
                Sector s = currentSectors.get("A");
                if (s != null) {
                    if (azimuthA != null) {
                        s.azimuth = azimuthA;
                    }
                    if (mechanicalTiltA != null) {
                        s.mechanicalTilt = mechanicalTiltA;
                    }
                    if (electricalTiltA != null) {
                        s.electricalTilt = electricalTiltA;
                    }
                }
                Integer azimuthB = StringUtil.toInteger(getData(data, "azimuthB", isIndexed, isOld));
                Integer mechanicalTiltB = StringUtil.toInteger(getData(data, "mechanicalTiltB", isIndexed, isOld));
                Integer electricalTiltB = StringUtil.toInteger(getData(data, "electricalTiltB", isIndexed, isOld));
                s = currentSectors.get("B");
                if (s != null) {
                    if (azimuthB != null) {
                        s.azimuth = azimuthB;
                    }
                    if (mechanicalTiltB != null) {
                        s.mechanicalTilt = mechanicalTiltB;
                    }
                    if (electricalTiltB != null) {
                        s.electricalTilt = electricalTiltB;
                    }
                }
                Integer azimuthC = StringUtil.toInteger(getData(data, "azimuthC", isIndexed, isOld));
                Integer mechanicalTiltC = StringUtil.toInteger(getData(data, "mechanicalTiltC", isIndexed, isOld));
                Integer electricalTiltC = StringUtil.toInteger(getData(data, "electricalTiltC", isIndexed, isOld));
                s = currentSectors.get("C");
                if (s != null) {
                    if (azimuthC != null) {
                        s.azimuth = azimuthC;
                    }
                    if (mechanicalTiltC != null) {
                        s.mechanicalTilt = mechanicalTiltC;
                    }
                    if (electricalTiltC != null) {
                        s.electricalTilt = electricalTiltC;
                    }
                }

                flow.sectors = new ArrayList<>(currentSectors.values());
            }

            // proximity
            Proximity pHealth = new Proximity();
            pHealth.proximityType = RadioMetricProximityType.HealthFacility;
            Proximity pEducational = new Proximity();
            pEducational.proximityType = RadioMetricProximityType.EducationalInstitution;

            String proximity = getData(data, "proximityHealth", isIndexed, isOld);
            if (StringUtil.isEmpty(proximity)) {
                pHealth.comments = StringUtil.remove(proximity, "در صورت پاسخ مثبت نام مرکز:");
            }

            proximity = getData(data, "proximityEducation", isIndexed, isOld);
            if (StringUtil.isEmpty(proximity)) {
                pEducational.comments = StringUtil.remove(proximity, "در صورت پاسخ مثبت نام مرکز:");
            }

            flow.proximities = new ArrayList<>(2);
            flow.proximities.add(pHealth);
            flow.proximities.add(pEducational);

            // collocation
            Long op1 = getOperator(operators, data.get("62AD96C0"), data.get("37180B29"), ui);
            Long op2 = getOperator(operators, data.get("3070F68B"), data.get("6ABBDBFA"), ui);
            Long op3 = getOperator(operators, data.get("43F2FCE2"), data.get("5B1D0DF9"), ui);
            flow.collocations = new ArrayList<>(3);
            if (op1 != null) {
                flow.collocations.add(new Collocation(CollocationType.Guest, op1));
            }
            if (op2 != null) {
                flow.collocations.add(new Collocation(CollocationType.Guest, op2));
            }
            if (op3 != null) {
                flow.collocations.add(new Collocation(CollocationType.Guest, op3));
            }
            if (flow.collocations.isEmpty()) {
                flow.collocations = null;
            }

            // add state
            flow.state = new ArrayList<>(3);

            // pending
            flow.state.add(new State(RadioMetricFlowState.Pending, new DateTime(flow.measurementDateTime).decreaseDays(2)));

            if (spcc.getStatus().equalsIgnoreCase("Not Available")) {
                flow.assignable = false;
            } else {
                if (spcc.getStatus().equalsIgnoreCase("in Service")) {
                    ui.addErrorMessage(siteCode + " conflict status 'in service' but has data");
                }

                // planed
                flow.assignDateTime = new DateTime(flow.measurementDateTime).decreaseDays(1);
                State s2 = new State(RadioMetricFlowState.Planned, flow.assignDateTime);
                s2.assignorId = flow.assignorId;
                s2.assigneeId = flow.assigneeId;
                flow.state.add(s2);

                // completed
                State s3 = new State(RadioMetricFlowState.Completed, flow.measurementDateTime);
                s3.assignorId = flow.assignorId;
                s3.assigneeId = flow.assigneeId;
                flow.state.add(s3);

                // approved
                flow.lastState = spcc.getStatus().equalsIgnoreCase("Problematic") ? RadioMetricFlowState.Problematic : RadioMetricFlowState.Approved;
                State s4 = new State(
                    flow.lastState,
                    new DateTime(flow.measurementDateTime).addDays(1)
                );
                s4.assignorId = flow.assignorId;
                s4.assigneeId = flow.assigneeId;
                flow.state.add(s4);

                flow.lastStateDateTime = new DateTime(flow.measurementDateTime).addDays(2);
                State s5 = new State(RadioMetricFlowState.Approved, flow.lastStateDateTime);
                s5.assignorId = flow.assignorId;
                s5.assigneeId = flow.assigneeId;
                flow.state.add(s5);

                flow.spotAddress = StringUtil.remove(getData(data, "complainAddress", isIndexed, isOld), "آدرس:");
            }

            try {
                CommonRepoMongo.update(flow);
            } catch (DatabaseException e) {
                ui.addErrorMessage("    database error: " + siteCode);
                log.error("! {}", siteCode, e);
                return;
            }

            String fp = Param.RADIO_METRIC_FILES + flow.site.code + "/measurement/" + flow.id + "/";
            try {
                FileUtil.makeDirectory(fp);
                Files.list(Paths.get(pathOnly))
                    .filter(Files::isRegularFile)
                    .forEach((file) -> {
                        FileUtil.move(pathOnly + file.getFileName().toString(), fp + file.getFileName().toString());
                    });

                if (isCC) {
                    cc.remove(siteCode + "__" + dateStr);
                } else {
                    sp.remove(siteCode + "__" + dateStr);
                }

            } catch (IOException e) {
                log.error("!", e);
            }
        };

        processFile(directory, callback);

        ui.addMessage("finished convert < < <").write();
        ui.addErrorMessage("no data but in excel:").write();

        sp.forEach((k, v) -> {
            ui.addErrorMessage("SP " + k).write();
        });
        cc.forEach((k, v) -> {
            ui.addErrorMessage("CC " + k).write();
        });
    }

    private static RadioMetricFlow getNewFlow(
        RadioMetricComplain complain,
        String siteCode,
        long assigneeId,
        WebUi ui) {

        RadioMetricFlow flow = new RadioMetricFlow();
        flow.assigneeId = assigneeId;
        flow.complain = complain;
        flow.complain.id = complain.id;

        QueryBuilder q = new QueryBuilder(new Site());
        q.condition().equal("code", siteCode);
        try {
            CommonRepoMongo.getFirst(q);
            flow.reRadioMetric = true;
        } catch (NoContentException | DatabaseException ignore) {
            flow.reRadioMetric = false;
        }

        flow.complain.assignable = false;

        try {
            flow.site = CommonRepoMongo.getFirst(q);
        } catch (DatabaseException e) {
            log.error("{}", siteCode, e);
            return null;
        } catch (NoContentException e) {
            ui.addErrorMessage("    " + siteCode + " site not exists");
            return null;
        }

        flow.assignorId = SHAHSANAM;
        flow.provinceId = flow.complain.provinceId;
        flow.cityId = flow.complain.cityId;
        flow.siteAddress = flow.site.address;
        flow.siteLocation = flow.site.location;
        flow.sectors = flow.site.sectors;

        RadioMetricComplain complainToUpdate = new RadioMetricComplain();
        complainToUpdate.id = flow.complain.id;
        complainToUpdate.assigneeId = flow.assigneeId;
        complainToUpdate.assignable = false;

        try {
            complainToUpdate.workFlowId = CommonRepoMongo.insert(flow);
            CommonRepoMongo.update(complainToUpdate);
            return flow;
        } catch (DatabaseException e) {
            log.error("{}", siteCode, e);
            return null;
        }
    }

    private static RadioMetricComplain getComplain(
        Map<String, String> data,
        String siteCode,
        String ccnumber,
        long assigneeId,
        boolean isIndexed,
        boolean isOld,
        DateTime date,
        String complainer,
        List<PropertyType> propertyTypes,
        List<PropertySection> propertySections,
        WebUi ui) {

        Site site = new Site();
        QueryBuilder q = new QueryBuilder(site);
        q.condition().equal("code", siteCode);
        try {
            site = CommonRepoMongo.getFirst(q);
        } catch (DatabaseException e) {
            log.error("{}", siteCode, e);
            return null;
        } catch (NoContentException e) {
            ui.addErrorMessage("    " + siteCode + " site not exists");
            return null;
        }
        getData(data, "siteCode", isIndexed, isOld);
        RadioMetricComplain complain = new RadioMetricComplain();
        complain.provinceId = site.provinceId;
        complain.cityId = site.cityId;
        complain.assignable = false;
        complain.creatorId = SHAHSANAM;
        complain.assigneeId = assigneeId;
        complain.siteCode = siteCode;
        complain.siteId = site.id;
        complain.siteName = site.name;
        complain.ccnumber = ccnumber;
        complain.location = getMeasurementLocation(data, isIndexed, isOld);

        complain.type = ccnumber.equals("0") ? ComplainType.NormalRequest : ComplainType.CustomerComplain;
        complain.assignTime = date;
        complain.complainTime = new DateTime(date).decreaseDays(1);
        complain.address = StringUtil.remove(getData(data, "complainAddress", isIndexed, isOld), "آدرس:");

        complain.complainerName = StringUtil.remove(getData(data,"complainerName", isIndexed, isOld), "نام و نام خانوادگی:");
        if (StringUtil.isEmpty(complain.complainerName)) {
            complain.complainerName = complainer;
        }
        complain.complainerMobile = StringUtil.remove(getData(data,"complainerMobile", isIndexed, isOld), "شماره تماس(همراه):");
        complain.complainerPhone = StringUtil.remove(getData(data,"complainerPhone", isIndexed, isOld), "شماره تماس(ثابت):");
        complain.complainerAddress = StringUtil.remove(getData(data,"complainerAddress", isIndexed, isOld), "آدرس:");
        complain.propertyId = getPropertyType(propertyTypes, getData(data, "propertyId", isIndexed, isOld), ui);
        complain.propertySectionId = getPropertySection(propertySections, getData(data, "propertySectionId", isIndexed, isOld), ui);
        complain.buildingHeight = StringUtil.toDouble(getData(data, "measurementHeight", isIndexed, isOld));;
        complain.floor = StringUtil.toInteger(StringUtil.remove(getData(data, "floor", isIndexed, isOld), "طبقه:"));
        complain.floorCount = StringUtil.toInteger(getData(data, "floorCount", isIndexed, isOld));
        complain.unitNumber = StringUtil.toInteger(StringUtil.remove(getData(data, "unitNumber", isIndexed, isOld), "واحد:"));

        try {
            complain.id = CommonRepoMongo.insert(complain);
            return complain;
        } catch (DatabaseException e) {
            log.error("! {}", siteCode, e);
            return null;
        }
    }

    private static Location getMeasurementLocation(Map<String, String> data, boolean isIndexed, boolean isOld) {
        String v = getData(data, "measurementLocation", isIndexed, isOld);
        if (StringUtil.isNotEmpty(v)) {
            Location l = stringToLocation(v);
            if (l != null && !l.isEmpty()) {
                return l;
            }
        }

        Double lng = StringUtil.toDouble(getData(data, "measurementLon", isIndexed, isOld));
        Double lat = StringUtil.toDouble(getData(data, "measurementLat", isIndexed, isOld));
        if (lng != null && lat != null) {
            Location l = new Location(lat, lng);
            return l.isEmpty() ? null : l;
        }
        return null;
    }

    private static ResponseMessage assignComplain(Params params, User assignor) throws ServerException, NoContentException, InputException {
        RadioMetricFlow flow = new RadioMetricFlow();
        flow.assigneeId = params.getLong("assigneeId");
        flow.complain = new RadioMetricComplain();
        flow.complain.id = params.getLong("complainId");

        if (ObjectUtil.isIdInvalid(flow.complain.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (complain.id)");
        }
        if (ObjectUtil.isIdInvalid(flow.assigneeId)) {
            throw new InputException(AppLangKey.INVALID_ASSIGNEE);
        }

        try {
            flow.complain = CommonRepoMongo.getFirst(flow.complain, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }


        Site site = new Site();
        site.id = flow.complain.siteId;
        try {
            site = CommonRepoMongo.getById(site, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().equal("site.code", site.code);
        try {
            CommonRepoMongo.getFirst(q);
            flow.reRadioMetric = true;
        } catch (NoContentException | DatabaseException ignore) {
            flow.reRadioMetric = false;
        }

        flow.complain.assignable = false;

        flow.site = site;
        flow.assignorId = assignor.getId();
        flow.provinceId = flow.complain.provinceId;
        flow.cityId = flow.complain.cityId;
        flow.siteAddress = site.address == null ? flow.complain.address : site.address;
        flow.siteLocation = site.location == null || site.location.isEmpty() ?  flow.complain.location : site.location;
        flow.sectors = site.sectors;

        flow.state = new ArrayList<>();
        flow.lastStateDateTime = new DateTime();
        flow.lastState = RadioMetricFlowState.Planned;

        // 1 - Pending
        State stateA = new State(RadioMetricFlowState.Pending, flow.complain.complainTime);
        flow.state.add(stateA);
        // 2- Planned
        User assignee = Services.get(ServiceDtoCache.class).getDto(User.class, flow.assigneeId);
        State stateB = new State(flow.lastState, params.getString("comments"));
        stateB.assignorId = assignor.id;
        stateB.assignorName = assignor.fullName;
        stateB.assigneeId = assignee.id;
        stateB.assigneeName = assignee.fullName;
        flow.state.add(stateB);

        RadioMetricComplain complainToUpdate = new RadioMetricComplain();
        complainToUpdate.id = flow.complain.id;
        complainToUpdate.assigneeId = flow.assigneeId;
        complainToUpdate.assignable = false;

        try {
            complainToUpdate.workFlowId = CommonRepoMongo.insert(flow);
            CommonRepoMongo.update(complainToUpdate);

            String imagePath = flow.complain.getImageFilePath(true);
            if (imagePath != null) {
                FileUtil.move(imagePath, flow.getPath() + flow.complain.getImageFilename());
            }

            return new ResponseMessage(VantarKey.INSERT_SUCCESS, complainToUpdate.workFlowId);
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.INSERT_FAIL);
        }
    }

    private static String getData(Map<String, String> data, String fieldName, boolean isIndexed, boolean isOld) {
        String v;
        if (!isIndexed) {
            String key = getDataKey(0, fieldName);
            v = data.get(key);
            if (StringUtil.isNotEmpty(v)) {
                v = v.trim();
                v = StringUtil.isEmpty(v) ? null : (isNotValid(v) ? null : v);
            }
            if (StringUtil.isEmpty(v)) {
                v = extractValue(data, fieldName);
            }
            return StringUtil.isEmpty(v) ? null : (isNotValid(v) ? null : v);
        }

        v = extractValue(data, fieldName);
        return StringUtil.isEmpty(v) ? null : (isNotValid(v) ? null : v);
    }

//    private static String getData(Map<String, String> data, String... key) {
//        for (String k : key) {
//            String v = data.get(k);
//            if (StringUtil.isNotEmpty(v)) {
//                v = v.trim();
//                return StringUtil.isEmpty(v) ? null : v;
//            }
//        }
//        return null;
//    }

    private static Long getSiteType(List<SiteType> siteTypes, String name) {
        if (name == null) {
            return null;
        }
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        for (SiteType s : siteTypes) {
            if (normalize(s.name.get("en")).equalsIgnoreCase(normalize(name))) {
                return s.id;
            }
        }
        return null;
    }

    private static Long getPropertySection(List<PropertySection> propertySections, String name, WebUi ui) {
        if (name == null) {
            return null;
        }
        name = StringUtil.remove(name, "مکان(آشپزخانه، پذیرایی، اتاق خواب، محل کار...):").trim();
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        for (PropertySection s : propertySections) {
            if (normalize(s.name).equalsIgnoreCase(normalize(name))) {
                return s.id;
            }
        }
        ui.addErrorMessage("PropertySection not found: " + name);
        return null;
    }

    private static Long getPropertyType(List<PropertyType> propertyTypes, String name, WebUi ui) {
        if (name == null) {
            return null;
        }
        name = StringUtil.remove(name, "نوع کاربری(مسکونی- تجاری/اداری):").trim();
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        for (PropertyType s : propertyTypes) {
            if (normalize(s.name).equalsIgnoreCase(normalize(name))) {
                return s.id;
            }
        }
        return null;
    }

    private static String normalize(String string) {
        return string == null ? "" : StringUtil.remove(string, '-', ',', '،', '_', '"', '\'', ' ');
    }

    private static Long getOperator(List<Operator> operators, String name1, String name2, WebUi ui) {
        String name = ((name1 == null ? "" : name1) + (name2 == null ? "" : name2)).trim();
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        for (Operator s : operators) {
            if (name.contains(s.name.get("fa")) || name.contains(s.name.get("en"))) {
                return s.id;
            }
        }
        return null;
    }

    private static RadioMetricFlow getWorkFlow(String siteCode, WebUi ui) {
        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().equal("site.code", siteCode);
        try {
            return CommonRepoMongo.getFirst(q, "en");
        } catch (NoContentException e) {
            ui.addErrorMessage("    " + siteCode + " flow and site not exists").write();
        } catch (DatabaseException e) {
            ui.addErrorMessage(siteCode + ": database error").write();
            log.error("! {}", siteCode, e);
        }
        return null;
    }

    private static Map<String, String> extractData(String docxFile, WebUi ui) {
        String command = "php -d memory_limit=512M " + Docx.class.getResource("/arta/app/docx/").getPath()
            + "toxml.php "
            + StringUtil.replace(docxFile, " ", "$$$$");

        FileUtil.giveAllPermissions(Docx.class.getResource("/arta/app/docx/").getPath());

        try {
            BufferedReader input = new BufferedReader(
                new InputStreamReader(
                    Runtime.getRuntime()
                        .exec(command)
                        .getInputStream()
                )
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null) {
                output.append(line).append("\n");
            }
            input.close();

            HtmlParser parser = new HtmlParser();
            parser.setHtml(fixText(output.toString()));

            Map<String, String> data = parser.getBySelector("p");
            if (data.isEmpty()) {
                data = parser.getBySelectorIx("p");
                data.put("__type", "1");
            }
            if (data.size() < 10) {
                log.error("! {}", command);
                ui.addErrorMessage(command).write();
            }
            return data;
        } catch (Exception e) {
            log.error("! {}", docxFile, e);
            ui.addErrorMessage(docxFile + ": could not extract data").write();
        }
        return null;
    }

    private void processFile(String directory, Callback callback) {
        String path = StringUtil.rtrim(directory, '/') + '/';
        try {
            Files.list(Paths.get(path))
                .filter(Files::isRegularFile)
                .forEach((file) -> callback.fileFound(path + file.getFileName().toString()));

            Files.list(Paths.get(path))
                .filter(Files::isDirectory)
                .forEach((file) -> processFile(path + file.getFileName().toString(), callback));

        } catch (IOException e) {
            log.error("! {}", path, e);
        }
    }

    private static Location stringToLocation(String str) {
        if (str == null) {
            return null;
        }
        String[] parts = StringUtil.split(str, ' ');
        Location location = new Location(StringUtil.toDouble(parts[0].trim()), StringUtil.toDouble(parts[1].trim()));
        return location.isEmpty() ? null : location;
    }

    private static User getTechnician(String name, WebUi ui) {
        User user = new User();
        QueryBuilder q = new QueryBuilder(user);
        q.condition().equal("fullName", name);
        try {
            return CommonRepoMongo.getFirst(q, "en");
        } catch (NoContentException e) {
            String[] parts = StringUtil.split(name, ' ');
            user.username = name;
            user.password = "abcd1234";
            user.firstName = parts[0].trim();
            if (parts.length > 1) {
                user.lastName = parts[1].trim();
            }
            user.fullName = name;
            user.accessStatus = AccessStatus.ENABLED;
            user.role = Role.TECHNICIAN;
            user.projectTypes = new ArrayList<>();
            user.projectTypes.add(ProjectType.RadioMetric);

            try {
                user.id = CommonRepoMongo.insert(user);
            } catch (DatabaseException databaseException) {
                ui.addErrorMessage("database error: " + name).write();
                log.error("! ", e);
            }

            ui.addMessage(name + ": user added").write();
            return user;

        } catch (DatabaseException e) {
            ui.addErrorMessage("database error: " + name).write();
            log.error("! ", e);
        }
        return null;
    }

    public static Map<String, String> getStatusData(String csv, WebUi ui) throws IOException, CsvValidationException {
        ui.addMessage("loading status data...").write();
        String[] record;
        CSVReader reader = new CSVReader(new FileReader(csv));
        Map<String, String> sites = new HashMap<>(39000);
        while ((record = reader.readNext()) != null) {
            sites.put(record[0].toUpperCase(), record[1]);
        }
        ui.addMessage("finished loading status data").write();
        return sites;
    }

    public static Map<String, SPCC> getSpData(String xlsxPath, Map<String, String> statuses, WebUi ui) throws IOException, CsvValidationException {
        ui.addMessage("loading SP data...").write();
        String[] record;
        CSVReader reader = new CSVReader(new FileReader(xlsxPath));
        Map<String, SPCC> sites = new HashMap<>(19000);
        while ((record = reader.readNext()) != null) {
            SP sp = new SP();
            sp.date = normalizeDate(record[1]);

            sp.address = fixText(StringUtil.remove(record[5], "Err:504").trim());
            if (sp.address.isEmpty()) {
                sp.address = null;
            }

            sp.densityAverage6min100 = StringUtil.toDouble(Persian.Number.toLatin(record[11]));
            sp.densityAverage6min150 = StringUtil.toDouble(Persian.Number.toLatin(record[12]));
            sp.densityAverage6min170 = StringUtil.toDouble(Persian.Number.toLatin(record[13]));

            sp.icnirp100 = StringUtil.toDouble(Persian.Number.toLatin(record[14]));
            sp.icnirp150 = StringUtil.toDouble(Persian.Number.toLatin(record[15]));
            sp.icnirp170 = StringUtil.toDouble(Persian.Number.toLatin(record[16]));

            sp.sector = record[17];
            sp.height = StringUtil.toDouble(Persian.Number.toLatin(record[18]));
            sp.type = record[19];

            record[4] = record[4].toUpperCase();

            sp.status = statuses.get(record[4]);
            if (sp.status == null) {
//                log.error("no status {}", record[4]);
                sp.status = "done";
            }

            String k = record[4] + "__" + sp.date;

            if (sites.containsKey(k)) {
                log.error("SP {}", k);
            }
            sites.put(k, sp);
        }
        ui.addMessage("finished loading SP data").write();
        return sites;
    }

    public static Set<String> getSpData(String xlsxPath) throws IOException, CsvValidationException {
        String[] record;
        CSVReader reader = new CSVReader(new FileReader(xlsxPath));
        Set<String> sites = new HashSet<>(19000);
        while ((record = reader.readNext()) != null) {
            sites.add(record[4]);
        }
        return sites;
    }

    public static Map<String, SPCC> getCcData(String xlsxPath, Map<String, String> statuses, WebUi ui) throws IOException, CsvValidationException {
        ui.addMessage("loading CC data...").write();
        String[] record;
        CSVReader reader = new CSVReader(new FileReader(xlsxPath));
        Map<String, SPCC> sites = new HashMap<>(19000);
        while ((record = reader.readNext()) != null) {
            CC cc = new CC();
            cc.date = normalizeDate(record[1]);

            cc.address = fixText(StringUtil.remove(record[5], "Err:504").trim());
            if (cc.address.isEmpty()) {
                cc.address = null;
            }

            cc.description = fixText(record[9]);
            if (StringUtil.isEmpty(cc.description)) {
                cc.description = null;
            }

            String x = fixText(StringUtil.remove(record[8], "شکایت", "می باشد", "شماره", "به", "مربوط", "-").trim());
            if (x.contains("آقای") || x.contains("خانم")) {
                cc.complainer = StringUtil.remove(x, "خانم", "آقای");
            }
            if (cc.description != null && (cc.description.contains("آقای") || cc.description.contains("خانم"))) {
                cc.complainer = StringUtil.remove(cc.description, "خانم", "آقای");
            }

            Integer ccc = StringUtil.toInteger(x);
            cc.ccnumber = ccc == null ? "0" : ccc.toString();

            cc.densityAverage6min100 = StringUtil.toDouble(Persian.Number.toLatin(record[12]));
            cc.densityAverage6min150 = StringUtil.toDouble(Persian.Number.toLatin(record[13]));
            cc.densityAverage6min170 = StringUtil.toDouble(Persian.Number.toLatin(record[14]));

            cc.icnirp100 = StringUtil.toDouble(Persian.Number.toLatin(record[15]));
            cc.icnirp150 = StringUtil.toDouble(Persian.Number.toLatin(record[16]));
            cc.icnirp170 = StringUtil.toDouble(Persian.Number.toLatin(record[17]));

            cc.sector = record[18];
            cc.height = StringUtil.toDouble(Persian.Number.toLatin(record[19]));
            cc.type = record[20];

            record[4] = record[4].toUpperCase();

            cc.status = statuses.get(record[4]);
            if (cc.status == null) {
                log.error("no status {}", record[4]);
                cc.status = "done";
            }

            //String k = record[4] + "__" + Persian.Number.toLatin(cc.ccnumber) + "__" + cc.date;
            String k = record[4] + "__" + cc.date;

            if (sites.containsKey(k)) {
                log.error("CC {}", k);
            }

            sites.put(k , cc);
        }
        ui.addMessage("finished loading CC data").write();
        return sites;
    }

    private static String fixText(String v) {
        return Persian.Number.toLatin(
            StringUtil.replace(
                StringUtil.replace(
                    StringUtil.replace(v, '\u200C', ' '), 'ي', 'ی'), 'ك', 'ک'));
    }

    private static String normalizeDate(String d) {
        try {
            if (StringUtil.isEmpty(d)) {
                return null;
            }
            d = Persian.Number.toLatin(d);
            d = StringUtil.replace(d, '.', '/');
            d = StringUtil.replace(d, '-', '/');
            String[] p = StringUtil.split(d, '/');
            if (p.length != 3) {
                return null;
            }
            String m = p[1];
            String y;
            String day;
            if (p[0].length() == 4) {
                y = p[0];
                day = p[2];
            } else if (p[2].length() == 4) {
                y = p[2];
                day = p[0];
            } else {
                int a = StringUtil.toInteger(p[0]);
                int b = StringUtil.toInteger(p[2]);
                if (a == 0) {
                    y = "1400";
                    day = p[2];
                } else if (b == 0) {
                    y = "1400";
                    day = p[0];
                } else if (a > 80) {
                    y = "13" + p[0];
                    day = p[2];
                } else {
                    y = "13" + p[2];
                    day = p[0];
                }
            }
            return y + '/' + (m.length() == 1 ? '0' + m : m) + '/' + (day.length() == 1 ? '0' + day : day);
        } catch (Exception x) {
            return null;
        }
    }

    private String docToDox(String docPath, WebUi ui) {
        docPath = StringUtil.replace(docPath, " ", "\\ ");
        String[] parts = StringUtil.split(docPath, '/');
        parts[parts.length-1] = "";
        String dir = CollectionUtil.join(parts, '/');
        String command = "lowriter run many  --headless --convert-to docx " + docPath
            + " --outdir " + dir;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);
        try {
            Process process = processBuilder.start();
            return docPath + 'x';
        } catch (IOException e) {
            log.error("! {}", docPath, e);
            ui.addErrorMessage(docPath + ": could not extract data").write();
        }

        ui.sleep(1000);

        return null;
    }

    private static boolean isNotValid(String value) {
        String[] exclude = new String[] {
            "تعالی",
            "تعال",
            "باسمه",
            "سند بررس",
            "فهرست مطالب",
            "..............................",
            "مشخصات سند اندازه گیری",
            "مشخصات سایت مورد شکایت",
            "نام اپراتور",
            "نام سایت",
            "(EIRP) توان تشعشعی آنتن",
            "N/A",
            "وجود دارد؟",
            "آیا در ",
            "□",
            "■",
            "نوع سایت",
            "آیا تا ",
            "پیوست ",
            "نحوه تکمیل جداول",
            "تایید نخواهد بود",
            "جداگانه تکمیل شود",
            "شماره 3 درج شود",
            "سالمندان و... است",
            " دانشگاه و... است",
            "4 نیز تکمیل شود",
            "استفاده می شود",
            "نشان می دهد، صورت گیرد",
            "مقدار اندازه گیری شده",
            " مردم در باند اندازه گیری ",
            "این صورت سازگار می باشد",
            "توضیحات:",
            "آدرس دقیق سایت",
            "شناسه سایت",
            "باند های فرکانس",
            "باند های فرکانس",
            "ارتفاع آنتن",
            "آیا در نزدیکی محل سایت مراکز درمانی وجود دارد؟",
            "□ بله ■ خیر",
            "در صورت پاسخ مثبت نام مرکز:",
            "آیا در نزدیکی محل سایت مراکز آموزشی وجود دارد؟",
            "□ بله ■ خیر",
            "در صورت پاسخ مثبت نام مرکز:",
            "نام اپراتور",
            " مشخصات دستگاه ",
            "نام دستگاه",
            "مدل",
            "شماره سریال دستگاه",
            "شرکت سازنده",
            "پروب ",
            "اعتبار کالیبراسیون",
            "مشخصات محل مورد اندازه",
            "جدول شماره ",
            "(متر)",
            "آدرس دقیق ",
            "موقعیت اندازه گیر",
            "کم ترین حد پرتو گی",
            "(µ w/cm2)",
            "مقدار اندازه گیر",
            "(درصد)",
            "وضعیت تشعشع",
            "سازگار/ناسازگار",
            "سانتیمتر از زمین",
            "سازگار",
            "اظهاریه",
            "مطابق ",
            "مشخصات کارشناسان",
            "اندازه گیر",
            "محل امضا",
            "تصویر دکل",
            "آنتن های همراه اول",
            "centercenter",
            "مشخصات تکمیل",
            "نام کارشناس",
            "نام تهیه ",
            "نام مسئول ",
            "تاریخ اندازه",
            "تاریخ گزارش",
            "طول جغراف",
            "عرض جغراف",
            "زاویه سمت",
            "زاویه ت",
            "تیلت",
        };
        for (String k : exclude) {
            if (value.contains(k)) {
                return true;
            }
        }
        return false;
    }

    private static String getDataKey(int i, String fieldName) {
        List<Map<String, String>> keys = new ArrayList<>(5);
        Map<String, String> keys1 = new HashMap<>();
        Map<String, String> keys2 = new HashMap<>();
        Map<String, String> keys3 = new HashMap<>();

        keys1.put("siteCode", "08C04CC0");
        keys2.put("siteCode", "35");
        keys3.put("siteCode", "34");

        keys1.put("siteAddress", "2D330FE0");
        keys2.put("siteAddress", "39");
        keys3.put("siteAddress", "38");

        keys1.put("siteLocation", "620AF6EB");

        keys1.put("measurementTime", "620AF6EB");

        keys1.put("measurementDate", "2ABAA7DC");
        keys2.put("measurementDate", "124");
        keys3.put("measurementDate", "122");

        keys1.put("measurementLocation", "026E2D95");

        keys1.put("reportData", "47EA9F12");

        keys1.put("frequencyBand", "3F226CF8");
        keys2.put("frequencyBand", "32");
        keys3.put("frequencyBand", "32");

        keys1.put("reporter", "60141E92");
        keys2.put("reporter", "119");
        keys3.put("reporter", "117");

        keys1.put("healthPhysics", "42C35673");
        keys2.put("healthPhysics", "121");
        keys3.put("healthPhysics", "119");

        keys1.put("comments", "66674DEC");
        keys2.put("comments", "63");
        keys3.put("comments", "62");

        keys1.put("siteFloor", "78F5BA62");

        keys1.put("technician", "04C10E0C");
        keys2.put("technician", "117");
        keys3.put("technician", "115");

        keys1.put("densityMax100", "1499F9B2");
   //     keys2.put("densityMax100", "");
     //   keys3.put("densityMax100", "104");

        keys1.put("densityAverage6min100", "5939AC75");
        //keys2.put("densityAverage6min100", "");
 //       keys3.put("densityAverage6min100", "106");

        keys1.put("densityMax150", "5A5E146E");
      //  keys2.put("densityMax150", "");
        //keys3.put("densityMax150", "99");

        keys1.put("densityAverage6min150", "42A1F5C3");
  //      keys2.put("densityAverage6min150", "");
    //    keys3.put("densityAverage6min150", "101");

        keys1.put("densityMax170", "1E96E5F1");
     //   keys2.put("densityMax170", "");
       // keys3.put("densityMax170", "94");

        keys1.put("densityAverage6min170", "4DCBCD7C");
///        keys2.put("densityAverage6min170", "");
   //     keys3.put("densityAverage6min170", "96");

        keys1.put("measurementHeight", "5A528977");
        keys2.put("measurementHeight", "84");

        keys1.put("complainAddress", "16F65F55");
        keys1.put("complainerName", "53962D4E");
        keys1.put("complainerMobile", "45587764");
        keys1.put("complainerPhone", "52669E61");
        keys1.put("complainerAddress", "16F65F55");
        keys1.put("propertyId", "6B77037C");
        keys1.put("propertySectionId", "073A025C");
        keys1.put("floor", "59A9CAE5");
        keys1.put("floorCount", "40904DB6");
        keys1.put("unitNumber", "02611FE0");

        keys1.put("horizontalDistanceFromLocation", "141D02DB");
        keys2.put("horizontalDistanceFromLocation", "82");

        keys1.put("deviceManufacturer", "51F076F5");
        keys2.put("deviceManufacturer", "73");
        keys1.put("deviceModel", "7A9DEBA7");
        keys2.put("deviceModel", "69");
        keys1.put("deviceSerialNumber", "11218E27");
        keys2.put("deviceSerialNumber", "71");
        keys1.put("deviceTitle", "3FD48776");
        keys2.put("deviceTitle", "67");
        keys1.put("deviceProbe", "34DA5815");
        keys2.put("deviceProbe", "75");

        keys1.put("deviceCalibrationExpire", "47BC0716");
        keys1.put("antennaHeight", "6887C2E4");
        keys3.put("antennaHeight", "43");

        keys1.put("azimuthA", "270EE3B0");
        keys1.put("mechanicalTiltA", "3555C323");
        keys1.put("electricalTiltA", "17076E64");
        keys1.put("azimuthB", "367C725B");
        keys1.put("mechanicalTiltB", "14E2C59E");
        keys1.put("electricalTiltB", "7235735A");
        keys1.put("azimuthC", "2970274A");
        keys1.put("mechanicalTiltC", "6B8A9E7D");
        keys1.put("electricalTiltC", "40A54653");


        keys1.put("proximityHealth", "616CD089");
        keys2.put("proximityHealth", "47");

        keys1.put("proximityEducation", "03CD42B8");
        keys2.put("proximityEducation", "49");

        keys.add(keys1);
        keys.add(keys2);
        keys.add(keys3);
        return keys.get(i).get(fieldName);
    }

    public static String extractValue(Map<String, String> data, String key) {
        String pKey = null;
        switch (key) {
            case "frequencyBand":
                pKey = "باند ها";
                break;
            case "siteCode":
                pKey = "شناسه سایت";
                break;
            case "siteAddress":
                pKey = "آدرس دقیق سایت";
                break;
            case "antennaHeight":
                pKey = "ارتفاع آنتن از سطح زمین ";
                break;
            case "comments":
                pKey = "توضیحات";
                break;
            case "deviceManufacturer":
                pKey = "شرکت سازنده";
                break;
            case "deviceModel":
                pKey = "مدل";
                break;
            case "deviceSerialNumber":
                pKey = "شماره سریال دستگاه";
                break;
            case "deviceTitle":
                pKey = "نام دستگاه";
                break;
            case "deviceProbe":
                pKey = "پروب مورد استفاده";
                break;
            case "deviceCalibrationExpire":
                pKey = " اعتبار کالیبراسیون";
                break;
            case "horizontalDistanceFromLocation":
                pKey = "فاصله افقی محل اندازه گیری از ";
                break;
            case "measurementHeight":
                pKey = "ارتفاع آنتن از محل اندازه ";
                break;
            case "spotAddress":
                pKey = "آدرس دقیق محل اندازه گیری";
                break;
            case "technician":
                pKey = "نام کارشناس(ان) اندازه گیری";
                break;
            case "reporter":
                pKey = "نام تهیه کننده(گان) گزارش";
                break;
            case "healthPhysics":
                pKey = "نام مسئول فیزیک بهداشت";
                break;
            case "measurementDate":
                pKey = "تاریخ اندازه گیری";
                break;
            case "reportDate":
                pKey = "تاریخ گزارش";
                break;
            case "measurementTime":
                pKey = "ساعت اندازه گیری";
                break;
            case "measurementLon":
                pKey = "طول جغرافیایی محل نصب دکل";
                break;
            case "measurementLat":
                pKey = "عرض جغرافیایی محل نصب دکل";
                break;
            case "selectedSector":
                for (String v : data.values()) {
                    if (v.contains("مشخصات محل مورد اندازه گیری")) {
                        return v;
                    }
                }
                break;
            case "avg170":
                pKey = "ارتفاع 170 سانتیمتر از زمین";
                break;
            case "avg150":
                pKey = "ارتفاع 150 سانتیمتر از زمین";
                break;
            case "avg100":
                pKey = "ارتفاع 100 سانتیمتر از زمین";
                break;
            default:
                return null;
        }

        boolean getNext = false;
        for (String v : data.values()) {
            if (getNext) {
                return v;
            }
            if (v.contains(pKey)) {
                getNext = true;
            }
        }
        return null;
    }


    private static interface SPCC {
        String toString();
        String getDate();
        String getAddress();
        String getCcnumber();
        String getDescription();
        String getComplainer();
        Double getDensityAverage6min100();
        Double getIcnirp100();
        Double getDensityAverage6min150();
        Double getIcnirp150();
        Double getDensityAverage6min170();
        Double getIcnirp170();
        String getSector();
        Double getHeight();
        String getType();
        String getStatus();
    }


    private static class SP implements SPCC {
        public String date;
        public String address;
        public Double densityAverage6min100;
        public Double icnirp100;
        public Double densityAverage6min150;
        public Double icnirp150;
        public Double densityAverage6min170;
        public Double icnirp170;
        public String sector;
        public Double height;
        public String type;
        public String status;

        public String toString() {
            return ObjectUtil.toString(this);
        }

        public String getDate() {
            return date;
        }

        public String getAddress() {
            return address;
        }

        public String getCcnumber() {
            return "0";
        }

        public String getDescription() {
            return null;
        }

        public String getComplainer() {
            return shahsanam.fullName;
        }

        public Double getDensityAverage6min100() {
            return densityAverage6min100;
        }

        public Double getIcnirp100() {
            return icnirp100;
        }

        public Double getDensityAverage6min150() {
            return densityAverage6min150;
        }

        public Double getIcnirp150() {
            return icnirp150;
        }

        public Double getDensityAverage6min170() {
            return densityAverage6min170;
        }

        public Double getIcnirp170() {
            return icnirp170;
        }

        public String getSector() {
            return sector;
        }

        public Double getHeight() {
            return height;
        }

        public String getType() {
            return type;
        }

        public String getStatus() {
            return status;
        }
    }


    private static class CC implements SPCC  {

        public String date;
        public String address;
        public String ccnumber;
        public String description;
        public String complainer;
        public Double densityAverage6min100;
        public Double icnirp100;
        public Double densityAverage6min150;
        public Double icnirp150;
        public Double densityAverage6min170;
        public Double icnirp170;
        public String sector;
        public Double height;
        public String type;
        public String status;

        public String toString() {
            return ObjectUtil.toString(this);
        }

        public String getDate() {
            return date;
        }

        public String getAddress() {
            return address;
        }

        public String getCcnumber() {
            return ccnumber;
        }

        public String getDescription() {
            return description;
        }

        public String getComplainer() {
            return complainer;
        }

        public Double getDensityAverage6min100() {
            return densityAverage6min100;
        }

        public Double getIcnirp100() {
            return icnirp100;
        }

        public Double getDensityAverage6min150() {
            return densityAverage6min150;
        }

        public Double getIcnirp150() {
            return icnirp150;
        }

        public Double getDensityAverage6min170() {
            return densityAverage6min170;
        }

        public Double getIcnirp170() {
            return icnirp170;
        }

        public String getSector() {
            return sector;
        }

        public Double getHeight() {
            return height;
        }

        public String getType() {
            return type;
        }

        public String getStatus() {
            return status;
        }
    }


    private interface Callback {

        void fileFound(String path);
    }
}