package com.americancsm.gdpr.webassess.subscriber;

public interface Observable {

	void addObserver(Observer observer);
	
	Observer removeObserver(Observer observer);
	
	void notifyObservers();
}
