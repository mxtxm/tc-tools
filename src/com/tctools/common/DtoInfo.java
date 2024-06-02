package com.tctools.common;

import com.tctools.business.dto.location.*;
import com.tctools.business.dto.project.container.Project;
import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.dto.system.*;
import com.tctools.business.dto.user.User;
import com.vantar.database.dto.DtoDictionary;
import com.vantar.database.nosql.mongo.MongoSequence;
import com.vantar.service.log.dto.*;
import com.vantar.service.patch.dto.PatchHistory;


public class DtoInfo {

    public static void start() {
        DtoDictionary.init(150);

        // base
        DtoDictionary.setGroup("داده های پایه");
        DtoDictionary.add(100, "لاگ", Log.class);
        DtoDictionary.add(100, "تنظیمات", Settings.class);
        DtoDictionary.add(100, "تست", WebTest.class);
        DtoDictionary.add(100, "وصله", PatchHistory.class);
        DtoDictionary.add(100, "Patch", PatchHistory.class);
        DtoDictionary.add(100, "Sequences", MongoSequence.class);

        // user
        DtoDictionary.setGroup("کاربران");
        //DtoDictionary.add("کاربران", User.class, "insert-exclude:signinT,token;present:id,fullName");
        DtoDictionary.add(3, "کاربران", User.class);
        DtoDictionary.add(4, "لاگ کاربران", UserLog.class);

        // location
        DtoDictionary.setGroup("مکان");
        DtoDictionary.add(1, "منطقه", Region.class);
        DtoDictionary.add(1, "استان", Province.class);
        DtoDictionary.add(2, "شهر", City.class);
        DtoDictionary.add(1, "بخش", District.class);
        DtoDictionary.add(1, "تعلق مکانی طراحی", LocationType.class);

        // project
        DtoDictionary.setGroup("پروژه");
        DtoDictionary.add(5, "پروژه", Project.class);

        // site
        DtoDictionary.setGroup("Site");
        DtoDictionary.add(7, "سایت", Site.class);
        DtoDictionary.add(6, "کلاس سایت", SiteClass.class);
        DtoDictionary.add(6, "تیپ سایت", SiteType.class);
        DtoDictionary.add(6, "مالکیت سایت", SiteOwnership.class);
        DtoDictionary.add(6, "نوع مالکیت", BtsOwnership.class);
        DtoDictionary.add(6, "شراکت دکل", BtsShare.class);
        DtoDictionary.add(6, "وضعیت نود", BtsStatus.class);
        DtoDictionary.add(6, "نوع دکل", BtsTowerType.class);
        DtoDictionary.add(6, "نوع نصب", BtsInstall.class);
        DtoDictionary.add(6, "بهینه سازی سکتور", SectorOptimization.class);
        DtoDictionary.add(6, "اپراتور", Operator.class);

        // radiometric
        DtoDictionary.setGroup("شکایت های رادیو متریک");
        DtoDictionary.add(8, "شکایت های رادیو متریک", RadioMetricComplain.class);
        DtoDictionary.add(8, "نوع ساختمان", PropertyType.class);
        DtoDictionary.add(8, "محل در ساختمان", PropertySection.class);

        DtoDictionary.setGroup("رادیو متریک");
        DtoDictionary.add(9, "روند کار رادیو متریک", RadioMetricFlow.class);
        DtoDictionary.add(9, "تاریخ تنظیم دستگاه", RadioMetricDevice.class);
        DtoDictionary.add(9, "برنامه ریزی پروژه", RadioMetricTarget.class);
        DtoDictionary.add(9, "کارکرد تکنسین", TechStatistic.class);
        DtoDictionary.add(9, "کارکرد استان", ProvinceStatistic.class);

        // mongo
        DtoDictionary.setGroup("MONGO");

        // no store
        DtoDictionary.setGroup("nostore");
        DtoDictionary.add(10, "Sector", Sector.class);
        DtoDictionary.add(10, "Proximity", Proximity.class);
        DtoDictionary.add(10, "Sector", Sector.class);
        DtoDictionary.add(10, "radiometric State", com.tctools.business.dto.project.radiometric.workflow.State.class);
        DtoDictionary.add(10, "Collocation", com.tctools.business.dto.site.Collocation.class);
    }
}