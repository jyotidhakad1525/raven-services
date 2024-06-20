package com.adobe.core.raven.steps;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.job.MasterJob;
import com.adobe.core.raven.dto.model.FooterRepository;
import com.adobe.core.raven.dto.qa.Link;
import com.adobe.core.raven.dto.repo.MasterRepository;
import com.adobe.core.raven.dto.repo.SocialLinks;
import com.adobe.core.raven.repository.MasterInfoRepository;
import com.adobe.core.raven.repository.MasterJobInfoRepository;
import com.adobe.core.raven.service.interfaces.*;
import org.apache.commons.codec.binary.Base64;
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
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidateFooterStep implements Tasklet {

    @Autowired
    ValidateService validateService;

    @Autowired
    DataService dataService;

    @Autowired
    OutputMapperService outputMapperService;

    @Autowired
    MasterJobInfoRepository masterJobInfoRepository;


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
        MasterJob masterJob= masterJobInfoRepository.get(jobId);
        //String optOut = dataService.fetchOptOutLinkLable(locale);
        String footerHtml=null;
        if(!ObjectUtils.isEmpty(masterJob.getLifecycleMarket())
                && masterJob.getLifecycleMarket().equalsIgnoreCase("Email - Tech Mktr")){
             footerHtml = dataService. fetchFooterTechMarketing(locale);
        }else {
             footerHtml = dataService.fetchFooter(locale);
        }
        String optOutLinkLocale = dataService.fetchOptOutLinkLable(locale);
       Element footerElement = parsedHtml.getElementById(QAConstant.FOOTERID);

       String finalFooterElement=null;
       if(footerElement !=null && (ObjectUtils.isEmpty(masterJob.getLifecycleMarket()) ||
               !masterJob.getLifecycleMarket().equalsIgnoreCase("Email - Tech Mktr"))){
           finalFooterElement=footerElement.text();
       }
      else {
           Document doc1 = Jsoup.parse(htmlBody);
           Element aliasBody = doc1.selectFirst("[class=legal]");
           String footerElements = aliasBody.text();

//           int lastIndex = footerElements.lastIndexOf(":\\d{6}");
//           if (lastIndex != -1) {
//               finalFooterElement=  footerElements.substring(0, lastIndex + 7);
//           }
      // }
        int colonIndex = footerElements.indexOf(':');
        Pattern pattern = Pattern.compile(":\\s*\\d{6}");
        Matcher matcher = pattern.matcher(footerElements);
        if (matcher.find()) {
            // Extract the substring from the start till the matched pattern
      finalFooterElement= footerElements.substring(0, matcher.end()).replaceAll("@email","@mail");;
//        if(finalFooterElement.contains("@email")){
//            finalFooterElement.replace("@email","@mail");
//        }

        }}

        //TODO - REG_FOOT NEEDS TO BE CHECKED FOR TECHMARKETING
        if(locale.equals("zh_TW")) {
            System.out.println("locale: " + locale);
        }

        ArrayList<Link> footers = new ArrayList<>();
        Link link = new Link();
        String state = JobConstant.PASS;
        String html = null;
        if(!ObjectUtils.isEmpty(footerHtml)) {

            footerHtml = footerHtml.replaceAll(QAConstant.OPTOUTLINKLOCALELINK, optOutLinkLocale);
            footerHtml = footerHtml.replaceAll(QAConstant.OPTOUTLINKLOCALELINK1, optOutLinkLocale);

        if( !(locale.equals("zh_TW")
                || locale.equals("zh_HK"))) {
            footerHtml = footerHtml.replaceAll(QAConstant.ATAG_FULLSTOP_LINK, QAConstant.SPACE_FULLSTOP_LINK);
        }


            footerHtml = footerHtml.replaceAll(QAConstant.NON_BREAKING_HYPHEN, QAConstant.SPACE_DASH_LINK);



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

        html = StringEscapeUtils.unescapeJava(footerHtml);
        html = StringEscapeUtils.unescapeHtml4(html);

        }
  //      System.out.println("Repo:" + html);


//        System.out.println("Footer:" + footerElement.text());

        if(finalFooterElement != null
            && footerHtml != null
            && validateService.compare(html.trim()
                .replaceAll(" ", "")
                .replaceAll("‑","")
                .replaceAll("-", ""),
                finalFooterElement.trim()
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
//            link.setLink(footerElement.text());
            link.setLink(finalFooterElement);
        }
        footers.add(link);

        outputMapperService.mapValidationOutput(JobConstant.VALIDATE_FOOTER_LABEL, JobConstant.VALIDATE_FOOTER_TYPE, JobConstant.VALIDATE_FOOTER_NAME, state, footers,stepId,messageId);

        return RepeatStatus.FINISHED;
    }

}
