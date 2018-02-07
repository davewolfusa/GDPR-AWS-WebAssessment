package com.americancsm.gdpr.webassess.subscriber;

import java.util.ArrayList;
import java.util.List;

import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;

public class AssessmentPublisher implements Observable<GDPRAssessmentRequest,String> {
	
	private GDPRAssessmentRequest message;
	
	private ArrayList<Observer<GDPRAssessmentRequest,String>> observerList;
	
	public AssessmentPublisher() {
		observerList = new ArrayList<>();
	}

	@Override
	public void addObserver(Observer<GDPRAssessmentRequest,String> observer) {
		observerList.add(observer);
		observer.setObservable(this);
	}

	@Override
	public Observer<GDPRAssessmentRequest,String> removeObserver(Observer<GDPRAssessmentRequest,String> observer) {
		observer.setObservable(null);
		observerList.remove(observer);
		return observer;
	}

	@Override
	public List<ObserverResult<String>> notifyObservers() {
		ArrayList<ObserverResult<String>> resultList = new ArrayList<>();
		for (Observer<GDPRAssessmentRequest,String> observer : observerList) {
			ObserverResult<String> result = observer.update(message);
			resultList.add(result);
		}
		return resultList;
	}

	/**
	 * @return the assessmentBean
	 */
	public GDPRAssessmentRequest getMessage() {
		return message;
	}

	/**
	 * @param assessmentBean the assessmentBean to set
	 */
	public void setMessage(GDPRAssessmentRequest message) {
		this.message = message;
	}

}
