package com.tctools.patch;

import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.vantar.business.CommonModelMongo;
import com.vantar.exception.VantarException;
import com.vantar.service.patch.*;

@Patch
public class UpdateCcRadiometric {

    public static Patcher.Result run() {
        Patcher.Result result = new Patcher.Result();
        try {
            CommonModelMongo.forEach(new RadioMetricFlow(), dto -> {
                try {
                    RadioMetricFlow flow = (RadioMetricFlow) dto;
                    if (!flow.isCc) {
                        return;
                    }
                    CommonModelMongo.updateNoLog(dto);
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
            });
        } catch (Exception e) {
            result.addFail(e).countFail();
        }
        return result;
    }
}
