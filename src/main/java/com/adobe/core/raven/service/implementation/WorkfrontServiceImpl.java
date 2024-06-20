package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.workfront.WorkfrontResponse;
import com.adobe.core.raven.service.interfaces.URLService;
import com.adobe.core.raven.service.interfaces.WorkfrontService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkfrontServiceImpl implements WorkfrontService {


    @Autowired
    URLService urlService;

    public String fetchFromAddress(String workfrontID){
        String workfront_payload = QAConstant.WORKFRONT_PAYLOAD + workfrontID + "\"}";
        String response = urlService.callPostAPI(workfront_payload, QAConstant.WORKFRONT_URL);
        WorkfrontResponse workflowResponse = null;
        try {
            workflowResponse = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(response, WorkfrontResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String fromAddress = workflowResponse.getProjectInfo().getWorkfrontCustomForm().getFromNameAndAddress();

        return fromAddress;
    }

}
