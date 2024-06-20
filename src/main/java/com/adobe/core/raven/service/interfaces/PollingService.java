package com.adobe.core.raven.service.interfaces;


import com.adobe.core.raven.dto.QAResponse;
import com.adobe.core.raven.dto.message.JobRequest;

public interface PollingService {

   QAResponse pollRaven(String jobId, String stepId);
   QAResponse rePollRaven(String jobId, JobRequest jobRequest );

   }
