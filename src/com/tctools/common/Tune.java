package com.tctools.common;

import com.vantar.common.Settings;

@org.aeonbits.owner.Config.Sources({
    "file:/opt/tc-tools/tune.properties",
    "classpath:/tune.properties"
})
public interface Tune extends Settings.Common {

    // auth


    @DefaultValue("30")
    @Key("auth.token.check.interval")
    int getAuthTokenCheckMins();

    @DefaultValue("30")
    @Key("auth.token.expire")
    int getAuthTokenExpireMins();
}