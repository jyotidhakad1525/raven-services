package com.adobe.core.raven.repository;

import com.adobe.core.raven.dto.job.MasterJob;
import com.adobe.core.raven.dto.qa.QaRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MasterJobInfoRepository {

	@Autowired
	public MongoTemplate mongoTemplate;

	public MasterJob insert(MasterJob masterJob) {
		MasterJob masterJobInfo = this.mongoTemplate.save(masterJob, "masterJob");
	
		return masterJobInfo;
	}

	public MasterJob get(String id) {

		MasterJob masterJob = mongoTemplate.findById(id, MasterJob.class);

		return masterJob;
	}

	public MasterJob getListByField(String field, String value) {
		Query query = new Query();
		Criteria criteria = new Criteria();

		criteria.and(field).is(value);
		query.addCriteria(criteria);
		List<MasterJob> contentInfos = mongoTemplate.find(query, MasterJob.class);

		if(contentInfos != null
			&& contentInfos.size() > 0) {
			return contentInfos.get(0);
		}

		return null;
	}

	public Boolean updateStatus(String jobId, String stepId, String status) {

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(jobId)
				.and("steps._id").is(stepId));
		Update update = new Update();
		update.set("steps.$.state", status);

		try {

			UpdateResult writeResult = this.mongoTemplate.upsert(query, update, MasterJob.class);

			if (writeResult != null) {

				System.out.println("Update successful :"
						+ writeResult.toString());

			}
		} catch (DataIntegrityViolationException die) {
			System.out.println("Update failed ====>" + die.getMessage());
		}

		return true;

	}

	public Boolean updateJobState(String jobId, String status) {

		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(jobId));
		Update update = new Update();
		update.set("state", status);

		try {

			UpdateResult writeResult = this.mongoTemplate.upsert(query, update, MasterJob.class);

			if (writeResult != null) {

				System.out.println("Update successful :"
						+ writeResult.toString());

			}
		} catch (DataIntegrityViolationException die) {
			System.out.println("Update failed ====>" + die.getMessage());
		}

		return true;

	}

}
