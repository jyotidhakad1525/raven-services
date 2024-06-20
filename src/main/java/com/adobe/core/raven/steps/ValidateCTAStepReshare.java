package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.service.interfaces.*;
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
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ValidateCTAStepReshare implements Tasklet {

    @Autowired
    URLService urlService;

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

        System.out.println("CTA Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String jobId = jobParameters.getString(JobConstant.JOB_ID);
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        String activityId = dataService.fetchActivityId(messageId);
        ArrayList<Cgen> cgenContent = dataService.fetchCgen(jobId,stepId);
        //chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("Key","Shivani");
        String htmlBody = jobParameters.getString(JobConstant.HTML_BODY);


        Document parsedHtml = Jsoup.parse(htmlBody, QAConstant.UTF, Parser.xmlParser());
        Elements links = parsedHtml.getElementsByTag(QAConstant.TAG_A);

        ArrayList<String> CTALinksFromHtml = fetchRedirectedLinks(links, chunkContext);

        ArrayList<Link> ctaLinks = new ArrayList<>();
        String state = JobConstant.PASS;
        for(String ctaLink: CTALinksFromHtml){
            Link link = new Link();
            MultiValueMap<String, String> queryParameters = urlService.getQueryParameters(ctaLink);
            String tagID = queryParameters.get(QAConstant.TRACKINGID).get(0);
            String tagfullurl = cgenService.fetchTagfullurl(tagID,cgenContent);
            if(validateService.compare(tagfullurl ,ctaLink)){
                link.setState(JobConstant.PASS);
                link.setReason(JobConstant.PASS);
            }else{
                state = JobConstant.FAIL;
                link.setState(JobConstant.FAIL);
                link.setReason(JobConstant.VALIDATECTA_REASON);
            }
            link.setLink(ctaLink);
            ctaLinks.add(link);
        }

            outputMapperService.mapValidationOutput(JobConstant.VALIDATE_CTA_LABEL, JobConstant.VALIDATE_CTA_TYPE, JobConstant.VALIDATE_CTA_NAME, state, ctaLinks, stepId, messageId,"");

        //Validate Unique Links
        ArrayList<Link> validateUniqueLinks = new ArrayList<>();

        Set<String> uniqueLinks = new HashSet<String>(CTALinksFromHtml);
        String uniqueLinksState = JobConstant.PASS;
        int ctaLinksCountFromCgen = cgenService.fetchCTALinkCount(activityId, cgenContent);
            Link link = new Link();
            if(uniqueLinks.size() == ctaLinksCountFromCgen){
                link.setState(JobConstant.PASS);
                link.setReason(JobConstant.PASS);
            }else{
                uniqueLinksState = JobConstant.FAIL;
                link.setState(JobConstant.FAIL);
                link.setReason(JobConstant.VALIDATE_UNIQUE_LINKS_REASON);
            }
            link.setLink(Integer.toString(ctaLinksCountFromCgen));
        validateUniqueLinks.add(link);

          //  System.out.println("true");
            outputMapperService.mapValidationOutput(JobConstant.VALIDATE_UNIQUE_LINKS_LABEL, JobConstant.VALIDATE_UNIQUE_LINKS_TYPE, JobConstant.VALIDATE_UNIQUE_LINKS_NAME, uniqueLinksState, validateUniqueLinks, stepId, messageId,"");


        return RepeatStatus.FINISHED;
    }

    private ArrayList<String> fetchRedirectedLinks(Elements links, ChunkContext chunkContext) {
        ArrayList<String> redirectedLinks = new ArrayList<>();
        ArrayList<String> CTALinks = new ArrayList<>();
        //optimization needed
        for(Element link: links){
            String hrefValue = link.attr(QAConstant.HREF);


            if(hrefValue.contains("mailto:")
                    || hrefValue.contains("pf.kakao.com")
                    || hrefValue.contains("tel:"))
            {
                // System.out.println("::::::::::mail url::  " + hrefValue);
                continue;
            }

           // System.out.println("url:::::::::::::::::::::::  " + hrefValue);
            HashMap<String,Integer> responseMap = urlService.getRedirectedUrl(hrefValue);

            String redirectedUrl = responseMap.entrySet().stream().iterator().next().getKey();
            redirectedLinks.add(redirectedUrl);
            link.attr("href",redirectedUrl);
            //Fetch CTALinks
            if (redirectedUrl != null && redirectedUrl.contains(QAConstant.TRACKINGID)) {
                CTALinks.add(redirectedUrl);
            }
        }
        //chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("htmlLinks",links);

        return CTALinks;
    }
}
