package com.tctools.common;

import com.vantar.common.Settings;
import com.vantar.database.common.BackupSettings;
import com.vantar.database.nosql.elasticsearch.ElasticConfig;
import com.vantar.database.nosql.mongo.MongoConfig;
import com.vantar.database.sql.SqlConfig;
import com.vantar.locale.LocaleSettings;
import com.vantar.queue.QueueConfig;


@org.aeonbits.owner.Config.Sources({
    "file:/opt/tc-tools/config.properties",
    "classpath:/config.properties"
})
public interface Config extends Settings.Common, Settings.CommonConfig,
    MongoConfig, SqlConfig, QueueConfig, ElasticConfig, BackupSettings, LocaleSettings {

}