package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.dto.message.MessageRepository;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.OutputMapperService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class ValidateFromEmailStep implements Tasklet {

    //GMOASD-166 - https://jira.corp.adobe.com/projects/GMOASD/issues/GMOASD-166?filter=allopenissues

    @Autowired
    ValidateService validateService;

    @Autowired
    DataService dataService;

    @Autowired
    OutputMapperService outputMapperService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("FromEmail Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String workfrontId = jobParameters.getString(JobConstant.WORKFRONT_ID);
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        MessageRepository messageRepository = new MessageRepository();
        messageRepository = dataService.getMessageRepository(messageId);
        String locale =messageRepository.getLocale();
        String activityId= messageRepository.getActivityId();
        String senderAddress = messageRepository.getSenderName() + " <" + messageRepository.getSenderAddress() + ">";

        //fetch activity id and send in validatefromaddress
        ArrayList<Link> fromEmail = new ArrayList<>();
        Link link = new Link();
        String state = JobConstant.PASS;
        if(validateService.validateFromAddress(senderAddress, workfrontId, locale,stepId,activityId)){
            link.setState(JobConstant.PASS);
            link.setReason(JobConstant.PASS);
        }else{
            state = JobConstant.FAIL;
            link.setState(JobConstant.FAIL);
            link.setReason(JobConstant.VALIDATE_FROM_EMAIL_REASON);
        }
        link.setLink(senderAddress);
        fromEmail.add(link);
        outputMapperService.mapValidationOutput(JobConstant.VALIDATE_FROM_EMAIL_LABEL, JobConstant.VALIDATE_FROM_EMAIL_TYPE, JobConstant.VALIDATE_FROM_EMAIL_NAME, state, fromEmail,stepId,messageId);

        return RepeatStatus.FINISHED;
    }
}
