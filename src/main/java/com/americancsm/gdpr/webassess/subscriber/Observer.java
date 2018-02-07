package com.americancsm.gdpr.webassess.subscriber;

public interface Observer<X,Y> {

	ObserverResult<Y> update(X message);

	void setObservable(Observable<X,Y> observable);
	
	void unSubscribe();
}
