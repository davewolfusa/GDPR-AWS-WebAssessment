package com.americancsm.gdpr.webassess.subscriber;

import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;

public interface Observer {

	String update(GDPRAssessmentRequest assessment);

	void setObservable(Observable observable);
	
	void unSubscribe();
}
