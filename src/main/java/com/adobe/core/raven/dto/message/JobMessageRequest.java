package com.adobe.core.raven.dto.message;

import lombok.Data;

@Data
public class JobMessageRequest {

    private String name;
    private String base64;
}
