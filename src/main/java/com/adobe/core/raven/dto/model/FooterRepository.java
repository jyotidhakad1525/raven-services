package com.adobe.core.raven.dto.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

public @Data
class FooterRepository {

	private String _id;

	private String lang_locale;
	

	private String footerhtml;
	

	private Date dateCreated;
	

	private Date dateModified;
	

	private String optOutLocale;

	private String optOutLinkLable;
	


    
}
