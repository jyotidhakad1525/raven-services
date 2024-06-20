package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.model.FooterRepository;
import com.adobe.core.raven.dto.model.LegalLinksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class LegalInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public LegalLinksRepo insert(LegalLinksRepo cgenRepository) {
		LegalLinksRepo cgenInfo = this.mongoTemplate.save(cgenRepository, "LegalLinksRepo");
	
		return cgenInfo;
	}


	public LegalLinksRepo get(String id) {

		LegalLinksRepo masterInfo = mongoTemplate.findById(id, LegalLinksRepo.class);

		return masterInfo;
	}

	public LegalLinksRepo getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<LegalLinksRepo> contentInfos = mongoTemplate.find(query,LegalLinksRepo.class);

		if(contentInfos != null
			&& contentInfos.size() > 0) {
			return contentInfos.get(0);
		}

		return null;
	}

}
