package com.tctools.web.patch;

import com.tctools.business.dto.location.City;
import com.tctools.business.dto.project.hseaudit.*;
import com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain;
import com.tctools.business.dto.project.radiometric.workflow.*;
import com.tctools.business.dto.project.radiometric.workflow.State;
import com.tctools.business.dto.site.Site;
import com.tctools.business.dto.user.*;
import com.tctools.business.model.project.radiometric.workflow.WorkFlowModel;
import com.tctools.common.Param;
import com.tctools.common.util.ExportCommon;
import com.vantar.admin.model.Admin;
import com.vantar.business.CommonRepoMongo;
import com.vantar.database.dependency.DataDependency;
import com.vantar.database.dto.*;
import com.vantar.database.query.QueryBuilder;
import com.vantar.exception.*;
import com.vantar.locale.Locale;
import com.vantar.locale.*;
import com.vantar.util.datetime.DateTime;
import com.vantar.util.file.FileUtil;
import com.vantar.util.json.Json;
import com.vantar.util.string.StringUtil;
import com.vantar.web.*;
import org.slf4j.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;


@WebServlet({
    "/test/t",
    "/test/index",
    "/test/fix",
    "/test/fix/complain/images",
    "/test/check",
    "/test/check/complains",
    "/test/city/fix",


    "/test/fix/date",
    "/test/fix/sec",
    "/test/fix/sec/select",
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
        List<Long> ids = new ArrayList<>();
        String s = "[0, 3, 259, 4, 6, 8, 73, 9, 74, 330, 396, 397, 14, 146, 19, 87, 23, 26, 28, 351, 224, 33, 417, 34, 228, 100, 38, 39, 105, 297, 45, 110, 51, 116, 53, 118, 119, 57, 121, 380, 63]";
        ids = Json.listFromJson(s, Long.class);


        log.error(">>{}",ids);


        QueryBuilder q = new QueryBuilder(new SubContractor());
        q.condition().inNumber("id", ids);
        List<Dto> x = CommonRepoMongo.getData(q);
        log.error(">>{}",x);

        //        RadioMetricFlow flow = new RadioMetricFlow();
//        QueryBuilder q = new QueryBuilder(flow);
//        q.condition().equal("site.code", params.getString("sitecode"));
//        q.condition().in(
//            "lastState",
//            RadioMetricFlowState.Pending,
//            RadioMetricFlowState.Planned
//        );
//
//        try {
//            for (Dto dto : CommonRepoMongo.getData(q)) {
//                flow = (RadioMetricFlow) dto;
//                log.error(">>>>>{} {}", flow.site.code, flow.lastState);
//            }
//        } catch (DatabaseException | NoContentException e) {
//            e.printStackTrace();
//        }

//        String s = "{\"answers\":[{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39},{\"comments\":\"\",\"imageNames\":[],\"questionId\":1},{\"comments\":\"\",\"imageNames\":[],\"questionId\":2},{\"comments\":\"\",\"imageNames\":[],\"questionId\":3},{\"comments\":\"\",\"imageNames\":[],\"questionId\":40},{\"comments\":\"بالاتر از كارخانه سيمان خمير- 5 كيلومتر به سمت لنگه - سمت راست جاده - حدود 500 متر وارد جاده خاكي - روستاي چاه دراز - دفتر مخابراتي\",\"imageNames\":[],\"questionId\":4},{\"comments\":\"\",\"imageNames\":[],\"questionId\":5},{\"comments\":\"\",\"imageNames\":[],\"questionId\":6},{\"answer\":\"Option3\",\"comments\":\"\",\"imageNames\":[],\"questionId\":7},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":8},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":9},{\"comments\":\"\",\"imageNames\":[],\"questionId\":10},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":11},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":12},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":13},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[],\"questionId\":14},{\"answer\":\"NA\",\"comments\":\"\",\"imageNames\":[],\"questionId\":15},{\"comments\":\"\",\"imageNames\":[],\"questionId\":16},{\"comments\":\"\",\"imageNames\":[],\"questionId\":17},{\"comments\":\"\",\"imageNames\":[],\"questionId\":18},{\"comments\":\"\",\"imageNames\":[],\"questionId\":19},{\"comments\":\"\",\"imageNames\":[],\"questionId\":20},{\"comments\":\"\",\"imageNames\":[],\"questionId\":21},{\"comments\":\"\",\"imageNames\":[],\"questionId\":22},{\"comments\":\"\",\"imageNames\":[],\"questionId\":23},{\"comments\":\"\",\"imageNames\":[],\"questionId\":24},{\"comments\":\"\",\"imageNames\":[],\"questionId\":25},{\"comments\":\"\",\"imageNames\":[],\"questionId\":26},{\"comments\":\"\",\"imageNames\":[],\"questionId\":27},{\"comments\":\"\",\"imageNames\":[],\"questionId\":28},{\"comments\":\"\",\"imageNames\":[],\"questionId\":29},{\"comments\":\"\",\"imageNames\":[],\"questionId\":30},{\"comments\":\"\",\"imageNames\":[],\"questionId\":31},{\"comments\":\"\",\"imageNames\":[],\"questionId\":32},{\"comments\":\"\",\"imageNames\":[],\"questionId\":33},{\"comments\":\"\",\"imageNames\":[],\"questionId\":34},{\"comments\":\"\",\"imageNames\":[],\"questionId\":35},{\"comments\":\"\",\"imageNames\":[],\"questionId\":36},{\"comments\":\"\",\"imageNames\":[],\"questionId\":37},{\"comments\":\"\",\"imageNames\":[],\"questionId\":38},{\"answer\":\"Yes\",\"comments\":\"\",\"imageNames\":[\"\\/storage\\/emulated\\/0\\/Android\\/data\\/ir.ictrc.mobilesitesafety\\/files\\/Pictures\\/JtelHSE\\/UserImage_Questionnaire4125_Question39_14000912_222913.jpg\"],\"questionId\":39}],\"id\":4125,\"notAllowed\":false}";
//
//        HseAuditQuestionnaire o = Json.fromJson(s, HseAuditQuestionnaire.class);
//        log.error(">>>>{}",o);

    }

    public void fixSecSelect(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        try {
            //List<RadioMetricFlow> items = CommonRepoMongo.getData(new RadioMetricFlow());

//            QueryBuilder q = new QueryBuilder();
            List<RadioMetricFlow> items = CommonRepoMongo.getData(new RadioMetricFlow());

            for (RadioMetricFlow f: items) {
                if (f.sectors == null || f.sectors.isEmpty()) {
                    continue;
                }
//if (!f.site.code.equals("KJ0835")) {
//    continue;
//}

                try {
                    WorkFlowModel.setNearestSector(f);

                    CommonRepoMongo.update(f);

                    ui.addMessage(f.site.code).write();
                } catch (Exception x) {

                    ui.addErrorMessage(x).write();
                }
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }
    public void fixSec(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        try {
            List<RadioMetricFlow> items = CommonRepoMongo.getData(new RadioMetricFlow());

            for (RadioMetricFlow f: items) {
                if (f.sectors == null || f.sectors.isEmpty()) {
                    continue;
                }

                QueryBuilder q = new QueryBuilder(new Site());
                q.condition().equal("code", f.site.code);
                try {
                    Site site = CommonRepoMongo.getFirst(q);

                    f.site = site;
                    f.sectors = site.sectors;
                    CommonRepoMongo.update(f);

                    ui.addMessage(site.code).write();
                } catch (Exception x) {
                    ui.addErrorMessage(x).write();
                }
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }


    public void fixDate(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        String[] x = ("ES0427\n" +
            "ES1046\n" +
            "ES1158\n" +
            "ES0013\n" +
            "ES0017\n" +
            "ES0023\n" +
            "ES0455\n" +
            "ES0501\n" +
            "ES0502\n" +
            "ES1045\n" +
            "ES0405\n" +
            "ES0201\n" +
            "ES0365\n" +
            "ES0428\n" +
            "ES0234\n" +
            "ES0243\n" +
            "ES0404\n" +
            "ES0510\n" +
            "ES1059\n" +
            "KJ0119\n" +
            "KJ0611\n" +
            "KJ0612\n" +
            "KJ0006\n" +
            "KJ0033\n" +
            "KJ0639\n" +
            "AS0166\n" +
            "AS0224\n" +
            "AS0281\n" +
            "AS0282\n" +
            "AS0334\n" +
            "AS0335\n" +
            "AS0408\n" +
            "AS0519\n" +
            "AS0559\n" +
            "AS0817\n" +
            "AS0864\n" +
            "TH0416\n" +
            "TH0420\n" +
            "TH0618\n" +
            "TH0743\n" +
            "TH0054\n" +
            "TH0814\n" +
            "TH0903\n" +
            "TH0943\n" +
            "TH0942\n" +
            "TH0935\n" +
            "TH0631\n" +
            "TH0749\n" +
            "TH0222\n" +
            "TH0821\n" +
            "TH1015\n" +
            "TH1683\n" +
            "TH1987\n" +
            "TH1223\n" +
            "TH1701\n" +
            "TH0600\n" +
            "TH0708\n" +
            "TH0078\n" +
            "TH0221\n" +
            "TH1716\n" +
            "TH0692\n" +
            "TH0018\n" +
            "TH1124\n" +
            "TH0753\n" +
            "TH0113\n" +
            "TH0165\n" +
            "TH1702\n" +
            "TH1276\n" +
            "TH1899\n" +
            "TH0083\n" +
            "TH0851\n" +
            "TH0151\n" +
            "TH0882\n" +
            "TH0921\n" +
            "TH0458\n" +
            "TH1053\n" +
            "TH0716\n" +
            "TH2005\n" +
            "TH1465\n" +
            "TH0791\n" +
            "TH1350\n" +
            "TH0022\n" +
            "TH1541\n" +
            "TH1165\n" +
            "TH0480\n" +
            "TH0014\n" +
            "TH1648\n" +
            "TH1967\n" +
            "TH1169\n" +
            "TH1634\n" +
            "TH1630\n" +
            "TH1566\n" +
            "TH0894\n" +
            "TH0922\n" +
            "TH0726\n" +
            "TH1668\n" +
            "TH2167\n" +
            "TH0011\n" +
            "TH0871\n" +
            "TH1679\n" +
            "TH1290\n" +
            "TH1703\n" +
            "TH1351\n" +
            "TH0731\n" +
            "TH1961\n" +
            "TH0238\n" +
            "TH0016\n" +
            "TH0897\n" +
            "TH1439\n" +
            "TH1277\n" +
            "TH0010\n" +
            "TH1027\n" +
            "TH1377\n" +
            "TH0455\n" +
            "TH1673\n" +
            "TH0546\n" +
            "TH0853\n" +
            "KH0232\n" +
            "KH0235\n" +
            "KH0437\n" +
            "KH0656\n" +
            "KH0708\n" +
            "NK0111\n" +
            "NK0022\n" +
            "NK0021\n" +
            "NK0012\n" +
            "NK0099\n" +
            "NK0107\n" +
            "NK0252\n" +
            "FS0011\n" +
            "FS0020\n" +
            "FS0030\n" +
            "FS0041\n" +
            "FS0047\n" +
            "FS0157\n" +
            "FS0159\n" +
            "FS0200\n" +
            "FS0218\n" +
            "FS0231\n" +
            "FS0239\n" +
            "FS0300\n" +
            "FS0304\n" +
            "FS0321\n" +
            "FS0429\n" +
            "FS0710\n" +
            "FS0715\n" +
            "FS0731\n" +
            "FS0751\n" +
            "FS0752\n" +
            "FS0753\n" +
            "FS0754\n" +
            "FS0755\n" +
            "FS0762\n" +
            "FS0886\n" +
            "FS0898\n" +
            "FS0910\n" +
            "FS0911\n" +
            "FS0237\n" +
            "QN0022\n" +
            "QN0040\n" +
            "QN0059\n" +
            "QN0200\n" +
            "QN0207\n" +
            "QN0208\n" +
            "QN0245\n" +
            "QN0246\n" +
            "QM0209\n" +
            "QM0240\n" +
            "QM0241\n" +
            "QM0192\n" +
            "KM0081\n" +
            "KM0082\n" +
            "KM0215\n" +
            "KM0620\n" +
            "KM0622\n" +
            "KM0648\n" +
            "KM0967\n" +
            "KM0986\n" +
            "KM1124\n" +
            "KM0555\n" +
            "GL0440\n" +
            "GL0034\n" +
            "GL0300\n" +
            "GL0536\n" +
            "GL0530\n" +
            "GL0540\n" +
            "GL0358\n" +
            "GL0202\n" +
            "GL0219\n" +
            "GL0080\n" +
            "GL0108\n" +
            "GL0207\n" +
            "GL0017\n" +
            "GL0109\n" +
            "GL0332\n" +
            "GL0443\n" +
            "GL0329\n" +
            "MA0031\n" +
            "MA0682\n" +
            "MA0684\n" +
            "MA0685\n" +
            "MA0687\n" +
            "MA0688\n" +
            "MA0691\n" +
            "MA0697\n" +
            "MK0004\n" +
            "MK0059\n" +
            "MK0101\n" +
            "MK0107\n" +
            "MK0112\n" +
            "MK0200\n" +
            "MK0280\n" +
            "MK0102\n" +
            "HZ0006\n" +
            "HZ0019\n" +
            "HZ0020\n" +
            "HZ0021\n" +
            "HZ0022\n" +
            "HZ0023\n" +
            "HZ0024\n" +
            "HZ0044\n" +
            "HZ0045\n" +
            "HZ0046\n" +
            "HZ0047\n" +
            "HZ0069\n" +
            "HZ0089\n" +
            "HZ0095\n" +
            "HZ0096\n" +
            "HZ0130\n" +
            "HZ0131\n" +
            "HZ0144\n" +
            "HZ0223\n" +
            "HZ0268\n" +
            "HZ0269\n" +
            "HZ0270\n" +
            "HZ0271\n" +
            "HZ0300\n" +
            "HZ0431\n" +
            "HZ0432\n" +
            "HZ0522\n" +
            "HZ0523\n" +
            "HZ0544\n" +
            "HZ0608\n" +
            "HZ0380\n" +
            "HZ0903\n" +
            "HZ0609\n" +
            "HN0003\n" +
            "HN0086\n" +
            "HN0242\n" +
            "HN0318\n").split("\n");

        DateTime d;
        try {
            d = new DateTime("1396-12-14");
        } catch (DateTimeException e) {
            ui.addErrorMessage(e).write();
            return;
        }

        ui.write();
        try {
            for (String item : x) {
                QueryBuilder q = new QueryBuilder(new RadioMetricFlow());
                q.condition().equal("site.code", item);

                for (Dto dd : CommonRepoMongo.getData(q)) {
                    RadioMetricFlow f = (RadioMetricFlow) dd;

                    if (f.state != null) {

                        for (State state : f.state) {
                            if (state.state.equals(RadioMetricFlowState.Approved)) {
                                state.dateTime = d;
                            }
                        }


                        f.measurementDateTime = d;
                        f.skipBeforeUpdate();
                        CommonRepoMongo.update(f);
                        ui.addMessage(f.site.code).write();
                    }
                }
            }
        } catch (DatabaseException | NoContentException ee) {
            ui.addErrorMessage(ee).write();
        }

        ui.addMessage("< < <").write();

    }



    public void cityFix(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        try {
            List<City> items = CommonRepoMongo.getData(new City());

            for (City c: items) {
                String oldName = c.name.get("fa");
                if (oldName == null || !oldName.contains("(")) {
                    continue;
                }

                String[] parts = StringUtil.split(oldName, '(');
                String newName = parts[0];
                c.name.put("fa", newName);
                ui.addMessage(oldName + " >> " + newName).write();

                CommonRepoMongo.update(c);
            }

            for (City c: items) {
                String oldName = c.name.get("en");
                if (oldName == null || !oldName.contains("(")) {
                    continue;
                }

                String[] parts = StringUtil.split(oldName, '(');
                String newName = parts[0];
                c.name.put("en", newName);
                ui.addMessage(oldName + " >> " + newName).write();

                CommonRepoMongo.update(c);
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }


    public void fixComplainImages(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        try {
            List<RadioMetricFlow> items = CommonRepoMongo.getData(new RadioMetricFlow());

            for (RadioMetricFlow f: items) {
                if (RadioMetricComplain.isEmpty(f.complain)) {
                    continue;
                }
                if (f.complain.imageUrl == null) {
                    continue;
                }

                String complainPath = Param.RADIO_METRIC_FILES + f.site.code + "/complain/";
                if (!FileUtil.exists(complainPath)) {
                    continue;
                }

                Arrays.stream(new File(complainPath)
                    .listFiles((file, p) -> p.endsWith(".png"))).forEach(file -> {
                    FileUtil.copy(file.getAbsolutePath(), "/opt/backup/" + f.id + "---" + f.complain.id + "---" + file.getName());
                });

                String imagePath = StringUtil.replace(
                    f.complain.imageUrl,
                    Param.RADIO_METRIC_URL + f.site.code + "/complain/",
                    complainPath
                );

                if (FileUtil.exists(imagePath)) {
                    FileUtil.copy(imagePath, f.complain.getImageFilePath(false));
                    ui.addMessage(imagePath + " >(1)> " + f.complain.getImageFilePath(false)).write();
                } else {
                    Arrays.stream(new File(complainPath)
                        .listFiles((file, p) -> p.endsWith(".png"))).forEach(file -> {

                        if (StringUtil.isNotEmpty(f.complain.ccnumber) && !f.complain.ccnumber.equals("0") && file.getName().contains(f.complain.ccnumber)) {
                            FileUtil.copy(file.getAbsolutePath(), f.complain.getImageFilePath(false));
                            ui.addMessage(file.getAbsolutePath() + " >(2)> " + f.complain.getImageFilePath(false)).write();
                        }
                    });
                }

                if (FileUtil.exists(f.getPath())) {
                    Arrays.stream(new File(f.getPath()).listFiles((file, p) -> p.endsWith(".png"))).forEach(file -> {
                        if (file.getName().contains("درخواست")) {
                            FileUtil.move(
                                file.getAbsolutePath(),
                                "/opt/backup/" + f.id + "------" + file.getName()
                            );
                        }
                    });
                }

                if (FileUtil.exists(f.complain.getImageFilePath(false))) {
                    f.complain.imageUrl = f.complain.getImageUrl(false);
                    CommonRepoMongo.update(f);
                    CommonRepoMongo.update(f.complain);
                    ui.addMessage(f.site.code).write();
                } else {
                    ui.addErrorMessage(f.site.code).write();
                }
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }


    public void checkComplains(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        try {
            List<RadioMetricFlow> items = CommonRepoMongo.getData(new RadioMetricFlow());
            List<RadioMetricComplain> complains = CommonRepoMongo.getData(new RadioMetricComplain());

            Set<Long> cc = new HashSet<>(complains.size());
            for (RadioMetricComplain c : complains) {
                cc.add(c.id);
            }

            for (RadioMetricFlow f: items) {
                if (!RadioMetricComplain.isEmpty(f.complain) && !cc.contains(f.complain.id)) {
                    ui.addMessage(f.id + " " + f.site.code).write();
                }
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }

    public void check(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        String dtos = params.getString("dtos");

        ui.beginFormGet();
        ui.addTextArea("dtos", "dtos", dtos);
        ui.addSubmit();
        ui.containerEnd().write();

        if (dtos == null) {
            return;
        }

        try {
            List<Site> items = CommonRepoMongo.getData(new Site());

            String[] parts = StringUtil.split(dtos, ',');
            for (String part : parts) {
                String[] dtoId = StringUtil.split(part, ':');
                ui.addHeading(2, dtoId[0]).write();

                Dto dto = DtoDictionary.get(dtoId[0]).getDtoInstance();

                QueryBuilder q = new QueryBuilder(dto);
                if (dtoId[1].contains("=")) {
                    q.condition().equal("id", StringUtil.toLong(StringUtil.remove(dtoId[1], '=')));
                } else {
                    q.condition().greaterThanEqual("id", StringUtil.toLong(dtoId[1]));
                }

                List<Dto> dicItems = CommonRepoMongo.getData(q);

                String dtoIdFieldName = StringUtil.firstCharToLowerCase(dtoId[0]) + "Id";
                for (Site s : items) {
                    for (Dto d : dicItems) {
                        if (s.getPropertyValue(dtoIdFieldName) == d.getId()) {
                            ui.addMessage(s.id + " " + s.code + " > " + d.getId()).write();
                        }
                    }
                }
            }

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }

        ui.write();
    }


    public void fix(Params params, HttpServletResponse response) throws FinishException {
        WebUi ui = Admin.getUi(Locale.getString(VantarKey.ADMIN_IMPORT), params, response, true);

        Site s = new Site();
        RadioMetricFlow r = new RadioMetricFlow();
        HseAuditQuestionnaire h = new HseAuditQuestionnaire();
        RadioMetricComplain c = new RadioMetricComplain();
        ui.addMessage("11").write();

        try {
            int i = 0;
            List<RadioMetricComplain> cc = CommonRepoMongo.getData(c);

            for (RadioMetricComplain ccc : cc) {
                i++;
                if (i %1000 == 0) ui.addMessage("1000").write();
                CommonRepoMongo.update(ccc);
            }

            List<Site> ss = CommonRepoMongo.getData(s);

            ui.addMessage("11").write();
ui.addMessage("11").write();
            for (Site sss : ss) {
                i++;
                if (i %1000 == 0) ui.addMessage("1000").write();
                CommonRepoMongo.update(sss);
            }
            ui.addMessage("11").write();

            List<RadioMetricFlow> rr = CommonRepoMongo.getData(r);

            i = 0;
            for (RadioMetricFlow rrr : rr) {
                i++;
                if (i %1000 == 0) ui.addMessage("1000").write();
                CommonRepoMongo.update(rrr);
            }
            ui.addMessage("11").write();

            ui.addMessage("11").write();
            List<HseAuditQuestionnaire> hh = CommonRepoMongo.getData(h);

            i = 0;
            for (HseAuditQuestionnaire hhh : hh) {
                i++;
                if (i %1000 == 0) ui.addMessage("1000").write();
                CommonRepoMongo.update(hhh);
            }
            ui.addMessage("11").write();

        } catch (DatabaseException | NoContentException e) {
            ui.addErrorMessage(e);
        }


    }

    private static String ymToString(int ym) {
        return (ym / 100) + "-" + ExportCommon.numberToMonth(ym % 100);
    }


    public void index(Params params, HttpServletResponse response) {
        QueryBuilder q = new QueryBuilder(new Test9());
        q.condition().in("test7.test1.id", 112);

        try {
            log.error(">>>>{}", CommonRepoMongo.getData(q));
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (NoContentException e) {
            e.printStackTrace();
        }


        if (true) return;

        DtoDictionary.Info dt = DtoDictionary.get("Test7");

        Test1 test1 = new Test1();
        test1.id = 1L;
        test1.name = "AA";

        Test1 test12 = new Test1();
        test12.id = 2L;
        test12.name = "BB";

        Test1 test13 = new Test1();
        test13.id = 3L;
        test13.name = "CC";

        Test1 test14 = new Test1();
        test14.id = 4L;
        test14.name = "DD";

        Test1 test15 = new Test1();
        test15.id = 5L;
        test15.name = "EE";

        Test1 test16 = new Test1();
        test16.id = 6L;
        test16.name = "FF";

        Test1 test17 = new Test1();
        test17.id = 7L;
        test17.name = "GG";

        Test1 test18 = new Test1();
        test18.id = 8L;
        test18.name = "HH";

        Test1 test19 = new Test1();
        test19.id = 9L;
        test19.name = "II";

        Test1 test20 = new Test1();
        test20.id = 10L;
        test20.name = "JJ";

        Test1 test21 = new Test1();
        test21.id = 11L;
        test21.name = "KK";

        Test1 test22 = new Test1();
        test22.id = 12L;
        test22.name = "LL";

        Test1 test23 = new Test1();
        test23.id = 13L;
        test23.name = "MM";

        Test1 test24 = new Test1();
        test24.id = 14L;
        test24.name = "NN";

        Test1 test25 = new Test1();
        test25.id = 15L;
        test25.name = "OO";

        Test1 test26 = new Test1();
        test26.id = 16L;
        test26.name = "PP";

        Test1 test27 = new Test1();
        test27.id = 17L;
        test27.name = "QQ";


////
        Test2 test2 = new Test2();
        test2.name = "AAA";
        test2.test1Id = 1L;
////
        Test3 test3 = new Test3();
        test3.name = "AAAA";
        test3.test1Ids = new ArrayList<>();
        test3.test1Ids.add(2L);
        test3.test1Ids.add(3L);
////
        Test4 test4 = new Test4();
        test4.name = "AAAAA";
        test4.test1 = test14;
////
        Test5 test5 = new Test5();
        test5.name = "AAAAA";
        test5.test1s = new ArrayList<>();
        test5.test1s.add(test15);
        test5.test1s.add(test16);
////
        Test7 test7 = new Test7();
        test7.name = "AAAAAAA";
        test7.test1Id = 7L;
        test7.test1 = test18;
        Test6 test6 = new Test6();
        test6.name = "AAAAAA";
        test6.test7 = test7;
////
        Test7 test71 = new Test7();
        test71.name = "BBBBBBB";
        test71.test1Id = 10L;
        test71.test1 = test20;
        Test6 test61 = new Test6();
        test61.name = "BBBBBB";
        test61.test7 = test71;
        Test8 test8 = new Test8();
        test8.name = "AAAAAAAA";
        test8.test6 = test61;
////
        Test7 test72 = new Test7();
        test72.name = "CCCCCCC";
        test72.test1Id = 11L;
        test72.test1 = test12;
        Test7 test73 = new Test7();
        test73.name = "DDDDDDD";
        test73.test1Id = 13L;
        test73.test1 = test14;
        Test9 test9 = new Test9();
        test9.name = "AAAAAAAAA";
        test9.test7 = new ArrayList<>();
        test9.test7.add(test72);
        test9.test7.add(test73);
////
        Test7 test75 = new Test7();
        test75.name = "FFFFFFF";
        test75.test1Id = 15L;
        test75.test1 = test16;
        Test7 test76 = new Test7();
        test76.name = "FFFFFFF";
        test76.test1Id = 17L;
        test76.test1 = test18;
        Test6 test64 = new Test6();
        test64.name = "NNNNNN";
        test64.test7 = test75;
        Test6 test65 = new Test6();
        test65.name = "GGGGGG";
        test65.test7 = test76;
        Test10 test100 = new Test10();
        test100.name = "AAAAAAAAAA";
        test100.test6 = new ArrayList<>();
        test100.test6.add(test64);
        test100.test6.add(test65);

//
//        try {
//            CommonRepoMongo.insert(test1);
//            CommonRepoMongo.insert(test12);
//            CommonRepoMongo.insert(test13);
//            CommonRepoMongo.insert(test14);
//            CommonRepoMongo.insert(test15);
//            CommonRepoMongo.insert(test16);
//            CommonRepoMongo.insert(test17);
//            CommonRepoMongo.insert(test18);
//            CommonRepoMongo.insert(test19);
//            CommonRepoMongo.insert(test20);
//            CommonRepoMongo.insert(test21);
//            CommonRepoMongo.insert(test22);
//            CommonRepoMongo.insert(test23);
//            CommonRepoMongo.insert(test24);
//            CommonRepoMongo.insert(test25);
//            CommonRepoMongo.insert(test26);
//            CommonRepoMongo.insert(test27);
//
//            CommonRepoMongo.insert(test2);
//            CommonRepoMongo.insert(test3);
//            CommonRepoMongo.insert(test4);
//            CommonRepoMongo.insert(test5);
//            CommonRepoMongo.insert(test6);
//            CommonRepoMongo.insert(test8);
//            CommonRepoMongo.insert(test9);
//            CommonRepoMongo.insert(test100);
//
//
//        } catch (DatabaseException e) {
//            e.printStackTrace();
//        }

        List<DataDependency.Dependants> items;

//        items = DataDependency.getDependencies(new Test1(), 1L);
//        log.error(">>>>>{} > 1", items);
//        items = DataDependency.getDependencies(new Test1(), 2L);
//        log.error(">>>>>{} > 1", items);
//        items = DataDependency.getDependencies(new Test1(), 3L);
//        log.error(">>>>>{} > 1", items);
//
        //items = DataDependency.getDependencies(new Test1(), 4L);
        //log.error(">>>>>{} > 0", items);




//        items = DataDependency.getDependencies(new Test1(), 5L);
//        log.error(">>>>>{} > 0", items);
//
//        items = DataDependency.getDependencies(new Test1(), 6L);
//        log.error(">>>>>{} > 0", items);
//
//        items = DataDependency.getDependencies(new Test1(), 7L);
//        log.error(">>>>>{} > 1", items);

        items = DataDependency.getDependencies(new Test1(), 8L);
        log.error(">>>>>{} > 0", items);

        items = DataDependency.getDependencies(new Test1(), 9L);
        log.error(">>>>>{} > 1", items);
        items = DataDependency.getDependencies(new Test1(), 10L);
        log.error(">>>>>{} > 0", items);

        if(1==1)return;


        items = DataDependency.getDependencies(new Test1(), 11L);
        log.error(">>>>>{} > 1", items);
        items = DataDependency.getDependencies(new Test1(), 12L);
        log.error(">>>>>{} > 0", items);
        items = DataDependency.getDependencies(new Test1(), 13L);
        log.error(">>>>>{} > 1", items);
        items = DataDependency.getDependencies(new Test1(), 14L);
        log.error(">>>>>{} > 0", items);

        items = DataDependency.getDependencies(new Test1(), 15L);
        log.error(">>>>>{} > 1", items);
        items = DataDependency.getDependencies(new Test1(), 16L);
        log.error(">>>>>{} > 0", items);
        items = DataDependency.getDependencies(new Test1(), 17L);
        log.error(">>>>>{} > 1", items);
        items = DataDependency.getDependencies(new Test1(), 18L);
        log.error(">>>>>{} > 0", items);



        //        try {
//            ImageIO.write(
//                Scalr.resize(ImageIO.read(new File("/home/lynx/downloads/a.jpg")), 150),
//                "jpg",
//                new File("/home/lynx/downloads/a-small.jpg")
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (1==1)return;
//
//        try {
////            BufferedImage img = ImageIO.read(new File("/home/lynx/downloads/test.jpg"));
//  //          BufferedImage scaledImg = Scalr.resize(img, 150);
//    //        ImageIO.write(scaledImg, "jpg", new File("/home/lynx/downloads/test_thumb.jpg"));
//
//            PictureEdit i = new PictureEdit("/home/lynx/downloads/f1.jpg");
//            i.resize(150);
//            i.write("/home/lynx/downloads/test_thumb.jpg");
//
//        } catch (IOException | ImageReadException | ImageWriteException e) {
//            e.printStackTrace();
//        }
    }

}