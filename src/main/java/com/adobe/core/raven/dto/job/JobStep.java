package com.adobe.core.raven.dto.job;

import lombok.Data;

public @Data class JobStep {

	private String id;
	private int sequence;
	private String name;
	private String type;
	private int required;
	private String state;
	
}
