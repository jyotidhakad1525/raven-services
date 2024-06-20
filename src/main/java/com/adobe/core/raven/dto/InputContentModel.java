package com.adobe.core.raven.dto;

import com.adobe.core.raven.dto.cgen.Cgen;
import lombok.Data;

import java.util.ArrayList;

public @Data
class InputContentModel {

    ArrayList<String> msgFiles;
    ArrayList<Cgen> cgen;
    String workfrontID;
    String jobId;
    String bu;

}
