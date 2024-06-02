package com.tctools.patch;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.dto.site.Site;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.exception.VantarException;
import com.vantar.service.patch.*;
import com.vantar.web.WebUi;

@PatchDelayed
public class AddressFix implements Patcher.PatchInterface {

    @Override
    public void setUi(WebUi webUi) {

    }

    @Override
    public Patcher.Result run() {
        Patcher.Result result = new Patcher.Result();
        try {
            Db.modelMongo.forEach(new Site(), dto -> {
                try {
                    Db.modelMongo.update(new ModelCommon.Settings(dto).mutex(false).logEvent(false));
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
                return true;
            });
            Db.modelMongo.forEach(new RadioMetricFlow(), dto -> {
                try {
                    Db.modelMongo.update(new ModelCommon.Settings(dto).mutex(false).logEvent(false));
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
                return true;
            });
            Db.modelMongo.forEach(new RadioMetricComplain(), dto -> {
                try {
                    Db.modelMongo.update(new ModelCommon.Settings(dto).mutex(false).logEvent(false));
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
                return true;
            });
        } catch (Exception e) {
            result.addFail(e).countFail();
        }
        return result;
    }
}
