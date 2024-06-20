package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.repo.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MasterInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public MasterRepository insert(MasterRepository masterRepository) {
		MasterRepository mastersRepository = this.mongoTemplate.save(masterRepository, "masterRepository");
	
		return mastersRepository;
	}

	public void insertAll(List<MasterRepository> masterRepositoryList) {
		for(MasterRepository masterInfo: masterRepositoryList){
			this.mongoTemplate.save(masterInfo, "MasterRepository");
		}
	}

	public MasterRepository get(String id) {

		MasterRepository masterInfo = mongoTemplate.findById(id, MasterRepository.class);

		return masterInfo;
	}

	public MasterRepository getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<MasterRepository> contentInfos = mongoTemplate.find(query,MasterRepository.class);

		if(contentInfos != null
			&& contentInfos.size() > 0) {
			return contentInfos.get(0);
		}

		return null;
	}

}
