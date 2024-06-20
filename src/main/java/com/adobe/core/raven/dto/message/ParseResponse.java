package com.adobe.core.raven.dto.message;

import lombok.Data;

public @Data class ParseResponse {

    private String htmlBody;
    private String senderAddress;
    private String senderName;
    private String subject;
}
