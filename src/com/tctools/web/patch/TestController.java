package com.tctools.web.patch;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire;
import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.vantar.admin.model.index.Admin;
import com.vantar.business.ModelMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.slf4j.*;
import javax.management.MBeanServer;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;


@WebServlet({
    "/test/t",
    "/test/index",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class TestController extends RouteToMethod {

    public enum State {

        PENDING("Pending", false, true),;

        public final String title;
        public String xx;

        State(String title, boolean b, boolean b1) {
            this.title = title;
        }

        public void setXx() {
            xx = "mehdi";
        }

    }



    public static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Access("ROOT")
    public void t(Params params, HttpServletResponse response) throws VantarException, FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        ModelMongo.forEach(new RadioMetricFlow(), new QueryResultBase.EventForeach() {
            @Override
            public void afterSetData(Dto dto) throws VantarException {
                RadioMetricFlow flow = (RadioMetricFlow) dto;
                if (flow.imageUrls != null) {
                    for (String path : flow.imageUrls.values()) {
                        path = StringUtil.replace(path, "/static/", "/opt/tc-tools/files/");
                        if (!FileUtil.exists(path)) {
                            ui.addMessage(flow.site.code).write();
                            return;
                        }
                    }
                }
            }

        });


        ui.addMessage("finish").finish();
        //dumpHeap("/opt/tc-tools.hprof", true);
    }

    public static void dumpHeap(String filePath, boolean live) throws IOException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        HotSpotDiagnosticMXBean mxBean = ManagementFactory.newPlatformMXBeanProxy(
            server, "com.sun.management:type=HotSpotDiagnostic", HotSpotDiagnosticMXBean.class);
        mxBean.dumpHeap(filePath, live);
    }

    public void index(Params params, HttpServletResponse response) throws VantarException, FinishException {
        if (true) return;


        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);
        ModelMongo.forEach(new RadioMetricComplain(), new QueryResultBase.EventForeach() {
            @Override
            public void afterSetData(Dto dto) throws VantarException {
                RadioMetricComplain complain = (RadioMetricComplain) dto;
                QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
                q.condition().equal("complain.id", complain.id);

                try {
                    RadioMetricFlow r = ModelMongo.getFirst(q);
                    complain.workFlowId = r.id;
                    complain.assigneeId = r.assigneeId;
                    complain.assignTime = r.assignDateTime;
                    complain.assignable = false;
                } catch (NoContentException e) {
                    complain.workFlowId = null;
                    complain.assigneeId = null;
                    complain.assignTime = null;
                    complain.assignable = true;
                }
                ModelMongo.update(complain);
                ui.addMessage("updated").write();
            }
        });

ui.addMessage("finish").finish();
        if (true) return;



        //List<String> oldCodes = StringUtil.splitToList(FileUtil.getFileContent("/home/lynx/downloads/radiometrics-missing"), '\n');
        List<String> oldCodes = StringUtil.splitToList(FileUtil.getFileContent("/home/lynx/downloads/hse-missing"), '\n');

        //QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
//        QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
//        q.condition().inString("site.code", oldCodes);
//
//        log.error(">>>> {}", ModelMongo.getData(q).size());
//
//        MongoBackup.dumpQuery("/home/lynx/downloads/mongo-missing-hse.zip", q, ui);

    }

    public void indexY(Params params, HttpServletResponse response) throws VantarException, FinishException {
        Set<String> oldCodes = StringUtil.splitToSet(FileUtil.getFileContent("/home/lynx/downloads/radiometrics"), "\n");
        ModelMongo.forEach(new RadioMetricFlow(), new QueryResultBase.EventForeach() {
            @Override
            public void afterSetData(Dto dto) {
                String code = ((RadioMetricFlow) dto).site.code;
                oldCodes.remove(code);
                //ui.addMessage(((RadioMetricFlow) dto).site.code).write();
            }

        });
        FileUtil.write("/home/lynx/downloads/radiometrics-missing", CollectionUtil.join(oldCodes, "\n"));

        Set<String> oldCodesx = StringUtil.splitToSet(FileUtil.getFileContent("/home/lynx/downloads/hse"), "\n");
        ModelMongo.forEach(new HseAuditQuestionnaire(), new QueryResultBase.EventForeach() {
            @Override
            public void afterSetData(Dto dto) {
                String code = ((HseAuditQuestionnaire) dto).site.code;
                oldCodesx.remove(code);
                //ui.addMessage(((RadioMetricFlow) dto).site.code).write();
            }

        });
        FileUtil.write("/home/lynx/downloads/hse-missing", CollectionUtil.join(oldCodesx, "\n"));
    }

    public void indexX(Params params, HttpServletResponse response) throws VantarException, FinishException {
        List<String> codes = new ArrayList<>(50000);
        ModelMongo.forEach(new RadioMetricFlow(), new QueryResultBase.EventForeach() {
            @Override
            public void afterSetData(Dto dto) {
                codes.add(((RadioMetricFlow) dto).site.code);
                //ui.addMessage(((RadioMetricFlow) dto).site.code).write();
            }
        });
        FileUtil.write("/home/lynx/downloads/radiometrics", CollectionUtil.join(codes, "\n"));


        List<String> codesX = new ArrayList<>(50000);
        ModelMongo.forEach(new HseAuditQuestionnaire(), new QueryResultBase.EventForeach() {
            @Override
            public void afterSetData(Dto dto) {
                codesX.add(((HseAuditQuestionnaire) dto).site.code);
                //ui.addMessage(((RadioMetricFlow) dto).site.code).write();
            }

        });
        FileUtil.write("/home/lynx/downloads/hse", CollectionUtil.join(codesX, "\n"));

    }


}