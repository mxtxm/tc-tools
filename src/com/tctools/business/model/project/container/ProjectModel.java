package com.tctools.business.model.project.container;

import com.tctools.business.dto.project.container.Project;
import com.vantar.business.ModelMongo;
import com.vantar.exception.VantarException;
import com.vantar.web.*;
import java.util.List;


public class ProjectModel {

    public static ResponseMessage insert(Params params) throws VantarException {
        return ModelMongo.insertJson(params, new Project());
    }

    public static ResponseMessage update(Params params) throws VantarException {
        return ModelMongo.updateJson(params, new Project());
    }

    public static ResponseMessage delete(Params params) throws VantarException {
        return ModelMongo.delete(params, new Project());
    }

    public static Project.Viewable get(Params params) throws VantarException {
        return ModelMongo.getById(params, new Project.Viewable());
    }

    public static List<Project.Viewable> getAll(Params params) throws VantarException {
        return ModelMongo.getAll(new Project.Viewable(), params.getLang());
    }
}
