package com.adobe.core.raven.service.interfaces;


import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.cgen.CgenRepository;
import com.adobe.core.raven.dto.job.JobStep;
import com.adobe.core.raven.dto.message.MessageRepository;
import com.adobe.core.raven.dto.qa.QaRepository;
import com.adobe.core.raven.dto.qa.QaItem;
import com.adobe.core.raven.dto.qa.ReshareQaRepository;

import java.util.ArrayList;
import java.util.HashMap;

public interface DataService {

     HashMap<String,String> fetchHtmlBody(String jobId, String stepId);

     ArrayList<Cgen> fetchCgen(String jobId, String stepId);

     String fetchWorkfrontId(String jobId);

     String fetchSenderAddress(String stepId);

    String fetchSubjectLine(String stepId);

    String fetchActivityId(String stepId);
    QaRepository getQaRepository(String stepId);

    QaItem fetchQAItem(String stepId, String messageId);

    String fetchLocale(String stepId);

    void mapData();

    String fetchSocialLink(String locale, String bu, String linkType);

    String fetchFooter(String locale);

    String fetchFooterTechMarketing(String locale);

    String fetchMirror(String locale);

    String fetchOptOutLinkLable(String locale);

    String fetchBu(String stepId, String messageId);

    String fetchSegment(String stepId, String messageId);

    MessageRepository fetchMessageByMd5(String md5);

    MessageRepository insertMessage(MessageRepository messageRepository);

    String getFromName(String fromAddressFromWorkfront, String local);

    JobStep fetchJobStep(String jobId, String stepId);


    ReshareQaRepository insertMessage(ReshareQaRepository messageRepository);

    ReshareQaRepository getReshareQARepository(String reshareQaId);

    QaRepository getQAQARepository(String stepId);

    CgenRepository getCgenRepository(String id);

    MessageRepository getMessageRepository(String id);

    String fetchMirrorTechMarketing(String locale);

    String fetchSocialLinkTechMarketing(String locale, String bu, String linkType);
}
