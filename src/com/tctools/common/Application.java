package com.tctools.common;

import com.tctools.business.dto.user.User;
import com.tctools.business.model.user.AuthModel;
import com.tctools.business.service.locale.LocaleService;
import com.vantar.admin.model.document.AdminDocument;
import com.vantar.common.Settings;
import com.vantar.database.nosql.mongo.MongoConnection;
import com.vantar.exception.*;
import com.vantar.http.Ssl;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.service.cache.ServiceDtoCache;
import com.vantar.service.messaging.ServiceMessaging;
import com.vantar.web.Response;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.*;
import javax.servlet.*;
import java.util.*;


public class Application implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(Application.class);


    @Override
    public void contextInitialized(ServletContextEvent e) {
        log.info("> Initializing application\n\n\n");

        Settings.setConfig(Config.class, ConfigFactory.create(Config.class));
        Settings.setTune(Tune.class, ConfigFactory.create(Tune.class));
        log.info(" >> settings set");

        Response.setJsonError();
        log.info(" >> error responses set to be JSON");
        Response.setAllowOrigin("*");
        log.info(" >> allowed origin = '*'");
        ServiceAuth.setUserClass(User.class);

        DtoInfo.start();
        log.info(" >> dto info loaded");
        Ssl.disable();
        log.info(" >> disabled SSL connection checking");
        LocaleService.start(Settings.locale());
        log.info(" >> localization started");


        Services.setEvents(new Services.Event() {
            @Override
            public void beforeStart(Set<Class<?>> dependencies) {
                Services.connectToDataSources(dependencies);
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
                        if (type == Param.MESSAGE_DATABASE_UPDATED && "User".equals(message.getString())) {
                            try {
                                Services.getService(ServiceAuth.class)
                                    .updateOnlineUsers((List) Services.get(ServiceDtoCache.class).getList(User.class));
                                log.info(" >> online user data updated");
                            } catch (ServiceException e) {
                                log.error("! failed to update online user data", e);
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
                    log.error("! ", e);
                }
            }

            @Override
            public void afterStop() {
                MongoConnection.shutdown();
            }
        });
        Services.startServer();

        AdminDocument.createDtoDocument();

        log.info("\n\n< Initializing application\n\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("\n\n> Stopping application\n");
        Services.stop();
        log.info("\n\n< Stopping application\n\n");
    }
}