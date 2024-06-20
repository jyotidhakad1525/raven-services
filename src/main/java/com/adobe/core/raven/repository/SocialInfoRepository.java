package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.model.FooterRepository;
import com.adobe.core.raven.dto.model.SocialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class SocialInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public SocialRepository insert(SocialRepository cgenRepository) {
		SocialRepository cgenInfo = this.mongoTemplate.save(cgenRepository, "SocialRepository");
	
		return cgenInfo;
	}


	public SocialRepository get(String id) {

		SocialRepository masterInfo = mongoTemplate.findById(id, SocialRepository.class);

		return masterInfo;
	}

	public List<SocialRepository> findAll() {

		List<SocialRepository> socialRepositoryList = mongoTemplate.findAll(SocialRepository.class);

		return socialRepositoryList;
	}


	public List<SocialRepository> getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<SocialRepository> contentInfos = mongoTemplate.find(query,SocialRepository.class);

		if(contentInfos != null
			&& contentInfos.size() > 0) {
			return contentInfos;
		}

		return null;
	}

}
