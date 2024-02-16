package com.tctools.web.patch;

import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.common.Param;
import com.vantar.admin.model.index.Admin;
import com.vantar.business.ModelMongo;
import com.vantar.database.dto.Dto;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.*;
import com.vantar.util.file.DirUtil;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;


@WebServlet({
    "/patch/hse/compare",
})
public class HseController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(HseController.class);

    public void hseCompare(Params params, HttpServletResponse response) throws FinishException, DatabaseException, NoContentException, IOException, AuthException {
if (true) {

    throw new AuthException(VantarKey.WRONG_PASSWORD, "mehdi", "torbi");
}


        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        DirUtil.browse(Param.HSE_AUDIT_FILES, new DirUtil.Callback() {
            @Override
            public void found(File file) {
                String[] files = DirUtil.getDirectoryFiles(file.getAbsolutePath());
                String siteCode = file.getName().trim().toUpperCase();
                QueryBuilder q = new QueryBuilder(new HseAuditQuestionnaire());
                q.condition().equal("site.code", siteCode);
                try {
                    List<Dto> data = ModelMongo.getData(q);
                    for (Dto dto : data) {
                        HseAuditQuestionnaire hse = (HseAuditQuestionnaire) dto;
                        if (HseAuditFlowState.Pending.equals(hse.lastState)) {
                            ui.addMessage(siteCode + " > pending > files=" + files.length).write();
                        }
                    }
                } catch (NoContentException e) {
                    ui.addMessage(siteCode + " > no record").write();
                } catch (VantarException e) {
                    log.error(" !  {}", siteCode, e);
                }

            }
        });


        ui.finish();
    }
}