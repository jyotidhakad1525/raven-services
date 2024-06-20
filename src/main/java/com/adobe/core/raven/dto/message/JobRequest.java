package com.adobe.core.raven.dto.message;

import lombok.Data;

import java.util.ArrayList;

public @Data
class JobRequest {

    private String userId;
    private String jobId;
 //   private WorkfrontRepository workfrontInfo;
  //  private ArrayList<CgenContent> cgen;
    private ArrayList<JobMessageRequest> messages;



    private ArrayList<String> messagesIds;


   // private ArrayList<UserInfo> qas;
   // private ArrayList<JobSegmentInfo> segmentInfos;
    //private String jobType;

  //  private int type; // 1 - update msg files for QA-1, QA-2 etc.
  //  private String qaId;

    //private String qaStatus;

   // private String base64;
}
