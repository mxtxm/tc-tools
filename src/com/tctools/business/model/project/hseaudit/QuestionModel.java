package com.tctools.business.model.project.hseaudit;

import com.tctools.business.dto.project.hseaudit.HseAuditQuestion;
import com.vantar.exception.ServiceException;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.web.Params;
import java.util.*;


public class QuestionModel {

    public static List<HseAuditQuestion.Viewable> get(Params params) {
        List<HseAuditQuestion.Viewable> items = new ArrayList<>();
        try {
            for (HseAuditQuestion question : Services.getService(ServiceDtoCache.class).getList(HseAuditQuestion.class)) {
                HseAuditQuestion.Viewable viewable = new HseAuditQuestion.Viewable();
                viewable.set(question, params.getLang());
                items.add(viewable);
            }
        } catch (ServiceException e) {
            return items;
        }

        items.sort((q1, q2) -> q1.order == null || q2.order == null ? 0 : q1.order.compareTo(q2.order));

        return items;
    }
}