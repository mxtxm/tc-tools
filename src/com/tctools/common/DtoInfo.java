package com.tctools.common;

import com.tctools.business.dto.location.*;
import com.tctools.business.dto.project.container.Project;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.dto.system.*;
import com.tctools.business.dto.user.User;
import com.vantar.database.dto.DtoDictionary;
import com.vantar.database.nosql.mongo.MongoSequence;
import com.vantar.database.query.QueryBuilder;
import com.vantar.service.log.dto.*;
import com.vantar.service.patch.dto.PatchHistory;


public class DtoInfo {

    public static void start() {
        // base
        DtoDictionary.setCategory("داده های پایه");
        DtoDictionary.add( "لاگ", Log.class);
        DtoDictionary.add( "تنظیمات", Settings.class);
        DtoDictionary.add( "تست", WebTest.class);
        DtoDictionary.add("وصله", PatchHistory.class);
        DtoDictionary.add("Patch", PatchHistory.class);

        // user
        DtoDictionary.setCategory("کاربران");
        //DtoDictionary.add("کاربران", User.class, "insert-exclude:signinT,token;present:id,fullName");
        DtoDictionary.add("کاربران", User.class);
        DtoDictionary.add( "لاگ کاربران", UserLog.class);

        // location
        DtoDictionary.setCategory("مکان");
        DtoDictionary.add("منطقه", Region.class);
        DtoDictionary.add("استان", Province.class);
        DtoDictionary.add("شهر", City.class);
        DtoDictionary.add("بخش", District.class);
        DtoDictionary.add("تعلق مکانی طراحی", LocationType.class);

        // project
        DtoDictionary.setCategory("پروژه");
        DtoDictionary.add("پروژه", Project.class);

        // site
        DtoDictionary.setCategory("Site");
        DtoDictionary.add("سایت", Site.class);
        DtoDictionary.add("کلاس سایت", SiteClass.class);
        DtoDictionary.add("تیپ سایت", SiteType.class);
        DtoDictionary.add("مالکیت سایت", SiteOwnership.class);
        DtoDictionary.add("نوع مالکیت", BtsOwnership.class);
        DtoDictionary.add("شراکت دکل", BtsShare.class);
        DtoDictionary.add("وضعیت نود", BtsStatus.class);
        DtoDictionary.add("نوع دکل", BtsTowerType.class);
        DtoDictionary.add("نوع نصب", BtsInstall.class);
        DtoDictionary.add("بهینه سازی سکتور", SectorOptimization.class);
        DtoDictionary.add("اپراتور", Operator.class);

        // radiometric
        if (com.vantar.common.Settings.config.getProperty("project.radiometric").equals("on")) {
            DtoDictionary.setCategory("شکایت های رادیو متریک");
            DtoDictionary.add("شکایت های رادیو متریک", RadioMetricComplain.class);
            DtoDictionary.add("نوع ساختمان", PropertyType.class);
            DtoDictionary.add("محل در ساختمان", PropertySection.class);
            DtoDictionary.setCategory("رادیو متریک");
            DtoDictionary.add("روند کار رادیو متریک", RadioMetricFlow.class);
            DtoDictionary.add("تاریخ تنظیم دستگاه", RadioMetricDevice.class);
            DtoDictionary.add("برنامه ریزی پروژه", RadioMetricTarget.class);
            DtoDictionary.add("کارکرد تکنسین", TechStatistic.class);
            DtoDictionary.add("کارکرد استان", ProvinceStatistic.class);
        }

        // hseaudit
        if (com.vantar.common.Settings.config.getProperty("project.hse.audit").equals("on")) {
            DtoDictionary.setCategory("بازرسی نصب سایت های فاز ۸");
            DtoDictionary.add("کارفرما", SubContractor.class);
            DtoDictionary.add("روند پرسشنامه", HseAuditQuestionnaire.class);
            QueryBuilder q = new QueryBuilder(new HseAuditQuestion());
            q.sort("order:1");
            q.condition().equal("enabled", true);
            DtoDictionary.add("پرسش", HseAuditQuestion.class, q);
        }

        // mongo
        DtoDictionary.setCategory("MONGO");
        DtoDictionary.add("Sequences", MongoSequence.class);

        // no store
        DtoDictionary.setCategory("nostore");
        DtoDictionary.add("Sector", Sector.class);
        DtoDictionary.add("HseAuditAnswer", HseAuditAnswer.class);
        DtoDictionary.add("Proximity", Proximity.class);
        DtoDictionary.add("Sector", Sector.class);
        DtoDictionary.add("hseaudit State", com.tctools.business.dto.project.hseaudit.State.class);
        DtoDictionary.add("radiometric State", com.tctools.business.dto.project.radiometric.workflow.State.class);
        DtoDictionary.add("Collocation", com.tctools.business.dto.site.Collocation.class);
    }
}