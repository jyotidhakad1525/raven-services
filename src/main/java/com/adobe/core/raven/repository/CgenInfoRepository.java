package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.cgen.CgenRepository;
import com.adobe.core.raven.dto.repo.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CgenInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public CgenRepository insert(CgenRepository cgenRepository) {
		CgenRepository cgenInfo = this.mongoTemplate.save(cgenRepository, "cgenRepository");
	
		return cgenInfo;
	}


	public CgenRepository get(String id) {

		CgenRepository masterInfo = mongoTemplate.findById(id, CgenRepository.class);

		return masterInfo;
	}

	public CgenRepository getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<CgenRepository> contentInfos = mongoTemplate.find(query,CgenRepository.class);

		if(contentInfos != null
			&& contentInfos.size() > 0) {
			return contentInfos.get(0);
		}

		return null;
	}

	public CgenRepository getByHash(String hash) {

		//hashCode
		Query query = new Query();

		Criteria criteria = new Criteria();

		criteria.and("md5").is(hash);

		query.addCriteria(criteria);

		List<CgenRepository> cgenRepositories = this.mongoTemplate.find(query, CgenRepository.class);

		return  cgenRepositories.size() > 0 ? cgenRepositories.get(0) : null;
	}
}
