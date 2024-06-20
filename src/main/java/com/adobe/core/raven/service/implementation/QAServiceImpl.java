package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.*;
import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.repository.MasterInfoRepository;
import com.adobe.core.raven.service.interfaces.*;
import com.auxilii.msgparser.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class QAServiceImpl implements QAService {

    @Autowired
    MsgParserService msgParserService;

    @Autowired
    ValidateService validateService;

    @Autowired
    EmailComponent emailComponent;

    @Autowired
    URLService urlService;

    @Autowired
    CgenService cgenService;

    @Autowired
    private MasterInfoRepository masterInfoRepository;

    public void generateQA(InputContentModel content){
      /*  try {
            ArrayList<String> msgFiles = content.getMsgFiles();
            int i=0;
            new File("src/main/resources/" + content.getJobId()).mkdirs();
            for(String msgFile: msgFiles){
                String path = "src/main/resources/" + content.getJobId() + "/msgFile" + i + ".msg";
                String str = new String(msgFile.getBytes(), StandardCharsets.UTF_8);
                byte[] msgContent = Base64.getDecoder().decode(str);
                File file = new File(path);
                FileOutputStream os = new FileOutputStream(file, true);
                os.write(msgContent);
                os.close();
                i++;
                Message msg = msgParserService.parseMsgFile(path);
                String subject = msg.getSubject();
                String[] subjectTokens = subject.split(" ");

                //Set Email Components
                emailComponent.setFromEmail(msg.getFromEmail());
                emailComponent.setFromName(msg.getFromName());
                emailComponent.setSentDate(msg.getDate());
                emailComponent.setActivityId(subjectTokens[0]);
                emailComponent.setLocale(subjectTokens[1]);
                emailComponent.setSubjectLine(msg.getSubject());
                emailComponent.setCreativeFileName(subjectTokens[2]);
                emailComponent.setBu(content.getBu());

                String body = msg.getConvertedBodyHTML();
                System.out.println(subject);

                //QA Check - Image source is working
                validateService.validateImageSource(body);

                //QA Check - Image alt test is there
                validateService.validateImageAltText(body);

                //Fetch All Links(Serial Flow then QA checks parallel)
                Document parsedHtml = Jsoup.parse(body, QAConstant.UTF, Parser.xmlParser());
                Elements links = parsedHtml.getElementsByTag(QAConstant.TAG_A);
                ArrayList<String> redirectedLinks = new ArrayList<>();
                ArrayList<String> CTALinks = new ArrayList<>();
                for(Element link:links){
                    String hrefValue = link.attr(QAConstant.HREF);
                    String redirectedUrl = urlService.getRedirectedUrl(hrefValue);
                    redirectedLinks.add(urlService.getRedirectedUrl(hrefValue));
                    link.attr("href",urlService.getRedirectedUrl(hrefValue));
                    //Fetch CTALinks
                    if (redirectedUrl != null && redirectedUrl.contains(QAConstant.TRACKINGID)) {
                        CTALinks.add(redirectedUrl);
                    }
                }


                //QA Check - Validate Social Links
                List<SocialRepository> socialRepositoryList = null;
                //     socialRepoRepository.selectsocial(emailComponent.getFromEmail(),
                //            emailComponent.getLocale());
                ArrayList<String> socialLinkDetails = new ArrayList<>();
                for (SocialLink socialLink : SocialLink.values()) {
                    System.out.println(socialLink.name());

                    Elements socialLinks = links.select(socialLink.getLinktag());
                    socialLinkDetails = validateService.validateSocialLink(socialLinks,socialLink.getLinkType(),socialRepositoryList);
                }


                //QA Check - Cgen matching with Tag
                for(String ctaLink: CTALinks){
                    MultiValueMap<String, String> queryParameters = urlService.getQueryParameters(ctaLink);
                    String tagID = queryParameters.get(QAConstant.TRACKINGID).get(0);
                    String tagfullurl = cgenService.fetchTagfullurl(tagID,content.getCgen());
                    validateService.validateTagIdwithCgen(tagfullurl ,ctaLink);
                }

                //QA Check - Footer Link
                FooterRepository footerRepository = null;
                //    footerRepositoryService.findByLangLocal(emailComponent.getLocale());
                Element footerElement = parsedHtml.getElementById(QAConstant.FOOTERID);
                validateService.validateFooter(footerRepository.getFooterHtml(), footerElement, emailComponent.getLocale());

                //QA Check - Subject Line match with Cgen
                String subjectLine = emailComponent.getSubjectLine().substring(emailComponent.getSubjectLine().lastIndexOf("]") + 1);
                String subjectLineFromCgen = cgenService.fetchSubjectline(emailComponent.getActivityId(),content.getCgen());
                validateService.validateSubjectLine(subjectLineFromCgen, subjectLine);

                //QA Check - OptOutLink Label
                // footerRepository = footerRepositoryService.findByLangLocal(emailComponent.getLocale());
                footerRepository = null;
                footerRepository.getOptOutLinkLable();
                Elements optOutLink = parsedHtml.select(QAConstant.OPTOUTLINK);
                validateService.validateOptOutLink(optOutLink.text(), footerRepository.getOptOutLinkLable());

                //QA Check - Workfront API - From Email Name
                validateService.validateFromAddress(msg.getFromEmail(), content.getWorkfrontID());

                //QA Check - Mirror Links
                Elements mirrorLink = parsedHtml.select(QAConstant.MIRRORLINK);
                validateService.validateMirrorLinks(emailComponent.getLocale(), mirrorLink.text());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }



    Element fetchCTALinks(Elements links, String tagId) {
        Element link = null;
        for (Element ctaLink : links) {
            String hrefValue = link.attr(QAConstant.HREF);
            if (hrefValue.contains(tagId)) {
                link = ctaLink;
            }
        }
        return link;
    }


    ArrayList<Cgen> fetchLinks(String activityId, ArrayList<Cgen> cgen){
        List<Cgen> cgenFiltered = cgen.stream().filter(input -> input.getActivityId().equals(activityId)).collect(Collectors.toList());

        return new ArrayList(cgenFiltered);
    }


}
