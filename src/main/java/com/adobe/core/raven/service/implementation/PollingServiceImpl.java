package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.dto.MsgValidationModel;
import com.adobe.core.raven.dto.QAResponse;
import com.adobe.core.raven.dto.job.JobStep;
import com.adobe.core.raven.dto.job.MasterJob;
import com.adobe.core.raven.dto.message.JobRequest;
import com.adobe.core.raven.dto.message.MessageRepository;
import com.adobe.core.raven.dto.qa.CheckList;
import com.adobe.core.raven.dto.qa.QaRepository;
import com.adobe.core.raven.dto.qa.QaItem;
import com.adobe.core.raven.dto.qa.ReshareQaRepository;
import com.adobe.core.raven.repository.*;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.PollingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PollingServiceImpl implements PollingService {

    @Autowired
    QAInfoRepository qaInfoRepository;

    @Autowired
    MasterJobInfoRepository masterJobInfoRepository;

    @Autowired
    MessageInfoRepository messageInfoRepository;

    @Autowired
    DataService dataService;

    @Autowired
    ReshareQAInfoRepository reshareQAInfoRepository;

    public QAResponse pollRaven(String jobId, String stepId){
        QAResponse qaResponse = new QAResponse();
        ArrayList<MsgValidationModel> msgValidationModelList = new ArrayList<>();
        QaRepository qaRepository = qaInfoRepository.get(stepId);
        ArrayList<QaItem> qaItems = qaRepository.getItems();
        Boolean failureFlag = false;
        Boolean pendingFlag = false;
        for(QaItem qaItem: qaItems){
            MsgValidationModel msgValidationModel = new MsgValidationModel();
            String finalStatus = null;
            if(qaItem.getCheckList() != null){
                MessageRepository messageRepository = messageInfoRepository.get(qaItem.getId());
                msgValidationModel.setStatus(messageRepository.getActivityId());
                msgValidationModel.setActivityId(messageRepository.getActivityId());
                msgValidationModel.setLocale(messageRepository.getLocale());
                msgValidationModel.setSubjectLine(messageRepository.getSubject());
                msgValidationModel.setCheckList(qaItem.getCheckList());
                msgValidationModel.setStatus(qaItem.getStatus());
                msgValidationModel.setSegment(qaItem.getSegment());
                msgValidationModelList.add(msgValidationModel);

                List<CheckList> failedCheckList = qaItem.getCheckList().stream().filter(input -> input.getState().equalsIgnoreCase(JobConstant.FAIL)).collect(Collectors.toList());
                if(qaItem.getCheckList().size() == 12 && failedCheckList.size() > 0){
                    finalStatus = JobConstant.FAIL;
                    msgValidationModel.setStatus(JobConstant.FAIL);
                }else if(qaItem.getCheckList().size() == 12 && failedCheckList.size() == 0){
                    finalStatus = JobConstant.PASS;
                    msgValidationModel.setStatus(JobConstant.PASS);
                }else{
                    finalStatus = JobConstant.PENDING;
                    msgValidationModel.setStatus(JobConstant.PENDING);
                }
                //Update final status in DB
                qaInfoRepository.updateStatus(stepId,qaItem.getId(),finalStatus);

                if(finalStatus.equalsIgnoreCase(JobConstant.FAIL)){
                    failureFlag = true;
                }else if(finalStatus.equalsIgnoreCase(JobConstant.PENDING)){
                    pendingFlag = true;
                }
            }
        }

        JobStep jobStep = dataService.fetchJobStep(jobId, stepId);
        MasterJob masterJob = masterJobInfoRepository.get(jobId);
        if (pendingFlag == true) {
            masterJobInfoRepository.updateStatus(jobId, stepId, JobConstant.IN_PROGRESS);
            masterJobInfoRepository.updateJobState(jobId, jobStep.getName() + " " + JobConstant.IN_PROGRESS);
        }
        else if(jobStep.getState().equals(JobConstant.IN_PROGRESS)
                || masterJob.getState().equals(JobConstant.IN_DRAFT)
                || jobStep.getState().equals(JobConstant.CREATED) ) {
            if (failureFlag == true) {
                masterJobInfoRepository.updateStatus(jobId, stepId, JobConstant.FAILED);
                masterJobInfoRepository.updateJobState(jobId, jobStep.getName() + " " + JobConstant.FAILED);
            } else {
                masterJobInfoRepository.updateStatus(jobId, stepId, JobConstant.COMPLETED);
                masterJobInfoRepository.updateJobState(jobId, jobStep.getName() + " " + JobConstant.COMPLETED);
            }
        }

        qaResponse.setFinalStatus(jobStep.getState());
        qaResponse.setMsgValidationModel(msgValidationModelList);

        return qaResponse;

    }


    public QAResponse rePollRaven(String jobId, JobRequest jobRequest) {
        ArrayList<MsgValidationModel> msgValidationModelList = new ArrayList<>();
        ArrayList<QAResponse> responseList =new ArrayList<QAResponse>();
        ArrayList<String> messageIds = jobRequest.getMessagesIds();
        QAResponse qaResponse = new QAResponse();
        String status = "";
        boolean pending = false;
        boolean fail = false;
        for (String messageId : messageIds) {


            ReshareQaRepository reshareQaRepository = reshareQAInfoRepository.get(messageId);

            Boolean failureFlag = false;
            Boolean pendingFlag = false;

            ArrayList<CheckList> checkList = reshareQaRepository.getCheckList();
            MsgValidationModel msgValidationModel = new MsgValidationModel();
            String finalStatus = null;

            if (checkList != null) {
                MessageRepository messageRepository = messageInfoRepository.get(reshareQaRepository.getId());
                msgValidationModel.setStatus(messageRepository.getActivityId());
                msgValidationModel.setActivityId(messageRepository.getActivityId());
                msgValidationModel.setLocale(messageRepository.getLocale());
                msgValidationModel.setSubjectLine(messageRepository.getSubject());
                msgValidationModel.setCheckList(reshareQaRepository.getCheckList());
                msgValidationModel.setStatus(reshareQaRepository.getStatus());
                msgValidationModel.setSegment(reshareQaRepository.getSegment());
                msgValidationModelList.add(msgValidationModel);

                List<CheckList> failedCheckList = reshareQaRepository.getCheckList().stream().filter(input -> input.getState().equalsIgnoreCase(JobConstant.FAIL)).collect(Collectors.toList());
                if (reshareQaRepository.getCheckList().size() == 12 && failedCheckList.size() > 0) {
                    finalStatus = JobConstant.FAIL;
                    msgValidationModel.setStatus(JobConstant.FAIL);
                } else if (reshareQaRepository.getCheckList().size() == 12 && failedCheckList.size() == 0) {
                    finalStatus = JobConstant.PASS;
                    msgValidationModel.setStatus(JobConstant.PASS);
                } else {
                    finalStatus = JobConstant.PENDING;
                    msgValidationModel.setStatus(JobConstant.PENDING);
                }
                //Update final status in DB

                reshareQAInfoRepository.updateStatus(messageId, reshareQaRepository.getId(), finalStatus);

                if (finalStatus.equalsIgnoreCase(JobConstant.FAIL)) {
                    failureFlag = true;
                } else if (finalStatus.equalsIgnoreCase(JobConstant.PENDING)) {
                    pendingFlag = true;
                }
            }



            // JobStep jobStep = dataService.fetchJobStep(jobId, messageId);

            responseList.add(qaResponse);
            if(finalStatus.equalsIgnoreCase(JobConstant.FAIL)){
                fail = true;
            }
            if(finalStatus.equalsIgnoreCase(JobConstant.PENDING)){
                pending = true;
            }

        }
        if(fail){
            status = "FAILED";
        }else if(pending){
            status = "IN PROGRESS";
        }else{
            status = "COMPLETED";
        }
        qaResponse.setFinalStatus(status);
        qaResponse.setMsgValidationModel(msgValidationModelList);
        return qaResponse;
    }

}
