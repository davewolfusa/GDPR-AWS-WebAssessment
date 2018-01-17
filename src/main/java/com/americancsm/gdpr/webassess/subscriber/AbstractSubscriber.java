package com.americancsm.gdpr.webassess.subscriber;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;

public abstract class AbstractSubscriber implements Observer {

	protected Observable publisher;
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
	public void setObservable(Observable observable) {
		this.publisher = observable;
	}
	
	@Override
	public void unSubscribe() {
		publisher.removeObserver(this);
	}
}
