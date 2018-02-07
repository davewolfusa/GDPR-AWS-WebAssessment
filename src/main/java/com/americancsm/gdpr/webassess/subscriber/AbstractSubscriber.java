package com.americancsm.gdpr.webassess.subscriber;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;

public abstract class AbstractSubscriber<X,Y> implements Observer<X,Y> {

	protected Observable<X,Y> publisher;
	protected Context context;
	protected final LambdaLogger LOGGER;
	
	public AbstractSubscriber() {
		super();
		this.context = AWSContextLocator.getInstance().getContext();
		LOGGER = this.context.getLogger();
	}

	protected void initialize() {
	}
	
	@Override
	public void setObservable(Observable<X,Y> observable) {
		this.publisher = observable;
	}
	
	@Override
	public void unSubscribe() {
		publisher.removeObserver(this);
	}
}
