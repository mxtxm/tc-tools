package com.tctools.business.model.project.container;

import com.tctools.business.dto.project.container.Project;
import com.vantar.business.ModelCommon;
import com.vantar.database.common.Db;
import com.vantar.exception.VantarException;
import com.vantar.web.*;
import java.util.List;


public class ProjectModel {

    public static ResponseMessage insert(Params params) throws VantarException {
        return Db.modelMongo.insert(new ModelCommon.Settings(params, new Project()).isJson().mutex(false));
    }

    public static ResponseMessage update(Params params) throws VantarException {
        return Db.modelMongo.update(new ModelCommon.Settings(params, new Project()).isJson());
    }

    public static ResponseMessage delete(Params params) throws VantarException {
        return Db.modelMongo.delete(new ModelCommon.Settings(params, new Project()));
    }

    public static Project.Viewable get(Params params) throws VantarException {
        return Db.modelMongo.getById(params, new Project.Viewable());
    }

    public static List<Project.Viewable> getAll(Params params) throws VantarException {
        return Db.modelMongo.getAll(new Project.Viewable(), params.getLang());
    }
}
