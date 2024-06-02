package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.vantar.database.common.Db;
import com.vantar.database.query.*;
import com.vantar.exception.*;
import com.vantar.util.collection.CollectionUtil;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.xlsx.*;
import com.vantar.web.Params;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class ExportUserModel {

    public static void downloadXlsx(Params params, HttpServletResponse response) throws VantarException {
        DateTime from;
        DateTime to;
        try {
            from = new DateTime("2023-06-20");
            to = new DateTime("2024-04-01");
        } catch (DateTimeException e) {
            return;
        }

        Xlsx.Config xlsx = new Xlsx.Config(
            response,
            "import-project-structure.xlsx",
            "Project structure"
        );

        xlsx.addWriteEvent("Project structure", (context) -> {
            XlsxStyledBase.setHeader(context);
            Xlsx.setCell(context, "Site");
            Xlsx.setCell(context, "Last State");
            Xlsx.setCell(context, "Assignor");
            Xlsx.setCell(context, "State Assignor");
            Xlsx.setCell(context, "Date");

            QueryBuilder q = new QueryBuilder(new RadioMetricFlow.Viewable());
            q.condition(QueryOperator.OR)
                .equal("assignorId", 19)
                .equal("state.assignorId", 19);

            XlsxStyledBase.setNormalCenter(context);

            Db.modelMongo.forEach(q, dto -> {
                RadioMetricFlow.Viewable flow = (RadioMetricFlow.Viewable) dto;

                if (flow.assignor == null || flow.assignDateTime == null) {
                    return true;
                }

                List<DateTime> d = new ArrayList<>();
                String assignor;
                if (flow.assignor.id == 19L) {
                    d.add(flow.assignDateTime);
                    assignor = "yes";
                } else {
                    assignor = "no";
                }

                List<String> s = new ArrayList<>();
                for (State state : flow.state) {
                    if (state.assignorId != null && state.assignorId == 19) {
                        if (state.dateTime != null) {
                            d.add(state.dateTime);
                        }
                        s.add(state.state.name());
                    }
                }

                boolean dateRange = false;
                List<String> dd = new ArrayList<>();
                for (DateTime dt : d) {
                    dd.add(dt.formatter().getDatePersian());
                    if (dt.isBetweenOrEqual(from, to)) {
                        dateRange = true;
                    }
                }
                if (!dateRange) {
                    return true;
                }

                context.nextRow();
                Xlsx.setCell(context, flow.site.code);
                Xlsx.setCell(context, flow.lastState);
                Xlsx.setCell(context, assignor);
                Xlsx.setCell(context, CollectionUtil.join(s, ", "));
                Xlsx.setCell(context, CollectionUtil.join(dd, ", "));
                return true;
            });
        });

        Xlsx.create(xlsx);
    }
}
