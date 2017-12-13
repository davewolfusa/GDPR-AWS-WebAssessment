package com.americancsm.gdpr.webassess.subscriber;

import com.americancsm.gdpr.webassess.model.GDPRQuickAssessmentBean;

public interface Observer {

	String update(GDPRQuickAssessmentBean assessment);

	void setObservable(Observable observable);
}
