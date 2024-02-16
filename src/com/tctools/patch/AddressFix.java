package com.tctools.patch;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.dto.site.Site;
import com.vantar.business.ModelMongo;
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
            ModelMongo.forEach(new Site(), dto -> {
                try {
                    ModelMongo.updateNoLog(dto);
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
            });
            ModelMongo.forEach(new RadioMetricFlow(), dto -> {
                try {
                    ModelMongo.updateNoLog(dto);
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
            });
            ModelMongo.forEach(new RadioMetricComplain(), dto -> {
                try {
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
