package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.OutputMapperService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

public class ValidateOptoutLinkStep implements Tasklet {

    //GMOASD-168 - https://jira.corp.adobe.com/projects/GMOASD/issues/GMOASD-168?filter=allopenissues

    @Autowired
    ValidateService validateService;

    @Autowired
    DataService dataService;

    @Autowired
    OutputMapperService outputMapperService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("OptOutLink Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String htmlBody = jobParameters.getString("htmlBody");
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);

        String locale = dataService.fetchLocale(messageId);
        Document parsedHtml = Jsoup.parse(htmlBody, QAConstant.UTF, Parser.xmlParser());
        Elements optOutLink = parsedHtml.select(QAConstant.OPTOUTLINK);

        String optOutLinkLable = dataService.fetchOptOutLinkLable(locale);

        ArrayList<Link> mirror = new ArrayList<>();
        Link link = new Link();
        String state = JobConstant.PASS;
        if(!ObjectUtils.isEmpty(optOutLinkLable) && validateService.compare(optOutLinkLable
                        .replaceAll(QAConstant.Non_breaking_Space," ")
                        .replaceAll(QAConstant.Non_breaking_Space_Str, " ")
                        .replaceAll(QAConstant.SingleQuote, "'"),
                optOutLink.text()
                        .replaceAll(QAConstant.Non_breaking_Space," ")
                        .replaceAll(QAConstant.Non_breaking_Space_Str, " ")
                        .replaceAll(QAConstant.SingleQuote, "'"))) {
            link.setState(JobConstant.PASS);
            link.setReason(JobConstant.PASS);
        }else{
            state = JobConstant.FAIL;
            link.setState(JobConstant.FAIL);
            link.setReason(JobConstant.VALIDATE_OPTOUT_REASON);
        }
        link.setLink(optOutLink.text());
        mirror.add(link);

        outputMapperService.mapValidationOutput(JobConstant.VALIDATE_OPTOUT_LABEL, JobConstant.VALIDATE_OPTOUT_TYPE, JobConstant.VALIDATE_OPTOUT_NAME, state, mirror,stepId,messageId);


        return RepeatStatus.FINISHED;
    }
}
