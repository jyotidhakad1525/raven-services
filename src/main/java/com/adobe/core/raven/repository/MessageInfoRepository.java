package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MessageInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public MessageRepository insert(MessageRepository messageRepository) {
		MessageRepository messageInfo = this.mongoTemplate.save(messageRepository, "messageRepository");

		return messageInfo;
	}

	public MessageRepository get(String id) {

		MessageRepository qaInfo = mongoTemplate.findById(id, MessageRepository.class);

		return qaInfo;
	}

	public MessageRepository getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<MessageRepository> contentInfos = mongoTemplate.find(query, MessageRepository.class);

		if(contentInfos != null
			&& contentInfos.size() > 0) {
			return contentInfos.get(0);
		}

		return null;
	}

	public MessageRepository getByHash(String hash) {

		//hashCode
		Query query = new Query();

		Criteria criteria = new Criteria();

		criteria.and("md5").is(hash);

		query.addCriteria(criteria);

		List<MessageRepository> contentStorageInfo = this.mongoTemplate.find(query, MessageRepository.class);

		return  contentStorageInfo.size() > 0 ? contentStorageInfo.get(0) : null;
	}

}
