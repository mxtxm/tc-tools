package com.tctools.web.ui.project.container;

import com.tctools.business.dto.system.Settings;
import com.vantar.exception.ServiceException;
import com.vantar.service.Services;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.web.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@WebServlet({
    "/ui/map/last/aras",
})
public class MapController extends RouteToMethod {

    public void mapLastAras(Params params, HttpServletResponse response) throws ServiceException {
        List<Settings> items = Services.get(ServiceDtoCache.class).getList(Settings.class);
        if (items != null && !items.isEmpty()) {
            for (Settings item : items) {
                if (item.key.equals(Settings.KEY_ARAS_UPDATE)) {
                    Response.writeString(response, item.value);
                    return;
                }
            }
        }
        Response.writeString(response, "unknown");
    }

}