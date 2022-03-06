package com.tctools.web.patch;

import com.tctools.business.dto.user.*;
import com.tctools.web.patch.test.DataDependencyX;
import com.vantar.business.CommonModelMongo;
import com.vantar.database.dependency.DataDependency;
import com.vantar.exception.*;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@WebServlet({
    "/test/t",
    "/test/index",
})
@MultipartConfig(
    location="/tmp",
    fileSizeThreshold=10*1024*1024,
    maxFileSize=10*1024*1024,
    maxRequestSize=10*1024*1024*5
)
public class TestController extends RouteToMethod {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);

    public void t(Params params, HttpServletResponse response) throws FinishException, DatabaseException, NoContentException {

    }

    public void index(Params params, HttpServletResponse response) throws ServerException, InputException, NoContentException {

        DataDependency dep = new DataDependency(new Test1());

        Test1 test1 = new Test1();
        CommonModelMongo.purge(test1);
        for (long i = 1 ; i < 300 ; ++i) {
            test1.id = i;
            test1.name = "test1" + i;
            CommonModelMongo.insert(test1);
        }

//// case (1)
        Test2 test2 = new Test2();
        CommonModelMongo.purge(test2);
        for (long i = 1 ; i < 5 ; ++i) {
            test2.id = i;
            test2.test1Id = i;
            test2.name = "test2" + i;
            CommonModelMongo.insert(test2);
        }
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test2(1)] case(1)", dep.getDependencies(1L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test2(3)] case(1)", dep.getDependencies(3L));

//// case (3)
        Test3 test3 = new Test3();
        test3.id = 1L;
        test3.name = "AAAA";
        test3.test1Ids = new ArrayList<>();
        test3.test1Ids.add(7L);
        test3.test1Ids.add(8L);
        CommonModelMongo.purge(test3);
        CommonModelMongo.insert(test3);
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test3(1)] case(3)", dep.getDependencies(7L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test3(1)] case(3)", dep.getDependencies(8L));

//// case (no dependency by object with no @Depends)
        Test4 test4 = new Test4();
        test4.id = 1L;
        test4.name = "AAAAA";
        test4.test1 = CommonModelMongo.getById(new Test1(9L));
        CommonModelMongo.purge(test4);
        CommonModelMongo.insert(test4);
        log.error(">>>>>{} > [] case(object with no @Depends)", dep.getDependencies(9L));

//// case (no dependency by list of objects with no @Depends)
        Test5 test5 = new Test5();
        test5.id = 1L;
        test5.name = "AAAAA";
        test5.test1s = new ArrayList<>(2);
        test5.test1s.add(CommonModelMongo.getById(new Test1(10L)));
        test5.test1s.add(CommonModelMongo.getById(new Test1(11L)));
        CommonModelMongo.purge(test5);
        CommonModelMongo.insert(test5);
        log.error(">>>>>{} > [] case(object list with no @Depends)", dep.getDependencies(10L));
        log.error(">>>>>{} > [] case(object list with no @Depends)", dep.getDependencies(11L));

//// case (2)
        Test4A test4A = new Test4A();
        test4A.id = 1L;
        test4A.name = "AAAAA";
        test4A.test1 = CommonModelMongo.getById(new Test1(12L));
        CommonModelMongo.purge(test4A);
        CommonModelMongo.insert(test4A);
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test4A(1)] case(2)", dep.getDependencies(12L));

//// case (4)
        Test5A test5A = new Test5A();
        test5A.id = 1L;
        test5A.name = "AAAAA";
        test5A.test1s = new ArrayList<>(2);
        test5A.test1s.add(CommonModelMongo.getById(new Test1(13L)));
        test5A.test1s.add(CommonModelMongo.getById(new Test1(14L)));
        CommonModelMongo.purge(test5A);
        CommonModelMongo.insert(test5A);
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test5A(1)] case(4)", dep.getDependencies(13L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test5A(1)] case(4)", dep.getDependencies(14L));


//// case (1) recursive
        Test1A test1A = new Test1A();
        CommonModelMongo.purge(test1A);
        test1A.id = 1L;
        test1A.name = "asd";
        CommonModelMongo.insert(test1A);
        test1A.id = 2L;
        test1A.test1Aid = 1L;
        test1A.name = "aassdd";
        CommonModelMongo.insert(test1A);
        log.error(">>>>>{} > [] case(1) recursive with no id", new DataDependencyX(test1A).getDependencies(2L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test1A(2)] case(1)", new DataDependencyX(test1A).getDependencies(1L));

//// case (3) recursive list
        Test1B test1B = new Test1B();
        CommonModelMongo.purge(test1B);
        test1B.id = 1L;
        test1B.name = "asd";
        CommonModelMongo.insert(test1B);
        test1B.id = 2L;
        test1B.name = "asdf";
        CommonModelMongo.insert(test1B);
        test1B.id = 3L;
        test1B.name = "aassdd";
        test1B.test1Bids = new HashSet<>();
        test1B.test1Bids.add(1L);
        test1B.test1Bids.add(2L);
        CommonModelMongo.insert(test1B);
        log.error(">>>>>{} > [] case(1) recursive with no id", new DataDependencyX(test1B).getDependencies(3L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test1B(3)] case(1)", new DataDependencyX(test1B).getDependencies(1L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test1B(3)] case(1)", new DataDependencyX(test1B).getDependencies(2L));

//// case (2) recursive
        Test1C test1C = new Test1C();
        CommonModelMongo.purge(test1C);
        test1C.id = 1L;
        test1C.name = "asd";
        CommonModelMongo.insert(test1C);
        Test1C test1C2 = new Test1C();
        test1C2.id = 2L;
        test1C2.test1C = test1C;
        test1C2.name = "aassdd";
        CommonModelMongo.insert(test1C2);
        log.error(">>>>>{} > [] case(2) recursive with no object", new DataDependencyX(test1C).getDependencies(2L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test1C(2)] case(2)", new DataDependencyX(test1C).getDependencies(1L));

//// case (4) recursive list
        Test1D test1D = new Test1D();
        CommonModelMongo.purge(test1D);
        test1D.id = 1L;
        test1D.name = "asd";
        CommonModelMongo.insert(test1D);
        Test1D test1D2 = new Test1D();
        test1D2.id = 2L;
        test1D2.name = "asdf";
        CommonModelMongo.insert(test1D2);
        Test1D test1D3 = new Test1D();
        test1D3.id = 3L;
        test1D3.name = "aassdd";
        test1D3.test1Ds = new ArrayList<>();
        test1D3.test1Ds.add(test1D);
        test1D3.test1Ds.add(test1D2);
        CommonModelMongo.insert(test1D3);
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test1D(3)] case(4)", new DataDependencyX(test1D).getDependencies(1L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test1D(3)] case(4)", new DataDependencyX(test1D).getDependencies(2L));
        log.error(">>>>>{} > [] case(4) recursive with no object", new DataDependencyX(test1D).getDependencies(3L));

//// case (7)
        Test6 test6 = new Test6();
        test6.id = 1L;
        test6.name = "AAAAAbbb";
        test6.test6A = new Test6A();
        test6.test6A.id = 2L;
        test6.test6A.name = "asdasd";
        test6.test6A.test7 = new Test7();
        test6.test6A.test7.id = 12L;
        test6.test6A.test7.name = "asdsggegerg";
        test6.test6A.test7.test1Id = 20L;
        CommonModelMongo.purge(test6);
        CommonModelMongo.insert(test6);
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test6(1)] case(7)", dep.getDependencies(20L));


//// case (8)
        Test8 test8 = new Test8();
        test8.id = 1L;
        test8.name = "AAAAAbbb";
        test8.test8A = new Test8A();
        test8.test8A.id = 2L;
        test8.test8A.name = "asdasd";
        test8.test8A.test9s = new ArrayList<>(2);
        test8.test8A.test9s.add(new Test9(1L, "pool", 21L));
        test8.test8A.test9s.add(new Test9(2L, "pool2", 22L));
        test8.test8A.test9s.add(new Test9(41L, "pool3", 23L));
        CommonModelMongo.purge(test8);
        CommonModelMongo.insert(test8);
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test8(1)] case(8)", dep.getDependencies(21L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test8(1)] case(8)", dep.getDependencies(22L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test8(1)] case(8)", dep.getDependencies(23L));


//// case (8)
        Test10 test10 = new Test10();
        test10.id = 1L;
        test10.name = "AAAAAbbb";
        test10.test10A = new Test10A();
        test10.test10A.id = 2L;
        test10.test10A.name = "asdasd";
        test10.test10A.test11 = new ArrayList<>(2);
        test10.test10A.test11.add(new Test11(1L, "pool", 25L, 27L));
        test10.test10A.test11.add(new Test11(2L, "pool2", 26L, 28L));
        test10.test10A.test11.add(new Test11(41L, "pool3", 29L, 30L));
        CommonModelMongo.purge(test10);
        CommonModelMongo.insert(test10);
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test10(1)] case(8)", dep.getDependencies(25L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test10(1)] case(8)", dep.getDependencies(26L));
        log.error(">>>>>{} > [com.tctools.business.dto.user.Test10(1)] case(8)", dep.getDependencies(27L));







    }

}