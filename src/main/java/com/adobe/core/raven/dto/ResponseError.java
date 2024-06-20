package com.adobe.core.raven.dto;


import lombok.Data;

public @Data
class ResponseError {

    private int errorCode;
    private String message;
}
