package com.tctools.web.ui.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.project.hseaudit.HseAuditQuestionSignificance;
import com.tctools.business.dto.user.*;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.util.object.EnumUtil;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/hse/audit/question/significance/get",
})
public class QuestionSignifanceController extends RouteToMethod {

    public void hseAuditQuestionSignificanceGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, EnumUtil.getEnumValues(HseAuditQuestionSignificance.values()));
    }
}