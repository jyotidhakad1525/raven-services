package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.message.MessageRepository;
import com.adobe.core.raven.dto.model.FooterRepository;
import com.adobe.core.raven.dto.repo.Mirror;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MirrorInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public Mirror insert(Mirror mirrorInfo) {
		Mirror mirror = this.mongoTemplate.save(mirrorInfo, "mirror");
	
		return mirror;
	}

	public void insertAll(List<Mirror> sapCodeModels) {
		for(Mirror localeInfo: sapCodeModels){
			this.mongoTemplate.save(localeInfo, "mirror");
		}
	}

	public Mirror get(String id) {

		Mirror masterInfo = mongoTemplate.findById(id, Mirror.class);

		return masterInfo;
	}

	public Mirror getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<Mirror> contentInfos = mongoTemplate.find(query, Mirror.class);

		if(contentInfos != null
				&& contentInfos.size() > 0) {
			return contentInfos.get(0);
		}

		return null;
	}
}
