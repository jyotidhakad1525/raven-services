package com.adobe.core.raven.dto.job;

import java.util.ArrayList;

import com.adobe.core.raven.dto.qa.Metadata;
import lombok.Data;
import org.springframework.data.annotation.Id;

public @Data class MasterJob {

	@Id
	private String id;
	private String name;
	private String workfrontId;
	private ArrayList<JobStep> steps;
	private Metadata masterJobMetadata;
	private String state;
	private String lifecycleMarket;
	
}
