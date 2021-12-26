package com.tctools.business.model.project.container;

import com.tctools.business.dto.project.container.Project;
import com.tctools.business.dto.site.Site;
import com.vantar.business.*;
import com.vantar.exception.*;
import com.vantar.locale.VantarKey;
import com.vantar.util.number.NumberUtil;
import com.vantar.web.*;
import java.util.List;


public class ProjectModel {

    public static ResponseMessage insert(Params params) throws InputException, ServerException {
        return CommonModelMongo.insertJson(params, new Project());
    }

    public static ResponseMessage update(Params params) throws InputException, ServerException {
        return CommonModelMongo.updateJsonStrict(params, new Project());
    }

    public static ResponseMessage delete(Params params) throws InputException, ServerException {
        return CommonModelMongo.delete(params, new Project());
    }

    public static Project.Viewable get(Params params) throws ServerException, NoContentException, InputException {
        Project.Viewable project = new Project.Viewable();
        project.id = params.getLong("id");

        if (NumberUtil.isIdInvalid(project.id)) {
            throw new InputException(VantarKey.INVALID_ID, "id (Project.id)");
        }

        try {
            return CommonRepoMongo.getFirst(project, params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }

    public static List<Site.Viewable> getAll(Params params) throws ServerException, NoContentException {
        try {
            return CommonRepoMongo.getAll(new Project.Viewable(), params.getLang());
        } catch (DatabaseException e) {
            throw new ServerException(VantarKey.FETCH_FAIL);
        }
    }
}
