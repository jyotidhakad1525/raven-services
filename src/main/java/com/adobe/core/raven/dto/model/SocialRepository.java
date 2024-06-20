package com.adobe.core.raven.dto.model;

import lombok.Data;

import javax.persistence.*;


public @Data class SocialRepository {

	private String _id;


	private String social_BU;


	private String lang_local;


	private String social_type;


	private String social_link;



}
