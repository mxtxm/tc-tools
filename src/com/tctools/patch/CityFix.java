package com.tctools.patch;

import com.tctools.business.dto.location.City;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.service.log.ServiceLog;
import com.vantar.service.patch.*;
import com.vantar.web.WebUi;


@PatchDelayed
public class CityFix implements Patcher.PatchInterface {

    @Override
    public void setUi(WebUi webUi) {

    }

    @Override
    public Patcher.Result run() {
        Patcher.Result result = new Patcher.Result();
        try {
            Db.modelMongo.forEach(new City(), dto -> {
                City f = (City) dto;
                if (f.name.get("fa") == null && f.name.get("en") == null) {
                    ServiceLog.log.error(">>>{}", f.id);
                    result.addFail("bad city").countFail();
                    return true;
                }
                if (f.name.get("fa") == null) {
                    f.name.put("fa", f.name.get("en"));
                    Db.modelMongo.update(new ModelCommon.Settings(dto).dtoHasFullData(true).mutex(false).logEvent(false));
                    result.countSuccess();
                }
                if (f.name.get("en") == null) {
                    f.name.put("en", f.name.get("fa"));
                    Db.modelMongo.update(new ModelCommon.Settings(dto).dtoHasFullData(true).mutex(false).logEvent(false));
                    result.countSuccess();
                }
                return true;
            });

        } catch (Exception e) {
            result.addFail(e).countFail();
        }
        return result;
    }
}
