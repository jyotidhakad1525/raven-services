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

import java.util.ArrayList;

public class ValidateMirrorStepReshare implements Tasklet {

    //GMOASD-170 - https://jira.corp.adobe.com/projects/GMOASD/issues/GMOASD-170?filter=allopenissues

    @Autowired
    ValidateService validateService;

    @Autowired
    DataService dataService;

    @Autowired
    OutputMapperService outputMapperService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("Mirror Links Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String htmlBody = jobParameters.getString("htmlBody");
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        String locale = dataService.fetchLocale(messageId);
        Document parsedHtml = Jsoup.parse(htmlBody, QAConstant.UTF, Parser.xmlParser());
        Elements mirrorLink = parsedHtml.select(QAConstant.MIRRORLINK);

        String mirrorTextFromRepo = dataService.fetchMirror(locale);

        ArrayList<Link> mirror = new ArrayList<>();
        Link link = new Link();
        String state = JobConstant.PASS;
        System.out.println("Mirror Text from Repo:" +mirrorTextFromRepo);
        System.out.println("Mirror Text:" +mirrorLink.text());
        if(validateService.compare(mirrorTextFromRepo
                        .replaceAll(QAConstant.Non_breaking_Space," ")
                        .replaceAll(QAConstant.Non_breaking_Space_Str, " "),
                mirrorLink.text()
                        .replaceAll(QAConstant.Non_breaking_Space," ")
                        .replaceAll(QAConstant.Non_breaking_Space_Str, " "))){
            link.setState(JobConstant.PASS);
            link.setReason(JobConstant.PASS);
        }else{
            state = JobConstant.FAIL;
            link.setState(JobConstant.FAIL);
            link.setReason(JobConstant.VALIDATE_MIRROR_REASON);
        }
        link.setLink(mirrorLink.text());
        mirror.add(link);
            outputMapperService.mapValidationOutput(JobConstant.VALIDATE_MIRROR_LABEL, JobConstant.VALIDATE_MIRROR_TYPE, JobConstant.VALIDATE_MIRROR_NAME, state, mirror, stepId, messageId,"");

        return RepeatStatus.FINISHED;
    }
}
