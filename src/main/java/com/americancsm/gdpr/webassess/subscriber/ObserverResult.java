package com.americancsm.gdpr.webassess.subscriber;

import static com.americancsm.gdpr.webassess.model.StatusEnum.FAILURE;
import static com.americancsm.gdpr.webassess.model.StatusEnum.SUCCESS;

import com.americancsm.gdpr.webassess.model.StatusEnum;

import lombok.Data;

public @Data class ObserverResult<T> {
	
	private T product;
	private boolean hasProduct = false;
	private StatusEnum status;
	private String awsErrorCode = null;
	private String awsErrorType = null;
	private String errorMessage = null;
	private String requestId = null;
	
	public ObserverResult() {
		this.product = null;
		this.hasProduct = false;
		this.status  = FAILURE;
	}
	
	public ObserverResult(T product) {
		this.product = product;
		this.hasProduct = true;
		this.status  = SUCCESS;
	}
	
	public ObserverResult(StatusEnum status) {
		this.product = null;
		this.hasProduct = false;
		this.status  = status;
	}
	
	public ObserverResult(String errorCode, String errorType, String errorMessage, String requestId) {
		this.product = null;
		this.hasProduct = false;
		this.status  = FAILURE;
		this.awsErrorCode = errorCode;
		this.awsErrorType = errorType;
		this.errorMessage = errorMessage;
		this.requestId = requestId;
	}
}
