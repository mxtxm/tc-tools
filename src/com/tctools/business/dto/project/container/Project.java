package com.tctools.business.dto.project.container;

import com.vantar.database.dto.*;
import com.vantar.util.datetime.DateTime;
import java.util.Map;

@Mongo
public class Project extends DtoBase {

    public Long id;

    @Required
    public ProjectType type;

    @Localized
    @Required
    public Map<String, String> name;

    @Localized
    @Required
    public Map<String, String> employer;

    @Localized
    @Required
    public Map<String, String> executor;

    @Localized
    public Map<String, String> description;

    public Map<String, String> properties;

    @Timestamp
    @Required
    public DateTime startTime;

    @Timestamp
    public DateTime expectedFinishTime;

    @Timestamp
    public DateTime finishTime;



    @Storage("Project")
    public static class Viewable extends DtoBase {

        public Long id;
        public ProjectType type;
        @DeLocalized
        public String name;
        @DeLocalized
        public String employer;
        @DeLocalized
        public String executor;
        @DeLocalized
        public String description;
        public Map<String, String> properties;
        public DateTime startTime;
        public DateTime expectedFinishTime;
        public DateTime finishTime;
    }
}