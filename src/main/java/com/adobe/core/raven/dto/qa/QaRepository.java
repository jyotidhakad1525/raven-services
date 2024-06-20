package com.adobe.core.raven.dto.qa;

import java.util.ArrayList;

import lombok.Data;

public @Data class QaRepository {

	private String id;
	private String assignee;
	private String cgenId;
	private ArrayList<QaItem> items;
	private Metadata metadata;
	
}
