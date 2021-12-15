package com.tctools.web.ui.project.hseaudit;

import com.tctools.business.dto.project.container.ProjectType;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.hseaudit.QuestionModel;
import com.vantar.exception.*;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;


@WebServlet({
    "/ui/hse/audit/questions/get",
})
public class QuestionController extends RouteToMethod {

    public void hseAuditQuestionsGet(Params params, HttpServletResponse response) throws AuthException, ServiceException {
        User user = (User) Services.get(ServiceAuth.class).permitAccess(params, Role.MANAGER, Role.ENGINEER, Role.TECHNICIAN);
        user.projectAccess(ProjectType.HseAudit);
        Response.writeJson(response, QuestionModel.get(params));
    }
}