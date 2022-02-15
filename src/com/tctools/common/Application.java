package com.tctools.common;

import com.tctools.business.dto.user.User;
import com.tctools.business.repo.user.UserRepo;
import com.tctools.business.service.locale.LocaleService;
import com.vantar.admin.model.AdminDocument;
import com.vantar.common.Settings;
import com.vantar.database.nosql.mongo.MongoConnection;
import com.vantar.exception.ServiceException;
import com.vantar.http.Ssl;
import com.vantar.service.Services;
import com.vantar.service.auth.ServiceAuth;
import com.vantar.service.log.LogEvent;
import com.vantar.service.messaging.ServiceMessaging;
import com.vantar.web.Response;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.*;
import javax.servlet.*;
import java.util.Set;


public class Application implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(Application.class);


    @Override
    public void contextInitialized(ServletContextEvent e) {
        Settings.setConfig(Config.class, ConfigFactory.create(Config.class));
        Settings.setTune(Tune.class, ConfigFactory.create(Tune.class));

        log.info("> Initializing application\n\n\n");

        DtoInfo.start();
        log.info("loaded dto info");

        Ssl.disable();
        log.info("disabled SSL connection checking");

        LocaleService.start(Settings.locale());
        log.info("started localization");

        Response.setJsonError();
        Response.setAllowOrigin("*");

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
                    public void onReceive(int type, ServiceMessaging.Message message) {

                    }

                    @Override
                    public void onMessageQueueFail(String queue) {
                        queueFail(getClass(), ServiceMessaging.QUEUE_NAME_MESSAGE, 0);
                    }
                });

                // > > > auth service
                try {
                    Services.get(ServiceAuth.class)
                  /////      .restoreTokens()
                        .startupSignin(User.getTemporaryRoot())
                        .setEvent(username -> {
                            UserRepo repo = new UserRepo();
                            return repo.getUserForAuth(username);
                        });
                } catch (ServiceException e) {
                    log.error("! > > > ", e);
                }
            }

            @Override
            public void afterStop() {
                MongoConnection.shutdown();
            }
        });
        Services.start();

        AdminDocument.createDtoDocument();

        log.info("\n\n< Initializing application\n\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("\n\n> Stopping application\n");
        Services.stop();
        log.info("\n\n< Stopping application\n\n");
    }

    public static void queueFail(Class<?> clazz, String queueName, int workerId) {
        LogEvent.fatal(clazz, "queue(" + queueName + ", "  + workerId + ") failed - restarting all services...");
        Services.stop();
        Services.start();
    }
}