package com.americancsm.gdpr.webassess.model;

import lombok.Data;

public @Data class Country {
	
	private String id;
	private String name;
	private String continent;
	private Boolean isEUMember;

}
