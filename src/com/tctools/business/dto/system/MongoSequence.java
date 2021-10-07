package com.tctools.business.dto.system;

import com.vantar.database.dto.DtoBase;
import com.vantar.database.nosql.mongo.Mongo;


@com.vantar.database.dto.Mongo
public class MongoSequence extends DtoBase {

    public static final String STORAGE = Mongo.Sequence.COLLECTION;

    public Long id;

    public String n;

    public Long c;

}