package com.tctools.patch;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.exception.VantarException;
import com.vantar.service.patch.*;

@Patch
public class UpdateCcRadiometric {

    public static Patcher.Result run() {
        Patcher.Result result = new Patcher.Result();
        try {
            Db.modelMongo.forEach(new RadioMetricFlow(), dto -> {
                try {
                    RadioMetricFlow flow = (RadioMetricFlow) dto;
                    if (!flow.isCc) {
                        return true;
                    }
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
