package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.repo.MasterRepository;
import com.adobe.core.raven.dto.repo.MasterRepositoryTechMarketing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MasterInfoRepositoryTechMarketing {
    @Autowired
    public MongoTemplate mongoTemplate;

//    public MasterRepositoryTechMarketing get(String id) {
//        MasterRepositoryTechMarketing masterRepositoryTechMarketing
//                = mongoTemplate.findById(id, MasterRepositoryTechMarketing.class);
//        return masterRepositoryTechMarketing;
//    }

    public MasterRepositoryTechMarketing getListByField(String field, String value) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        criteria.and(field).is(value);
        query.addCriteria(criteria);
        List<MasterRepositoryTechMarketing> contentInfos =
                mongoTemplate.find(query,MasterRepositoryTechMarketing.class);

        if(contentInfos != null
                && contentInfos.size() > 0) {
            return contentInfos.get(0);
        }

        return null;
    }
}
