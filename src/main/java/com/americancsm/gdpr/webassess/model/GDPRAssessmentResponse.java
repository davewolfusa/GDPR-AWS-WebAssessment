package com.americancsm.gdpr.webassess.model;

import java.io.Serializable;

import lombok.Data;

public @Data class GDPRAssessmentResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String result;
	private Float score;

}
