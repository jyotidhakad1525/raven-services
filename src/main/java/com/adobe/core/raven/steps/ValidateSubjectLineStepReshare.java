package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.service.interfaces.CgenService;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.OutputMapperService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class ValidateSubjectLineStepReshare implements Tasklet {


    @Autowired
    CgenService cgenService;

    @Autowired
    ValidateService validateService;

    @Autowired
    DataService dataService;

    @Autowired
    OutputMapperService outputMapperService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("SubjectLine Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String jobId = jobParameters.getString(JobConstant.JOB_ID);
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        ArrayList<Cgen> cgenContent = dataService.fetchCgen(jobId,stepId);
        String subjectLine = dataService.fetchSubjectLine(messageId);
        String activityID = dataService.fetchActivityId(messageId);
        String subjectLineFromCgen = cgenService.fetchSubjectline(activityID,cgenContent);//debug

        subjectLine = subjectLine.split("]")[1].trim();
        ArrayList<Link> subjectLines = new ArrayList<>();
        Link link = new Link();
        String state = JobConstant.PASS;
        if(validateService.compare(subjectLineFromCgen
                        .replaceAll(QAConstant.Non_breaking_Space," ")
                        .replaceAll(QAConstant.Non_breaking_Space_Str, " ")
                , subjectLine
                        .replaceAll(QAConstant.Non_breaking_Space," ")
                        .replaceAll(QAConstant.Non_breaking_Space_Str, " "))){
            link.setState(JobConstant.PASS);
            link.setReason(JobConstant.PASS);

        }else{
            state = JobConstant.FAIL;
            link.setState(JobConstant.FAIL);
            link.setReason(JobConstant.VALIDATE_SUBJECT_REASON);
        }
        link.setLink(subjectLine);
        subjectLines.add(link);

            outputMapperService.mapValidationOutput(JobConstant.VALIDATE_SUBJECT_LABEL, JobConstant.VALIDATE_SUBJECT_TYPE, JobConstant.VALIDATE_SUBJECT_NAME, state, subjectLines, stepId, messageId,"");


        return RepeatStatus.FINISHED;
    }
}
