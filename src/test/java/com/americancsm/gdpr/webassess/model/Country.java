package com.americancsm.gdpr.webassess.model;

import lombok.Data;

public @Data class Country {
	
	private String key;
	private String value;
	private String continent;
	private Boolean isEUMember;

}
