package com.adobe.core.raven.dto.workfront;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data
class WorkfrontResponse {


    private String reason;

    private String message;

    private ProjectInfo projectInfo;


}

