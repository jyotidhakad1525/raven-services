package com.adobe.core.raven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public @Data
class EmailComponent {

    private String subjectLine;
    private String locale;
    private String activityId;
    private String creativeFileName;
    private String fromEmail;
    private String fromName;
    private Date sentDate;
    private String bu;
}

