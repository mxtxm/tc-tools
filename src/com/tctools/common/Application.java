package com.tctools.common;

import com.tctools.business.dto.user.User;
import com.tctools.business.model.user.AuthModel;
import com.tctools.business.service.locale.LocaleService;
import com.vantar.common.*;
import com.vantar.exception.*;
import com.vantar.http.Ssl;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.service.log.ServiceLog;
import com.vantar.service.messaging.ServiceMessaging;
import com.vantar.util.object.ClassUtil;
import com.vantar.web.*;
import org.aeonbits.owner.ConfigFactory;
import javax.servlet.*;
import java.util.List;


public class Application implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        ServiceLog.log.info("----> Initializing radiometric\n\n");

        Settings.setConfig(Config.class, ConfigFactory.create(Config.class));
        Settings.setTune(Tune.class, ConfigFactory.create(Tune.class));
        ServiceLog.log.info(" > settings loaded");

        Response.setJsonError();
        ServiceLog.log.info(" > error responses set to be JSON");
        Response.setAllowOrigin("*");
        ServiceLog.log.info(" > allowed origin = '*'");
        ServiceAuth.setUserClass(User.class);

        Params.start();
        ServiceLog.log.info(" > serverUpCount={}", Params.serverUpCount);
        DtoInfo.start();
        ServiceLog.log.info(" > dto info loaded");
        Ssl.disable();
        ServiceLog.log.info(" > disabled SSL connection checking");
        LocaleService.start(Settings.locale());
        ServiceLog.log.info(" > localization started");

        ClassUtil.checkControllers("com.tctools.web.ui");

        Services.setEvents(new Services.Event() {
            @Override
            public void beforeStart() {

            }

            @Override
            public void beforeStop() {

            }

            @Override
            public void afterStart() {
                // > > > messaging service
                Services.messaging.setEvent(new ServiceMessaging.Event() {
                    @Override
                    @SuppressWarnings({"unchecked", "rawtypes"})
                    public void onReceive(int type, ServiceMessaging.Message message) {
                        if (type == Param.MESSAGE_DATABASE_UPDATED) {
                            if ("User".equals(message.getString())) {
                                Services.get(ServiceAuth.class)
                                    .updateOnlineUsers((List) ServiceDtoCache.asList(User.class));
                                ServiceLog.log.info(" >> online user data updated");
                            }
                        }
                    }

                    @Override
                    public void onMessageQueueFail(String queue) {

                    }
                });

                // > > > auth service
                try {
                    Services.getService(ServiceAuth.class)
                        .restoreFromBackup()
                        .setEvent(AuthModel::getUserForAuth)
                        .startupSignin(User.getTemporaryRoot());
                } catch (ServiceException | AuthException e) {
                    ServiceLog.log.error(" ! ", e);
                }
            }

            @Override
            public void afterStop() {

            }
        });
        Services.startServices();

        ServiceLog.log.info("\n\n<---- Initializing radiometric\n\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServiceLog.log.info("\n\n----> Stopping radiometric\n");
        Services.stopServices();
        ServiceLog.log.info("\n\n<---- Stopping radiometric\n\n");
    }
}