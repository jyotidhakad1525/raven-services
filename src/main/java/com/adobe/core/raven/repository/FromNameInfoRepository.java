package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.cgen.CgenRepository;
import com.adobe.core.raven.dto.repo.FromNameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FromNameInfoRepository {

    @Autowired
    public MongoTemplate mongoTemplate;

    public FromNameInfo insert(FromNameInfo fromNameInfo) {
        FromNameInfo insertedFromNameInfo = this.mongoTemplate.save(fromNameInfo, "fromNameInfo");

        return insertedFromNameInfo;
    }


    public FromNameInfo get(String id) {

        FromNameInfo fromNameInfo = mongoTemplate.findById(id, FromNameInfo.class);

        return fromNameInfo;
    }

    public FromNameInfo getListByField(String field, String value) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        criteria.and(field).is(value);
        query.addCriteria(criteria);
        List<FromNameInfo> fromNameInfos = mongoTemplate.find(query,FromNameInfo.class);

        if(fromNameInfos != null
                && fromNameInfos.size() > 0) {
            return fromNameInfos.get(0);
        }

        return null;
    }
}
