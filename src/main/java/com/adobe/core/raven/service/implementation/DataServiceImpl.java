package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.cgen.CgenRepository;
import com.adobe.core.raven.dto.job.JobStep;
import com.adobe.core.raven.dto.job.MasterJob;
import com.adobe.core.raven.dto.message.MessageRepository;
import com.adobe.core.raven.dto.model.FooterRepository;
import com.adobe.core.raven.dto.model.SocialRepository;
import com.adobe.core.raven.dto.qa.QaRepository;
import com.adobe.core.raven.dto.qa.QaItem;
import com.adobe.core.raven.dto.qa.ReshareQaRepository;
import com.adobe.core.raven.dto.repo.*;
import com.adobe.core.raven.repository.*;
import com.adobe.core.raven.service.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DataServiceImpl implements DataService {

    @Autowired
    MasterJobInfoRepository masterJobInfoRepository;

    @Autowired
    CgenInfoRepository cgenInfoRepository;

    @Autowired
    MasterInfoRepository masterInfoRepository;

    @Autowired
    MasterInfoRepositoryTechMarketing masterInfoRepositoryTechMarketing;

    @Autowired
    MessageInfoRepository messageInfoRepository;

    @Autowired
    QAInfoRepository qaInfoRepository;

    @Autowired
    FromNameInfoRepository fromNameInfoRepository;

    @Autowired
    FooterInfoRepository footerInfoRepository;

    @Autowired
    SocialInfoRepository socialInfoRepository;

    @Autowired
    LegalInfoRepository legalInfoRepository;

    @Autowired
    MirrorInfoRepository mirrorInfoRepository;

    @Autowired
    ReshareQAInfoRepository reshareQAInfoRepository;

    @Autowired
    QAInfoRepository qAInfoRepository;



    public String fetchWorkfrontId(String jobId){
        MasterJob masterJob = masterJobInfoRepository.get(jobId);

        return masterJob.getWorkfrontId();
    }

    public String fetchSenderAddress(String messageId){
        MessageRepository messageRepository = messageInfoRepository.get(messageId);

        return  messageRepository.getSenderName() + " <" + messageRepository.getSenderAddress() + ">";
    }

    public String fetchSubjectLine(String messageId){
        MessageRepository messageRepository = messageInfoRepository.get(messageId);

        return messageRepository.getSubject();
    }

    public ArrayList<Cgen> fetchCgen(String jobId, String stepId){
        QaRepository qaRepository = getQaRepository(stepId);
        String cgenId = qaRepository.getCgenId();
        CgenRepository cgenRepository = cgenInfoRepository.get(cgenId);
        ArrayList<Cgen> cgenArrayList = cgenRepository.getContent();
        return cgenArrayList;
    }

    public String fetchActivityId(String messageId){
        MessageRepository messageRepository = messageInfoRepository.get(messageId);

        return messageRepository.getActivityId();
    }

    public String fetchBu(String stepId, String messageId){
        QaItem qaItem = fetchQAItem(stepId, messageId);

        return qaItem.getBu();
    }

    public String fetchSegment(String stepId, String messageId){
        QaItem qaItem = fetchQAItem(stepId, messageId);

        return qaItem.getSegment();
    }

    @Override
    public MessageRepository fetchMessageByMd5(String md5) {
        return messageInfoRepository.getByHash(md5);
    }

    @Override
    public MessageRepository insertMessage(MessageRepository messageRepository) {
        return messageInfoRepository.insert(messageRepository);
    }

    @Override
    public ReshareQaRepository insertMessage(ReshareQaRepository messageRepository) {
        return reshareQAInfoRepository.insert(messageRepository);
    }

    public ReshareQaRepository getReshareQARepository(String reshareQaId){
        return reshareQAInfoRepository.get(reshareQaId);
    }

    public  QaRepository getQAQARepository(String stepId)
    {
        QaRepository qaRepository= qAInfoRepository.get(stepId);
        return qaRepository;
    }

    public CgenRepository getCgenRepository(String id)
    {
        CgenRepository  CgenRepositorys=cgenInfoRepository.get(id);
        return CgenRepositorys;
    }

    public MessageRepository getMessageRepository(String id)
    {
        MessageRepository messageRepository= messageInfoRepository.get(id);
        return messageRepository;

    }


    @Override
    public String getFromName(String fromAddressFromWorkfront, String local) {

        String fromAddressName = fromAddressFromWorkfront.split("<")[0];
        fromAddressName = fromAddressName.toLowerCase().replaceAll(" ","");
        FromNameInfo fromNameInfo = fromNameInfoRepository.get(fromAddressName);

        String fromName = null;

        final String language;

        if(local.equals("zh_TW")
                || local.equals("zh_HK")
                || local.equals("zh_CN")) {
            language = local;
        } else {
            language = local.split("_")[0];
        }

        if(fromNameInfo != null) {

            fromName = fromNameInfo.getFromInfos().stream()
                    .filter(fromInfo -> fromInfo.getLanguage().equals(language))
                    .map(fromInfo -> fromInfo.getFromName())
                    .findFirst()
                    .orElse(null);

            if(fromName == null) {

                fromName = fromNameInfo.getFromInfos().stream()
                        .filter(fromInfo -> fromInfo.getLanguage().equals("en"))
                        .map(fromInfo -> fromInfo.getFromName())
                        .findFirst()
                        .orElse(null);
            }
        }else {
            fromName =fromAddressFromWorkfront.split("<")[0].trim();
        }

        return fromName;
    }

    public String fetchLocale(String messageId){
        MessageRepository messageRepository = messageInfoRepository.get(messageId);

        return messageRepository.getLocale();
    }


    public HashMap<String,String> fetchHtmlBody(String jobId, String stepId){
        HashMap<String,String> htmlBodies = new HashMap<>();
        QaRepository qaRepository = getQaRepository(stepId);
        ArrayList<QaItem> qaItems = qaRepository.getItems();
        List<String> msgIds = qaItems.stream().map(input -> input.getId()).collect(Collectors.toList());
        for(String msgId: msgIds) {
   //         if (msgId.equalsIgnoreCase("3790447a-2dfa-11ef-9d45-fa163e30268c")) {
                MessageRepository messageRepository = messageInfoRepository.get(msgId);
                String body = messageRepository.getHtml().getBody();
                htmlBodies.put(msgId, body);
   //        }
        }
        return htmlBodies;
    }

    public QaRepository getQaRepository(String stepId) {
       /* MasterJob masterJob = masterJobInfoRepository.get(jobId);
        ArrayList<JobStep> masterJobSteps = masterJob.getSteps();
        List<JobStep> jobSteps = masterJobSteps.stream().filter(input -> input.getId().equals(stepId)).collect(Collectors.toList());
        JobStep step = jobSteps.get(0);*/
        QaRepository qaRepository = qaInfoRepository.get(stepId);
        return qaRepository;
    }

    public QaItem fetchQAItem(String stepId, String messageId){
        QaRepository qaRepository = getQaRepository(stepId);
        ArrayList<QaItem> qaItems = qaRepository.getItems();
        List<QaItem> qaItemsFiltered = qaItems.stream().filter(input -> input.getId().equals(messageId)).collect(Collectors.toList());
        return qaItemsFiltered.get(0);
    }

    public JobStep fetchJobStep(String jobId, String stepId){
        MasterJob masterJob = masterJobInfoRepository.get(jobId);
        ArrayList<JobStep> jobSteps = masterJob.getSteps();
        List<JobStep> jobStepsFiltered = jobSteps.stream().filter(input -> input.getId().equals(stepId)).collect(Collectors.toList());
        return jobStepsFiltered.get(0);
    }


    public void mapData(){
        List<FooterRepository> footerInfoRepositoryList = footerInfoRepository.findAll();
        for(FooterRepository footerRepository:footerInfoRepositoryList) {
            String langLocal = footerRepository.getLang_locale();

            MasterRepository masterRepository = new MasterRepository();
            List<SocialRepository> socialRepositoryList = socialInfoRepository.getListByField("lang_local", langLocal);
            ArrayList<SocialLinks> socialLinks = new ArrayList<>();
            ArrayList<Footer> footerLinks = new ArrayList<>();
            if(socialRepositoryList != null){
                for (SocialRepository socialRepository1 : socialRepositoryList) {
                    SocialLinks socialLink = new SocialLinks();
                    socialLink.setHandleURL(socialRepository1.getSocial_link());
                    socialLink.setPlatform(socialRepository1.getSocial_type());
                    socialLink.setProduct(socialRepository1.getSocial_BU());
                    socialLinks.add(socialLink);
                }
                masterRepository.setSocialLinks(socialLinks);
            }
            String language = null;
            String country = null;
            if (langLocal.contains("_")) {
                String token[] = langLocal.split("_");
                language = langLocal.split("_")[0];
                country = langLocal.split("_")[token.length-1];
            }else{
                String token[] = langLocal.split("-");
                language = langLocal.split("-")[0];
                country = langLocal.split("-")[token.length-1];
            }


            Footer footer = new Footer();
            footer.setLanguage(language);
            footer.setOptOutLinkLabel(footerRepository.getOptOutLinkLable());
            footer.setOptoutLocale(footerRepository.getOptOutLocale());
            footer.setFooterHtml(footerRepository.getFooterhtml());
            footerLinks.add(footer);

            Mirror mirror = mirrorInfoRepository.getListByField("langLocal",langLocal);
            if(mirror == null){
                masterRepository.setMirrorText("Read online");
            }else{
                masterRepository.setMirrorText(mirror.getMirrorText());
            }

            masterRepository.setFooter(footerLinks);

            masterRepository.setAcomLocaleCode(langLocal);

            masterRepository.setCountryCode(country);
            try {
                masterRepository.setId(convertToMD5(langLocal));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            masterInfoRepository.insert(masterRepository);
        }
    }

    @Override
    public String fetchSocialLink(String locale, String bu, String linkType) {
        MasterRepository masterRepository = masterInfoRepository.getListByField(JobConstant.LOCALE, locale);
           List<SocialLinks> socialLinkList=null;

        if(!ObjectUtils.isEmpty(masterRepository)){
            List<SocialLinks> socialLinks = masterRepository.getSocialLinks();
           socialLinkList = socialLinks.parallelStream().filter(input -> (input.getPlatform().equalsIgnoreCase(linkType) && input.getProduct().equalsIgnoreCase(bu))).collect(Collectors.toList());
        }


          SocialLinks socialLink = null;
//        if(locale.equals("ar_SA")
//        && linkType.equals("Youtube")) {
//            System.out.println("here");
//        }
        if(socialLinkList != null && socialLinkList.size() > 0) {
            System.out.println("correct local, bu, linkType :: " + locale + " " + bu + linkType);
            socialLink = socialLinkList.get(0);
        } else {

            System.out.println("local, bu, linkType :: " + locale + " " + bu + linkType);
        }
        String socialLinkFromRepo  = null;
        if(socialLink != null) {
            socialLinkFromRepo = socialLink.getHandleURL();
        }
        return socialLinkFromRepo;
    }

    @Override
    public String fetchFooter(String locale) {
        MasterRepository masterRepository = masterInfoRepository.
                getListByField(JobConstant.LOCALE, locale);

        if(ObjectUtils.isEmpty(masterRepository)){
            return null;
        }
        List<Footer> footers = masterRepository.getFooter();
        return footers.get(0).getFooterHtml();
    }

    @Override
    public String fetchFooterTechMarketing(String locale) {
        MasterRepositoryTechMarketing masterRepositoryTechMarketing =
                masterInfoRepositoryTechMarketing.
                        getListByField(JobConstant.LOCALE, locale);
        if(ObjectUtils.isEmpty(masterRepositoryTechMarketing)){
            return null;
        }
        List<Footer> footers = masterRepositoryTechMarketing.getFooter();
        return footers.get(0).getFooterHtml();
    }


    @Override
    public String fetchMirror(String locale) {
        MasterRepository masterRepository = masterInfoRepository.getListByField(JobConstant.LOCALE, locale);

        if(ObjectUtils.isEmpty(masterRepository)){
            return null;
        }
        return masterRepository.getMirrorText();
    }

    @Override
    public String fetchOptOutLinkLable(String locale) {

        MasterRepository masterRepository = masterInfoRepository.
                getListByField(JobConstant.LOCALE, locale);
        if(ObjectUtils.isEmpty(masterRepository)){
            return null;
        }
        List<Footer> footers = masterRepository.getFooter();
        return footers.get(0).getOptOutLinkLabel();
    }


    public static String convertToMD5(String text) throws NoSuchAlgorithmException {

        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(text.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }

        return hashtext;
    }

    @Override
    public String fetchMirrorTechMarketing(String locale) {

        MasterRepositoryTechMarketing masterRepositoryTechMarketing =
                masterInfoRepositoryTechMarketing.getListByField(JobConstant.LOCALE, locale);

        if(ObjectUtils.isEmpty(masterRepositoryTechMarketing)){
            return null;
        }
        return masterRepositoryTechMarketing.getMirrorText();
    }

    @Override
    public String fetchSocialLinkTechMarketing(String locale, String bu, String linkType) {
        List<SocialLinks> socialLinkList = null;
        MasterRepositoryTechMarketing masterRepositoryTechMarketing =
                masterInfoRepositoryTechMarketing.getListByField(JobConstant.LOCALE, locale);

        if(!ObjectUtils.isEmpty(masterRepositoryTechMarketing)){
            List<SocialLinks> socialLinks = masterRepositoryTechMarketing.getSocialLinks();
            socialLinkList = socialLinks.parallelStream().filter(input -> (input.getPlatform().equalsIgnoreCase(linkType) && input.getProduct().equalsIgnoreCase(bu))).collect(Collectors.toList());
        }


        SocialLinks socialLink = null;
//        if(locale.equals("ar_SA")
//        && linkType.equals("Youtube")) {
//            System.out.println("here");
//        }
        if(socialLinkList != null && socialLinkList.size() > 0) {
            System.out.println("correct local, bu, linkType :: " + locale + " " + bu + linkType);
            socialLink = socialLinkList.get(0);
        } else {

            System.out.println("local, bu, linkType :: " + locale + " " + bu + linkType);
        }
        String socialLinkFromRepo  = null;
        if(socialLink != null) {
            socialLinkFromRepo = socialLink.getHandleURL();
        }
        return socialLinkFromRepo;
    }

}
