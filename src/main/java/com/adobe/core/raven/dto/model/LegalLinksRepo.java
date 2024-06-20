package com.adobe.core.raven.dto.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


public @Data class LegalLinksRepo {

	@Id
	private long id;
	

	private String lang_locale;
	

	private String privacy;
	

	private String terms;
	

	private String contact;
	

	private String unsublink;
	

	
    
}
