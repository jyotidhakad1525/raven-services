package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.qa.Link;
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

public class ValidateImageSourceStepReshare implements Tasklet {

    @Autowired
    ValidateService validateService;

    @Autowired
    OutputMapperService outputMapperService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("Image Source Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String htmlBody = jobParameters.getString("htmlBody");
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        Document parsedHtml = Jsoup.parse(htmlBody, QAConstant.UTF, Parser.xmlParser());
        Elements images = parsedHtml.getElementsByTag(QAConstant.TAG_IMG);
        ArrayList<Link> imageDetailList = new ArrayList<>();
        String state = JobConstant.PASS;
        for (Element image : images) {
            Link link = new Link();
            if(validateService.validateImageSource(image)){
                link.setState(JobConstant.PASS);
                link.setReason(JobConstant.PASS);
            }else{
                state = JobConstant.FAIL;
                link.setState(JobConstant.FAIL);
                link.setReason(JobConstant.VALIDATE_IMAGE_SOURCE_REASON);
            }
            link.setLink(image.attr("src"));
            imageDetailList.add(link);

        }
          //  System.out.println("true");
            outputMapperService.mapValidationOutput(JobConstant.VALIDATE_IMAGE_SOURCE_LABEL, JobConstant.VALIDATE_IMAGE_SOURCE_TYPE, JobConstant.VALIDATE_IMAGE_SOURCE_NAME, state, imageDetailList, stepId, messageId,"");

        return RepeatStatus.FINISHED;
    }
}
