package com.tctools.web.patch;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.model.project.radiometric.workflow.Measurement;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonModelMongo;
import com.vantar.database.common.ValidationError;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.util.file.FileUtil;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@WebServlet({
    "/patch/radiometric/measurement/redo",
})
public class MeasurementController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(MeasurementController.class);


    public void radiometricMeasurementRedo(Params params, HttpServletResponse response) throws FinishException, VantarException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        String siteCode = params.getString("siteCode");
        Long id = params.getLong("id");

        CommonModelMongo.forEach(new RadioMetricFlow(), dto -> {
            RadioMetricFlow flow = (RadioMetricFlow) dto;
            if (siteCode != null && !flow.site.code.equals(siteCode)) {
                return;
            }
            if (id != null && !flow.id.equals(id)) {
                return;
            }
            try {

                String csv100 = flow.getPath() + flow.site.code + "__100CM.csv";
                String csv150 = flow.getPath() + flow.site.code + "__150CM.csv";
                String csv170 = flow.getPath() + flow.site.code + "__170CM.csv";

                List<ValidationError> errors = new ArrayList<>(10);
                boolean hasCsv = false;

                if (FileUtil.exists(csv100)) {
                    hasCsv = true;
                    Measurement measurement = new Measurement();
                    measurement.applyCsv(csv100, flow, "100", errors);
                    try {
                        measurement.createOkExcel(csv100);
                    } catch (ServerException ignore) {

                    }
                }
                if (FileUtil.exists(csv150)) {
                    hasCsv = true;
                    Measurement measurement = new Measurement();
                    measurement.applyCsv(csv150, flow, "150", errors);
                    try {
                        measurement.createOkExcel(csv150);
                    } catch (ServerException ignore) {

                    }
                }
                if (FileUtil.exists(csv170)) {
                    hasCsv = true;
                    Measurement measurement = new Measurement();
                    measurement.applyCsv(csv170, flow, "170", errors);
                    try {
                        measurement.createOkExcel(csv170);
                    } catch (ServerException ignore) {

                    }
                }

                if (hasCsv && errors.isEmpty()) {
                    try {
                        CommonModelMongo.update(dto);
                        ui.addMessage(flow.site.code).write();
                    } catch (InputException | ServerException e) {
                        ui.addErrorMessage(e).write();
                    }
                }
                ui.addMessage(flow.site.code).write();
            } catch (Exception e) {
                ui.addErrorMessage(e).write();
            }
        });

        ui.addMessage("finished!").finish();
    }
}