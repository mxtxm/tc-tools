package com.tctools.patch;

import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.vantar.business.ModelMongo;
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
            ModelMongo.forEach(new RadioMetricComplain(), dto -> {
                try {
                    RadioMetricComplain flow = (RadioMetricComplain) dto;
                    if (flow.type == null || flow.type.equals(ComplainType.CustomerComplain)) {
                        return;
                    }
                    flow.type = ComplainType.Normal;
                    ModelMongo.updateNoLog(dto);
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
            });

            ModelMongo.forEach(new RadioMetricFlow(), dto -> {
                try {
                    RadioMetricFlow flow = (RadioMetricFlow) dto;
                    if (flow.complain == null || flow.complain.type == null || flow.complain.type.equals(ComplainType.CustomerComplain)) {
                        return;
                    }
                    flow.complain.type = ComplainType.NormalRequest;
                    ModelMongo.updateNoLog(dto);
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
