package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.ResponseError;
import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.cgen.CgenRepository;
import com.adobe.core.raven.dto.qa.QaRepository;
import com.adobe.core.raven.dto.response.JobResponse;
import com.adobe.core.raven.repository.MasterInfoRepository;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.URLService;
import com.adobe.core.raven.service.interfaces.ValidateService;
import com.adobe.core.raven.service.interfaces.WorkfrontService;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class ValidateServiceImpl implements ValidateService {

    @Autowired
    URLService urlService;

    @Autowired
    WorkfrontService workfrontService;

    @Autowired
    MasterInfoRepository masterInfoRepository;


    @Autowired
    DataService dataService;


    public Boolean validateImageSource(Element image) {
        String status = urlService.validate(image.attr("src"));
        String[] statusCode = status.split(QAConstant.UNDERSCORE);

        return statusCode[0].equals(QAConstant.VALID);

    }

    public Boolean validateImageAltText(Element image) {

        return !(image.attr("alt") == null || image.attr("alt").equals(""));

    }

    public Boolean compare(String val1, String val2) {
        return val1.equals(val2);

    }

    public void validateOptOutLink(String optOutLink, String optOutLinkFromRepo) {
        if (!optOutLink.equals(optOutLinkFromRepo)) {
            System.out.println("Subject Line does not match with Cgen");
        }

    }

    public Boolean validateFromAddress(String fromAddressFromMail, String workfrontId,
                                       String local, String stepId, String activityId) {


        String fromAddressFromWorkfront = workfrontService.fetchFromAddress(workfrontId);
        if (fromAddressFromWorkfront == null) {
            QaRepository qaRepository =dataService.getQaRepository(stepId);
            CgenRepository cgenRepository = dataService.getCgenRepository(qaRepository.getCgenId());
            if(cgenRepository == null){
                ResponseError responseError=new ResponseError();
                responseError.setErrorCode(101);
                responseError.setMessage(JobConstant.Cgen_Content_Not_Found);
                return false;
            }
            ArrayList<Cgen> cgen=cgenRepository.getContent();
            for(Cgen cgenI:cgen){
                if(cgenI.getActivityId().equals(activityId)){
                    fromAddressFromWorkfront=cgenI.getFromName();
                    if(fromAddressFromWorkfront == null){
                        ResponseError responseError=new ResponseError();
                        responseError.setErrorCode(101);
                        responseError.setMessage(JobConstant.From_Name_Address_is_Null);
                        return false;
                    }
                }
            }
            //fromAddressFromWorkfront =cgenRepository.getContent().get(0).getFromName();
            //TODO- check in cgen
        }
        Boolean isSame = false;
        String fromAddressWf = fromAddressFromWorkfront.split("<")[0].trim();
        String fromAddressM = fromAddressFromMail.split("<")[0].trim();
        // normal case
        if (!fromAddressFromWorkfront.contains("for")) {
            //String fromAddress = fromAddressFromWorkfront.split("<")[1].split(">")[0];

            return fromAddressWf.equals(fromAddressM);
        } else {
            // where from name contains for
            String fromName = dataService.getFromName(fromAddressFromWorkfront, local);
            if (fromName == null) {
                isSame = false;
            } else {

                //completeFromName = fromName + " <" + fromAddressFromWorkfront.split("<")[1];

                isSame = fromName.equals(fromAddressM);
            }
        }
        return isSame;
    }


}



