package com.tctools.web.patch;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonModelMongo;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.*;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/patch/radio/state",
})
public class RadioController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(RadioController.class);

    public void radioState(Params params, HttpServletResponse response) throws FinishException, VantarException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
        q.condition().equal("lastState", RadioMetricFlowState.Planned.name());

        CommonModelMongo.forEach(q, dto -> {
            RadioMetricFlow flow = (RadioMetricFlow) dto;
            flow.lastState = RadioMetricFlowState.Pending;
            flow.assignorId = null;
            flow.assigneeId = null;
            flow.assignDateTime = null;
            flow.measurementDateTime = null;
            CommonModelMongo.update(flow);
            ui.addMessage(flow.site.code).write();
        });


        ui.finish();
    }
}