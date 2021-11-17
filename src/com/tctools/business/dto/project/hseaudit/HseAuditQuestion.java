package com.tctools.business.dto.project.hseaudit;

import com.vantar.database.dto.*;
import java.util.*;

@Cache
@Mongo
@Index("order:1")
public class HseAuditQuestion extends DtoBase {

    public Long id;

    @Default("1")
    @Required
    public Integer order;

    @Default("true")
    @Required
    public Boolean enabled;

    @Required
    public HseAuditQuestionType questionType;
    @Required
    public HseAuditQuestionSignificance significance;
    @Required
    @Localized
    public Map<String, String> title;
    public String comments;

    @Required
    public Boolean requiresPhoto;
    @Required
    public Boolean requiresAnswer;

    @StoreString
    public Map<String, Map<String, String>> optionLabel;


    @Storage("HseAuditQuestion")
    public static class Viewable extends DtoBase {

        public Long id;
        public Integer order;

        public HseAuditQuestionType questionType;
        public HseAuditQuestionSignificance significance;
        @DeLocalized
        public String title;
        public String comments;

        public Boolean requiresPhoto;
        public Boolean requiresAnswer;

        public Map<String, Map<String, String>> optionLabel;
    }
}
