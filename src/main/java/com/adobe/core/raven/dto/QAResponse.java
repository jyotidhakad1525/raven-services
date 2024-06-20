package com.adobe.core.raven.dto;

import lombok.Data;

import java.util.ArrayList;

public @Data class QAResponse {

  ArrayList<MsgValidationModel> msgValidationModel;

  String finalStatus;

}