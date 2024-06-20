package com.adobe.core.raven.dto.repo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public @Data class FromNameInfo {

    @Id
    private String _id;
    private ArrayList<FromInfo> fromInfos;
}
