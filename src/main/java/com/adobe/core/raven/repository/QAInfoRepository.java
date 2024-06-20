package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.qa.CheckList;
import com.adobe.core.raven.dto.qa.QaItem;
import com.adobe.core.raven.dto.qa.QaRepository;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.glassfish.grizzly.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class QAInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public QaRepository insert(QaRepository qaInfoRepository) {
		QaRepository qaInfo = this.mongoTemplate.save(qaInfoRepository, "qaRepository");
	
		return qaInfo;
	}

	public QaRepository get(String id) {

		QaRepository qaInfo = mongoTemplate.findById(id, QaRepository.class);

		return qaInfo;
	}



		public Boolean updateCheckList(String stepId, String messageId, CheckList checkList) {

			Query query = new Query();
			query.addCriteria(Criteria.where("_id").is(stepId)
					.and("items._id").is(messageId));
			Update update = new Update();
			update.addToSet("items.$.checkList", checkList);

			try {

				UpdateResult writeResult = this.mongoTemplate.upsert(query, update, QaRepository.class);

				if (writeResult != null) {

					System.out.println("Update successful :"
							+ writeResult.toString());

				}
			} catch (DataIntegrityViolationException die) {
				System.out.println("Update failed ====>" + die.getMessage());
			}

			return true;

		}

	public Boolean updateStatus(String stepId, String messageId, String status) {

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(stepId)
				.and("items._id").is(messageId));
		Update update = new Update();
		update.set("items.$.status", status);

		try {

			UpdateResult writeResult = this.mongoTemplate.upsert(query, update, QaRepository.class);

			if (writeResult != null) {

				System.out.println("Update successful :"
						+ writeResult.toString());

			}
		} catch (DataIntegrityViolationException die) {
			System.out.println("Update failed ====>" + die.getMessage());
		}

		return true;

	}

	public QaRepository getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<QaRepository> contentInfos = mongoTemplate.find(query, QaRepository.class);

		if(contentInfos != null
			&& contentInfos.size() > 0) {
			return contentInfos.get(0);
		}

		return null;
	}

}
