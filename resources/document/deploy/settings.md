# Settings #

* **config.properties** this file is the default main system config file, to override the settings in this file,
  copy this file into "/opt/tc-tools/config.properties" and change the settings there. (see "settings.md")
* **tune.properties** this file is the default additional system config file, to override the settings in this file,
  copy this file into "/opt/tc-tools/tune.properties" and change the settings there. (see "settings.md")
* **log4j2.xml** this file is the log system config file, to change log stages and log files update this file before
  building the project (see "log.md")


## config.properties ##
Main settings
    
    title=TC-Tools 
    package=com.tctools     # do not change this value
    is.local=false          # set to false for production
    locale.default=en
    documents.dir=/opt/tc-tools/documents/
    web.log.request=false   # set to true only for debug, true=database log will  grow in size and performance will decrease  
    web.log.response=false  # set to true only for debug, true=database log will  grow in size and performance will decrease

    # enabling/disabling services
    service.dependencies=\
        com.vantar.database.nosql.mongo.MongoConnection
    service.enabled.dto.cache=1
    service.enabled.auth=2
    service.enabled.backup=3
    service.enabled.scheduler=4
    service.enabled.user.action.log=5

    # nosql database settings
    mongo.hosts=localhost:27017
    mongo.database=tctools
    mongo.user=
    mongo.password=
    mongo.connect.timeout=60000
    mongo.socket.timeout=60000
    mongo.server.selection.timeout=60000

## tune.properties ##
Service settings

    # auth
    service.auth.package=com.vantar.service.auth     # do not change
    service.auth.on.end.set.null=false               # do not change
    service.auth.token.check.interval.min=15         # session expire check intervals 
    service.auth.token.expire.min=2880               # session expire time after user's last action
    service.auth.backup.path=/opt/tc-tools/backup/   # backup directory to create session backup
    
   
    # cache
    service.dto.cache.package=com.vantar.service.cache # do not change
    service.dto.cache.on.end.set.null=true             # do not change
    
    
    # backup
    service.backup.package=com.vantar.service.backup # do not change
    service.backup.on.end.set.null=true              # do not change
    service.backup.dbms=MONGO                        # do not change
    service.backup.interval.hour=12                  # auto backup creation interval
    service.backup.path=/opt/tc-tools/backup/        # backup directory to create database backup
    
    
    # scheduler
    # to define jobs (like cron jobs)
    service.scheduler.package=com.vantar.service.scheduler # do not change
    service.scheduler.on.end.set.null=true                 # do not change
    service.scheduler.schedule=\
        com.tctools.business.model.project.radiometric.workflow.export.MetricTypeReport.cacheCount,3:10,repeat;\
        com.tctools.business.model.project.radiometric.workflow.export.StateReport.cacheMonthlyOverview,2:50,repeat;\
        com.tctools.business.model.project.radiometric.workflow.export.TechReport.cacheStatistics,3:0,repeat;\
        com.tctools.business.model.project.radiometric.workflow.export.ProvinceReport.cacheStatistics,3:15,repeat
    
    
    # user action log
    service.user.action.log.package=com.vantar.service.log  # do not change
    service.user.action.log.on.end.set.null=true            # do not change
    service.user.action.log.delayed.store.enabled=false     # set to true if rabbitmq is installed and registered to increase performance
    service.user.action.log.dbms=MONGO                      # log repo
    service.user.action.log.insert.interval.min=10          # intervals which logs are written to the database
