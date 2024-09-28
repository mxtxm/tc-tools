package com.tctools.patch;

import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow;
import com.tctools.business.dto.site.Site;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.exception.VantarException;
import com.vantar.service.log.ServiceLog;
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
            Db.modelMongo.forEach(new RadioMetricComplain(), dto -> {
                RadioMetricComplain d = (RadioMetricComplain) dto;
                d.addressOld = d.address;
                d.complainerAddressOld = d.complainerAddress;
                dto.setRunInnerEvents(false);
                try {
                    Db.modelMongo.update(new ModelCommon.Settings(dto).dtoHasFullData(true).mutex(false).logEvent(false));
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
                return true;
            });
            ServiceLog.log.info("RadioMetricComplain done");

            Db.modelMongo.forEach(new RadioMetricFlow(), dto -> {
                RadioMetricFlow d = (RadioMetricFlow) dto;
                d.spotAddressOld = d.spotAddress;
                d.siteAddressOld = d.siteAddress;
                if (d.site != null) {
                    d.site.addressOld = d.site.address;
                }
                if (d.complain != null) {
                    d.complain.addressOld = d.complain.address;
                    d.complain.complainerAddressOld = d.complain.complainerAddress;
                }
                dto.setRunInnerEvents(false);
                try {
                    Db.modelMongo.update(new ModelCommon.Settings(dto).dtoHasFullData(true).mutex(false).logEvent(false));
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
                return true;
            });
            ServiceLog.log.info("RadioMetric done");

            Db.modelMongo.forEach(new Site(), dto -> {
                Site d = (Site) dto;
                d.addressOld = d.address;
                dto.setRunInnerEvents(false);
                try {
                    Db.modelMongo.update(new ModelCommon.Settings(dto).dtoHasFullData(true).mutex(false).logEvent(false));
                    result.countSuccess();
                } catch (VantarException e) {
                    result.addFail(e).countFail();
                }
                return true;
            });
            ServiceLog.log.info("Site done");

        } catch (Exception e) {
            result.addFail(e).countFail();
        }
        return result;
    }
}
