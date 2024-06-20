package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.OutputMapperService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class ValidateFooterStepReshare implements Tasklet {

    @Autowired
    ValidateService validateService;

    @Autowired
    DataService dataService;

    @Autowired
    OutputMapperService outputMapperService;


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        System.out.println("Footer Step");
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        String htmlBody = jobParameters.getString(JobConstant.HTML_BODY);
        String jobId = jobParameters.getString(JobConstant.JOB_ID);
        String stepId = jobParameters.getString(JobConstant.STEP_ID);
        String messageId = jobParameters.getString(JobConstant.MESSAGE_ID);
        String locale = dataService.fetchLocale(messageId);
        Document parsedHtml = Jsoup.parse(htmlBody, QAConstant.UTF, Parser.xmlParser());

        //String optOut = dataService.fetchOptOutLinkLable(locale);
        String footerHtml = dataService.fetchFooter(locale);
        String optOutLinkLocale = dataService.fetchOptOutLinkLable(locale);
        Element footerElement = parsedHtml.getElementById(QAConstant.FOOTERID);

        if(locale.equals("zh_TW")) {
            System.out.println("locale: " + locale);
        }

        ArrayList<Link> footers = new ArrayList<>();
        Link link = new Link();
        String state = JobConstant.PASS;
        footerHtml = footerHtml.replaceAll(QAConstant.OPTOUTLINKLOCALELINK, optOutLinkLocale);
        footerHtml = footerHtml.replaceAll(QAConstant.OPTOUTLINKLOCALELINK1, optOutLinkLocale);

        if(!(locale.equals("zh_TW")
                || locale.equals("zh_HK"))) {
            footerHtml = footerHtml.replaceAll(QAConstant.ATAG_FULLSTOP_LINK, QAConstant.SPACE_FULLSTOP_LINK);
        }
        footerHtml = footerHtml.replaceAll(QAConstant.NON_BREAKING_HYPHEN,QAConstant.SPACE_DASH_LINK);



        if(footerHtml.contains("privacy@adobe.com")) {
            int count = 1;
            StringBuffer footerBuffer = new StringBuffer();
            Scanner scanner = new Scanner(footerHtml);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                footerBuffer.append(line);
                System.out.println("Line " + count + " : " + line);

                if (line.contains("privacy@adobe.com")) {
                    break;
                }
            }
            int year = Calendar.getInstance().get(Calendar.YEAR);
            footerHtml = footerBuffer.toString()
                    .replace(QAConstant.APAC_YEAR,String.valueOf(year))
                    .replace(QAConstant.APAC_OPTOUTLINKLOCALELINK,optOutLinkLocale);
            scanner.close();
        }
        Document parsedFooterHtml = Jsoup.parse(footerHtml, QAConstant.UTF, Parser.xmlParser());

        footerHtml = parsedFooterHtml.text();

        String html = StringEscapeUtils.unescapeJava(footerHtml);
        html = StringEscapeUtils.unescapeHtml4(html);

  //      System.out.println("Repo:" + html);


//        System.out.println("Footer:" + footerElement.text());

        if(footerElement != null
            && footerHtml != null
            && validateService.compare(html.trim()
                .replaceAll(" ", "")
                .replaceAll("‑","")
                .replaceAll("-", ""),
                footerElement.text()
                        .replaceAll(" ","")
                        .replaceAll("‑","")
                        .replaceAll("-",""))){
            link.setState(JobConstant.PASS);
            link.setReason(JobConstant.PASS);
        }else{
            state = JobConstant.FAIL;
            link.setState(JobConstant.FAIL);
            link.setReason(JobConstant.VALIDATE_FOOTER_REASON);
        }
        if(footerElement != null) {
            link.setLink(footerElement.text());
        }
        footers.add(link);

            outputMapperService.mapValidationOutput(JobConstant.VALIDATE_FOOTER_LABEL, JobConstant.VALIDATE_FOOTER_TYPE, JobConstant.VALIDATE_FOOTER_NAME, state, footers, stepId, messageId,"");

        return RepeatStatus.FINISHED;
    }

}
