package com.tctools.patch;

import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.exception.VantarException;
import com.vantar.service.patch.*;
import com.vantar.web.WebUi;

@Patch
public class FixNormalRegular implements Patcher.PatchInterface {

    @Override
    public void setUi(WebUi webUi) {

    }

    @Override
    public Patcher.Result run() {
        Patcher.Result result = new Patcher.Result();
        try {
            Db.modelMongo.forEach(new RadioMetricComplain(), dto -> {
                try {
                    RadioMetricComplain flow = (RadioMetricComplain) dto;
                    if (flow.type == null || flow.type.equals(ComplainType.CustomerComplain)) {
                        return true;
                    }
                    flow.type = ComplainType.Normal;
                    Db.modelMongo.update(new ModelCommon.Settings(dto).mutex(false).logEvent(false));
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
                return true;
            });

            Db.modelMongo.forEach(new RadioMetricFlow(), dto -> {
                try {
                    RadioMetricFlow flow = (RadioMetricFlow) dto;
                    if (flow.complain == null || flow.complain.type == null || flow.complain.type.equals(ComplainType.CustomerComplain)) {
                        return true;
                    }
                    flow.complain.type = ComplainType.NormalRequest;
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
