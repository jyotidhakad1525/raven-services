package com.adobe.core.raven.dto.repo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Id;

public @Data class Mirror {

    private String langLocal;

    private String mirrorText;



}
