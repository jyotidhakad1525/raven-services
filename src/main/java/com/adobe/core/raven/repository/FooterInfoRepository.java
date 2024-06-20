package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.cgen.CgenRepository;
import com.adobe.core.raven.dto.model.FooterRepository;
import com.adobe.core.raven.dto.model.SocialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class FooterInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public FooterRepository insert(FooterRepository cgenRepository) {
		FooterRepository cgenInfo = this.mongoTemplate.save(cgenRepository, "FooterRepository");
	
		return cgenInfo;
	}


	public FooterRepository get(String id) {

		FooterRepository masterInfo = mongoTemplate.findById(id, FooterRepository.class);

		return masterInfo;
	}

	public List<FooterRepository> findAll() {

		List<FooterRepository> footerRepositoryList = mongoTemplate.findAll(FooterRepository.class);

		return footerRepositoryList;
	}

	public FooterRepository getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<FooterRepository> contentInfos = mongoTemplate.find(query,FooterRepository.class);

		if(contentInfos != null
			&& contentInfos.size() > 0) {
			return contentInfos.get(0);
		}

		return null;
	}

}
