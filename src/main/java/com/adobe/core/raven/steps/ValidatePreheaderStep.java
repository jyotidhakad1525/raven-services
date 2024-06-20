package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.service.interfaces.CgenService;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.OutputMapperService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class ValidatePreheaderStep implements Tasklet {

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

        System.out.println("PreHeader Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String jobId = jobParameters.getString(JobConstant.JOB_ID);
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        ArrayList<Cgen> cgenContent = dataService.fetchCgen(jobId,stepId);
        String htmlBody = jobParameters.getString("htmlBody");
        String activityID = dataService.fetchActivityId(messageId);

        Document parsedHtml = Jsoup.parse(htmlBody, QAConstant.UTF, Parser.xmlParser());
        Element preheaderLink = parsedHtml.getElementsByTag(QAConstant.TAG_DIV).first();

        String preHeaderFromCgen = cgenService.fetchPreHeader(activityID,cgenContent);

        ArrayList<Link> preHeader = new ArrayList<>();
        Link link = new Link();
        String state = JobConstant.PASS;
        if(preHeaderFromCgen != null
                && preheaderLink != null
                && validateService.compare(preHeaderFromCgen
                .replaceAll(QAConstant.Non_breaking_Space," ")
                .replaceAll(QAConstant.Non_breaking_Space_Str, " ")
                        .trim(),
                preheaderLink.text()
                        .replaceAll(QAConstant.Non_breaking_Space," ")
                        .replaceAll(QAConstant.Non_breaking_Space_Str, " ")
        )){
            link.setState(JobConstant.PASS);
            link.setReason(JobConstant.PASS);

        }else{
            state = JobConstant.FAIL;
            link.setState(JobConstant.FAIL);
            link.setReason(JobConstant.VALIDATE_PREHEADER_REASON);
        }
        link.setLink(preheaderLink.text());
        preHeader.add(link);
        outputMapperService.mapValidationOutput(JobConstant.VALIDATE_PREHEADER_LABEL,JobConstant.VALIDATE_PREHEADER_TYPE,  JobConstant.VALIDATE_PREHEADER_NAME, state, preHeader,stepId,messageId);

        return RepeatStatus.FINISHED;
    }
}
