package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.SocialLinkEnum;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.OutputMapperService;
import com.adobe.core.raven.service.interfaces.URLService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import jdk.jshell.spi.ExecutionControl;
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
import java.util.HashMap;
import java.util.Map;

public class ValidateDeliveryLinksStep implements Tasklet {

    //GMOASD-163 - https://jira.corp.adobe.com/projects/GMOASD/issues/GMOASD-163?filter=allopenissues

    @Autowired
    ValidateService validateService;

    @Autowired
    OutputMapperService outputMapperService;

    @Autowired
    DataService dataService;

    @Autowired
    URLService urlService;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("Delivery Links Step");
         JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        String htmlBody = jobParameters.getString(JobConstant.HTML_BODY);
        String locale = dataService.fetchLocale(messageId);

        Document parsedHtml = Jsoup.parse(htmlBody, QAConstant.UTF, Parser.xmlParser());
        Elements links = parsedHtml.getElementsByTag(QAConstant.TAG_A);
        HashMap<String, Integer> linksMap = fetchLinksMap(links);

        ArrayList<Link> deliveryLinks = new ArrayList<>();

        String state = JobConstant.PASS;
        for(Map.Entry<String,Integer> deliveryLink: linksMap.entrySet()){
            if(deliveryLink.getKey().contains("tel:")){
                continue;
            }
            Link link = new Link();
            int respCode = deliveryLink.getValue();
            String url = deliveryLink.getKey();
            if(respCode >= 400){
                state = JobConstant.FAIL;
                link.setState(JobConstant.FAIL);
                link.setReason(JobConstant.VALIDATE_DELIVERY_LINKS_REASON);

            }else{
                link.setState(JobConstant.PASS);
                link.setReason(JobConstant.PASS);
            }
            link.setLink(url);
            deliveryLinks.add(link);
        }

        outputMapperService.mapValidationOutput(JobConstant.VALIDATE_DELIVERY_LINKS_LABEL, JobConstant.VALIDATE_DELIVERY_LINKS_TYPE, JobConstant.VALIDATE_DELIVERY_LINKS_NAME, state, deliveryLinks,stepId,messageId);

        return RepeatStatus.FINISHED;
    }


    private HashMap<String, Integer> fetchLinksMap(Elements links) {
        HashMap<String, Integer> outputLinkMap = new HashMap();

        links.forEach(link -> {
            String hrefValue = link.attr(QAConstant.HREF);


            if(!hrefValue.contains("mailto:")) {
                System.out.println("href Value : " + hrefValue);
                if(hrefValue.equalsIgnoreCase("https://t-info.mail.adobe.com/r/?id=h18c130e8,fd4c527c,bf9fea0f")){
                    System.out.println("");
                }
                HashMap<String, Integer> responseMap = urlService.getRedirectedUrl(hrefValue);
                String url = responseMap.entrySet().stream().iterator().next().getKey();
                link.attr("href", url);
                int respCode = responseMap.entrySet().stream().iterator().next().getValue();
                outputLinkMap.put(url, respCode);
            }
        });

        return outputLinkMap;
    }

}
