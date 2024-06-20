package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.service.interfaces.CgenService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CgenServiceImpl implements CgenService {

    public String fetchTagfullurl(String tagId, ArrayList<Cgen> cgen){
        List<Cgen> cgenFiltered = cgen.stream().filter(input -> input.getTagId().equals(tagId)).collect(Collectors.toList());
        String tagfullUrl = null;
        if(cgenFiltered != null && cgenFiltered.size() > 0) {
             tagfullUrl = cgenFiltered.get(0).getTagFullUrl();
        }
        return tagfullUrl;
    }

    public String fetchSubjectline(String activityId, ArrayList<Cgen> cgen){
        List<Cgen> cgenFiltered = cgen.stream().filter(input -> input.getActivityId().equals(activityId)).collect(Collectors.toList());
        String subjectLine = cgenFiltered.get(0).getSubjectLine();
        return subjectLine;
    }

    public String fetchPreHeader(String activityId, ArrayList<Cgen> cgen){
        List<Cgen> cgenFiltered = cgen.stream().filter(input -> input.getActivityId().equals(activityId)).collect(Collectors.toList());
        String preHeader = cgenFiltered.get(0).getPreHeader();
        return preHeader;
    }

    public int fetchCTALinkCount(String activityId, ArrayList<Cgen> cgen){
        List<Cgen> cgenFiltered = cgen.stream().filter(input -> input.getActivityId().equals(activityId)).collect(Collectors.toList());
        return cgenFiltered.size();
    }
}
