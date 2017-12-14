package com.americancsm.gdpr.webassess.model;

import java.io.Serializable;

import lombok.Data;

public @Data class GDPRQuickAssessmentBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LINE_END = "\n";
	
	private RequestorInfo requestor;
	private GDPRAssessmentInfo assessmentInfo;
	
	public String formatForDocument() {
		StringBuilder sb = new StringBuilder();
		sb.append(LINE_END);
		sb.append("GDPR Quick Assessment \n");
		sb.append(LINE_END);
		sb.append("Requested by:\n ");
		sb.append(requestor.formatForDocument());
		sb.append(LINE_END);
		sb.append("Assessment Information: \n ");
		sb.append(assessmentInfo.formatForDocument());
		
		return sb.toString();
	}
}
