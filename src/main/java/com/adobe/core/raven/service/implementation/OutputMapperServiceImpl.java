package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.JobConstant;
import com.adobe.core.raven.dto.qa.*;
import com.adobe.core.raven.repository.QAInfoRepository;
import com.adobe.core.raven.repository.ReshareQAInfoRepository;
import com.adobe.core.raven.service.interfaces.DataService;
import com.adobe.core.raven.service.interfaces.OutputMapperService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class OutputMapperServiceImpl implements OutputMapperService {

    @Autowired
    DataService dataService;

    @Autowired
    QAInfoRepository qaInfoRepository;

    @Autowired
    ReshareQAInfoRepository reshareQAInfoRepository;

    public void mapValidationOutput(String label, String type, String name, String state, ArrayList<Link> links, String stepId, String messageId) {

        QaRepository qaRepository = dataService.getQaRepository(stepId);
        QaItem qaItem = dataService.fetchQAItem(stepId, messageId);
        CheckList checkList = new CheckList();
        checkList.setLabel(label);
        checkList.setData(links);
        checkList.setType(type);
        checkList.setName(name);
        checkList.setState(state);

        synchronized (this){
       /* Boolean checkListPresent = false;
        ArrayList<CheckList> checkLists = qaItem.getCheckList();
        if (checkLists == null) {
            checkLists = new ArrayList<>();
        }
        for (CheckList checkList1 : checkLists) {
            if (checkList1.getType().equals(type)) {
                checkListPresent = true;
            }
        }
        if (!checkListPresent) {
            checkLists.add(checkList);
            checkListPresent = false;
        }

        //qaItem.setCheckList(checkLists);

        ArrayList<QaItem> qaItems = qaRepository.getItems();

        for (QaItem qaItem1 : qaItems) {
            if (qaItem1.getId().equals(messageId)) {
                qaItem1.setCheckList(checkLists);
            }
        }
        qaRepository.setItems(qaItems);
        qaRepository.setAssignee(qaRepository.getAssignee());
        qaRepository.setCgenId(qaRepository.getCgenId());
        qaRepository.setId(stepId);
        qaRepository.setMetadata(qaRepository.getMetadata());*/

        //qaInfoRepository.insert(qaRepository);
        qaInfoRepository.updateCheckList(stepId,messageId,checkList);


    }
    }

    public void mapValidationOutput(String label, String type, String name, String state, ArrayList<Link> links, String stepId, String messageId,String retest) {

        ReshareQaRepository reshareQaRepository = dataService.getReshareQARepository(messageId);
        CheckList checkList = new CheckList();
        checkList.setLabel(label);
        checkList.setData(links);
        checkList.setType(type);
        checkList.setName(name);
        checkList.setState(state);
        synchronized (this){
            reshareQAInfoRepository.updateCheckList(stepId,messageId,checkList);
        }
    }


}
