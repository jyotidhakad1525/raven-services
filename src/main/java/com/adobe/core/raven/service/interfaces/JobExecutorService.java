package com.adobe.core.raven.service.interfaces;


import com.adobe.core.raven.dto.message.JobMessageRequest;
import com.adobe.core.raven.dto.response.JobResponse;

import java.util.List;

public interface JobExecutorService {

   void executeQAJob(String jobId, String stepId);
   JobResponse executeRetestQA(String jobId, List<JobMessageRequest> htmls);

   }
