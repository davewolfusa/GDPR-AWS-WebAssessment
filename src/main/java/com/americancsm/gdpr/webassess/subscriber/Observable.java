package com.americancsm.gdpr.webassess.subscriber;

import java.util.List;

public interface Observable<X,Y> {

	void addObserver(Observer<X,Y> observer);
	
	Observer<X,Y> removeObserver(Observer<X,Y> observer);
	
	List<ObserverResult<Y>> notifyObservers();
}
