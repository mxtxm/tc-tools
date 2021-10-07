package com.tctools.business.model.project.radiometric.workflow.export;

import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.dto.user.User;
import com.tctools.business.service.locale.AppLangKey;
import com.tctools.common.util.ExportCommon;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.datatype.Location;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.datetime.DateTime;
import com.vantar.web.Params;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


public class ExportAssigned extends ExportCommon {

    public static void excel(Params params, User user, HttpServletResponse response) throws ServerException {
        Long userId = params.getLong("userId", user.id);

        QueryBuilder q = new QueryBuilder(new RadioMetricFlow.Viewable());
        q.sort("assignDateTime:desc");
        q.condition().equal("assigneeId", userId);
        q.condition().equal("lastState", RadioMetricFlowState.Planned);

        List<RadioMetricFlow.Viewable> items;
        try {
            items = CommonRepoMongo.getData(q, params.getLang());
        } catch (DatabaseException | NoContentException e) {
            log.error("!", e);
            throw new ServerException(VantarKey.FETCH_FAIL);
        }

        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Planned sites");
        int r = 0;
        int c = 0;
        Row row = sheet.createRow(r);
        row.createCell(c++).setCellValue("استان");
        row.createCell(c++).setCellValue("شهر");
        row.createCell(c++).setCellValue("کد سایت");
        row.createCell(c++).setCellValue("توضیحات");
        row.createCell(c++).setCellValue("تعلق مکانی طراحی");
        row.createCell(c++).setCellValue("نام سایت");
        row.createCell(c++).setCellValue("آدرس");
        row.createCell(c++).setCellValue("عرض جغرافیایی");
        row.createCell(c++).setCellValue("طول جغرافیایی");
        row.createCell(c++).setCellValue("WLL/MCI");
        row.createCell(c++).setCellValue("BSC/RNC");
        row.createCell(c++).setCellValue("وضعیت نود");
        row.createCell(c++).setCellValue("تاریخ راه اندازی");
        row.createCell(c++).setCellValue("نوع مالکیت");
        row.createCell(c++).setCellValue("تاریخ شروع قرارداد");
        row.createCell(c++).setCellValue("وضعیت SHARE بودن با دیگر اپراتورها");
        row.createCell(c++).setCellValue("تعداد آنتن های اضافه روی دکل");
        row.createCell(c++).setCellValue("تیپ سایت");
        row.createCell(c++).setCellValue("نوع سایت");
        row.createCell(c++).setCellValue("نوع نصب BTS");
        row.createCell(c++).setCellValue("نوع دکل");
        row.createCell(c++).setCellValue("ارتفاع دکل");
        row.createCell(c++).setCellValue("ارتفاع ساختمان");
        row.createCell(c++).setCellValue("باند فرکانسی");

        row.createCell(c++).setCellValue("ارتفاع سکتور A");
        row.createCell(c++).setCellValue("تعداد آنتن سکتور A");
        row.createCell(c++).setCellValue("زاویه سکتور A");
        row.createCell(c++).setCellValue("تیلت مکانیکی سکتور A");
        row.createCell(c++).setCellValue("تیلت الکترونیکی سکتور A");
        row.createCell(c++).setCellValue("نوع پوشش سکتور A");
        row.createCell(c++).setCellValue("ارتفاع انتن دوم سکتور A");
        row.createCell(c++).setCellValue("زاویه آنتن دوم سکتور A");
        row.createCell(c++).setCellValue("تیلت مکانیکی آنتن دوم سکتور A");

        row.createCell(c++).setCellValue("ارتفاع سکتور 2");
        row.createCell(c++).setCellValue("تعداد آنتن سکتور 2");
        row.createCell(c++).setCellValue("زاویه سکتور 2");
        row.createCell(c++).setCellValue("تیلت مکانیکی سکتور 2");
        row.createCell(c++).setCellValue("تیلت الکترونیکی سکتور 2");
        row.createCell(c++).setCellValue("نوع پوشش سکتور دوم");
        row.createCell(c++).setCellValue("ارتفاع انتن دوم سکتور2");
        row.createCell(c++).setCellValue("زاویه آنتن دوم سکتور 2");
        row.createCell(c++).setCellValue("تیلت مکانیکی آنتن دوم سکتور 2");
        row.createCell(c++).setCellValue("ارتفاع سکتور 3");
        row.createCell(c++).setCellValue("تعداد آنتن سکتور 3");
        row.createCell(c++).setCellValue("زاویه سکتور 3");
        row.createCell(c++).setCellValue("تیلت مکانیکی سکتور 3");
        row.createCell(c++).setCellValue("تیلت الکترونیکی سکتور 3");
        row.createCell(c++).setCellValue("نوع پوشش سکتور سوم");
        row.createCell(c++).setCellValue("ارتفاع انتن دوم سکتور3");
        row.createCell(c++).setCellValue("زاویه آنتن دوم سکتور 3");
        row.createCell(c++).setCellValue("تیلت مکانیکی آنتن دوم سکتور 3");
        row.createCell(c++).setCellValue("ارتفاع سکتور 4");
        row.createCell(c++).setCellValue("تعداد آنتن سکتور 4");
        row.createCell(c++).setCellValue("زاویه سکتور 4");
        row.createCell(c++).setCellValue("تیلت مکانیکی سکتور 4");
        row.createCell(c++).setCellValue("تیلت الکترونیکی سکتور 4");
        row.createCell(c++).setCellValue("تقسیم بندی بهینه سازی سکتور چهارم");
        row.createCell(c++).setCellValue("ارتفاع انتن دوم سکتور4");
        row.createCell(c++).setCellValue("زاویه آنتن دوم سکتور 4");
        row.createCell(c).setCellValue("تیلت مکانیکی آنتن دوم سکتور 4");

        for (RadioMetricFlow.Viewable flow : items) {
            c = 0;
            row = sheet.createRow(++r);
            row.createCell(c++).setCellValue(getValueObj(flow.reportedProvince == null ? flow.province.name : flow.reportedProvince)); //استان
            row.createCell(c++).setCellValue(getValueObj(flow.reportedCity == null ? flow.city.name : flow.reportedCity)); //شهر
            row.createCell(c++).setCellValue(flow.site.code); //کد سایت
            row.createCell(c++).setCellValue(getValueObj(flow.site.comments)); //comment
            row.createCell(c++).setCellValue(flow.site.locationType == null ? "" : getValueObj(flow.site.locationType.name)); //تعلق مکانی طراحی
            row.createCell(c++).setCellValue(getValueObj(flow.site.name)); //نام سایت
            row.createCell(c++).setCellValue(getValueObj(flow.spotAddress == null ? flow.site.address : flow.spotAddress)); //آدرس

            Location location = flow.spotLocation == null ? flow.site.location : flow.spotLocation;
            row.createCell(c++).setCellValue(location == null ? 0 : getValueObj(location.latitude)); //عرض جغرافیایی
            row.createCell(c++).setCellValue(location == null ? 0 : getValueObj(location.longitude)); //طول جغرافیایی

            row.createCell(c++).setCellValue(flow.site.siteOwnership == null ? "" : getValueObj(flow.site.siteOwnership.name)); //WLL/MCI
            row.createCell(c++).setCellValue(getValueObj(flow.site.controller)); //BSC/RNC
            row.createCell(c++).setCellValue(flow.site.btsStatus == null ? "" : getValueObj(flow.site.btsStatus.name)); //وضعیت نود
            row.createCell(c++).setCellValue(getValueObj(flow.site.setupDateFa)); //تاریخ راه اندازی
            row.createCell(c++).setCellValue(flow.site.btsOwnership == null ? "" : getValueObj(flow.site.btsOwnership.name)); //نوع مالکیت
            row.createCell(c++).setCellValue(getValueObj(flow.site.contractStartDateFa)); //تاریخ شروع قرارداد

            String collocation;
            if (CollocationType.Host.equals(flow.site.collocationType)) {
                collocation = "میزبان";
            } else if (CollocationType.Guest.equals(flow.site.collocationType)) {
                collocation = "مهمان";
            } else {
                collocation = "مشترک نمی باشد";
            }
            row.createCell(c++).setCellValue(collocation); //وضعیت SHARE بودن با دیگر اپراتورها

            row.createCell(c++).setCellValue(getValueObj(flow.site.etcTransceiverCount)); //تعداد آنتن های اضافه روی دکل
            row.createCell(c++).setCellValue(flow.site.siteClass == null ? "" : getValueObj(flow.site.siteClass.name)); //تیپ سایت
            row.createCell(c++).setCellValue(flow.site.siteType == null ? "" : getValueObj(flow.site.siteType.name)); //نوع سایت
            row.createCell(c++).setCellValue(flow.site.btsInstall == null ? "" : getValueObj(flow.site.btsInstall.name)); //نوع نصب BTS
            row.createCell(c++).setCellValue(flow.site.btsTowerType == null ? "" : getValueObj(flow.site.btsTowerType.name)); //نوع دکل
            row.createCell(c++).setCellValue(getValueObj(flow.site.towerHeight)); //ارتفاع دکل
            row.createCell(c++).setCellValue(getValueObj(flow.site.buildingHeight)); //ارتفاع ساختمان
            row.createCell(c++).setCellValue(getValueObj(flow.site.frequencyBand)); //باند فرکانسی

            if (flow.site.sectors != null) {
                Map<String, List<String>> sectorData = new HashMap<>();
                for (Sector.Viewable sector : flow.site.sectors) {
                    boolean is2 = sector.title.contains("2");
                    String name;
                    if (sector.title.contains("A")) {
                        name = is2 ? "A2" : "A";
                    } else if (sector.title.contains("B")) {
                        name = is2 ? "B2" : "B";
                    } else if (sector.title.contains("C")) {
                        name = is2 ? "C2" : "C";
                    } else if (sector.title.contains("D")) {
                        name = is2 ? "D2" : "D";
                    } else {
                        continue;
                    }

                    List<String> data = new ArrayList<>();
                    if (is2) {
                        data.add(getValue(sector.height)); //ارتفاع سکتور
                        data.add(getValue(sector.azimuth)); //زاویه سکتور
                        data.add(getValue(sector.mechanicalTilt)); //تیلت مکانیکی سکتور
                    } else  {
                        data.add(getValue(sector.height)); //ارتفاع سکتور
                        data.add(getValue(sector.antennaCount)); //تعداد آنتن سکتور
                        data.add(getValue(sector.azimuth)); //زاویه سکتور
                        data.add(getValue(sector.mechanicalTilt)); //تیلت مکانیکی سکتور
                        data.add(getValue(sector.electricalTilt)); //تیلت الکترونیکی سکتور
                        data.add(sector.locationType == null ? "" : getValueObj(sector.locationType.name)); //نوع پوشش سکتور
                    }
                    sectorData.put(name, data);
                }

                List<String> names = new ArrayList<>();
                names.add("A");
                names.add("A2");
                names.add("B");
                names.add("B2");
                names.add("C");
                names.add("C2");
                names.add("D");
                names.add("D2");
                for (String name : names) {
                    List<String> data = sectorData.get(name);
                    if (data == null) {
                        for (int i = 0 ; i < (name.contains("2") ? 3 : 6) ; ++i) {
                            row.createCell(c++).setCellValue("");
                        }
                        break;
                    }
                    for (String item : data) {
                        row.createCell(c++).setCellValue(item);
                    }
                }
            }

        }

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=assigned-"
            + (new DateTime().formatter().getDateTimeSimple()) + ".xlsx");

        try {
            wb.write(response.getOutputStream());
            wb.close();
        } catch (IOException e) {
            log.error("", e);
            throw new ServerException(AppLangKey.EXPORT_FAIL);
        }
    }
}