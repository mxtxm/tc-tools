package com.tctools.business.dto.user;

import com.vantar.database.dto.*;
import com.vantar.util.datetime.DateTime;
import java.util.Map;


@Mongo
@Index("userId:1")
public class UserLog extends DtoBase {

    public Long id;

    @Required
    @Depends(User.class)
    public Long userId;

    @Required
    public String subjectClass;
    public Long subjectId;

    @Required
    public Action action;

    public Map<String, String> headers;

    public String url;

    public String ip;

    @Timestamp
    @CreateTime
    public DateTime createT;

}