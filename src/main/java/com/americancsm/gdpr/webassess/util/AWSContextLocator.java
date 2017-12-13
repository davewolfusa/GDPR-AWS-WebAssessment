package com.americancsm.gdpr.webassess.util;

import com.amazonaws.services.lambda.runtime.Context;

public final class AWSContextLocator {
	
	private static volatile AWSContextLocator instance = null;
	private static Object mutex = new Object();
	
	private Context context;
	
	private AWSContextLocator() {
		super();
	}
	
	public static AWSContextLocator getInstance() {
		AWSContextLocator result = instance;
		if (result == null) {
			synchronized (mutex) {
				result = instance;
				if (result == null) {
					instance = result = new AWSContextLocator();
				}
			}
		}
	    return result;
	}
	
	public Context getContext() {
		return this.context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

}
