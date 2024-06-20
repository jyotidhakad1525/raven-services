package com.adobe.core.raven.dto;

import com.adobe.core.raven.dto.qa.CheckList;
import com.adobe.core.raven.dto.qa.QaItem;
import lombok.Data;

import java.util.ArrayList;

public @Data
class MsgValidationModel {

    private String activityId;

    private String locale;

    private String segment;

    private String subjectLine;

    private String status;

    private ArrayList<CheckList> checkList;

}
