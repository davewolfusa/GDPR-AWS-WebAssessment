package com.americancsm.gdpr.webassess.subscriber;

import java.util.ArrayList;

import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;

public class QuickAssessmentPublisher implements Observable {
	
	private GDPRAssessmentRequest assessmentBean;
	
	private ArrayList<Observer> observerList;
	
	public QuickAssessmentPublisher() {
		observerList = new ArrayList<>();
	}

	@Override
	public void addObserver(Observer observer) {
		observerList.add(observer);
		observer.setObservable(this);
	}

	@Override
	public Observer removeObserver(Observer observer) {
		observer.setObservable(null);
		observerList.remove(observer);
		return observer;
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : observerList) {
			observer.update(assessmentBean);
		}
	}

	/**
	 * @return the assessmentBean
	 */
	public GDPRAssessmentRequest getAssessmentBean() {
		return assessmentBean;
	}

	/**
	 * @param assessmentBean the assessmentBean to set
	 */
	public void setAssessmentBean(GDPRAssessmentRequest assessmentBean) {
		this.assessmentBean = assessmentBean;
		this.notifyObservers();
	}

}
