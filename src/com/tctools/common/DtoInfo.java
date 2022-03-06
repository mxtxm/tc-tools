package com.tctools.common;

import com.tctools.business.dto.location.*;
import com.tctools.business.dto.project.container.Project;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.project.radiometric.complain.*;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.site.*;
import com.tctools.business.dto.system.*;
import com.tctools.business.dto.user.*;
import com.vantar.database.dto.DtoDictionary;
import com.vantar.database.query.QueryBuilder;
import com.vantar.service.log.dto.*;


public class DtoInfo {

    public static void start() {
//        DtoDictionary.setCategory("داده های پایه");
//        DtoDictionary.add("a211", Test1.class);
//        DtoDictionary.add("a212d", Test1A.class);
//        DtoDictionary.add("a213s", Test1B.class);
//        DtoDictionary.add("a214sf", Test1C.class);
//        DtoDictionary.add("a215sg", Test1D.class);
//        DtoDictionary.add("a226", Test2.class);
//        DtoDictionary.add("a237", Test3.class);
//        DtoDictionary.add("a248", Test4.class);
//        DtoDictionary.add("a24A", Test4A.class);
//        DtoDictionary.add("a25", Test5.class);
//        DtoDictionary.add("a25A", Test5A.class);
//        DtoDictionary.add("a26", Test6.class);
//        DtoDictionary.add("a263", Test6A.class);
//        DtoDictionary.add("a275", Test7.class);
//        DtoDictionary.add("a28", Test8.class);
//        DtoDictionary.add("a282", Test8A.class);
//        DtoDictionary.add("a293", Test9.class);
//        DtoDictionary.add("a120", Test10.class);
//        DtoDictionary.add("a220", Test10A.class);
//        DtoDictionary.add("a320", Test11.class);
//        DtoDictionary.add("a3200", Test12.class);
//        DtoDictionary.add("a3200s", Test12A.class);


//
//        if (1==1)return;

        // base
        DtoDictionary.setCategory("داده های پایه");
        DtoDictionary.add( "لاگ", Log.class);
        DtoDictionary.add( "تنظیمات", Settings.class);
        DtoDictionary.add( "تست", WebTest.class);

        // user
        DtoDictionary.setCategory("کاربران");
        DtoDictionary.add("کاربران", User.class, "insert-exclude:signinT,token;present:id,fullName");
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
        DtoDictionary.setCategory("شکایت های رادیو متریک");
        DtoDictionary.add("شکایت های رادیو متریک", RadioMetricComplain.class);
        DtoDictionary.add("نوع ساختمان", PropertyType.class);
        DtoDictionary.add("محل در ساختمان", PropertySection.class);
        DtoDictionary.setCategory("رادیو متریک");
        DtoDictionary.add("روند کار رادیو متریک", RadioMetricFlow.class);
        DtoDictionary.add("تاریخ تنظیم دستگاه", RadioMetricDevice.class);
        DtoDictionary.add( "برنامه ریزی پروژه", RadioMetricTarget.class);
        DtoDictionary.add( "کارکرد تکنسین", TechStatistic.class);
        DtoDictionary.add( "کارکرد استان", ProvinceStatistic.class);

        // hseaudit
        DtoDictionary.setCategory("بازرسی نصب سایت های فاز ۸");
        DtoDictionary.add("کارفرما", SubContractor.class);
        DtoDictionary.add("روند پرسشنامه", HseAuditQuestionnaire.class);
        QueryBuilder q = new QueryBuilder(new HseAuditQuestion());
        q.sort("order:1");
        q.condition().equal("enabled", true);
        DtoDictionary.add("پرسش", HseAuditQuestion.class, q);

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