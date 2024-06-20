package com.adobe.core.raven.dto.cgen;

import com.adobe.core.raven.dto.qa.Metadata;
import lombok.Data;

import java.util.ArrayList;

public @Data
class CgenRepository {

    private String id;
    private ArrayList<Cgen> content;
    private String md5;
    private String type;
    private Metadata metadata;
}
