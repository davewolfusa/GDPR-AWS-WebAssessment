package com.americancsm.gdpr.webassess;

import java.io.Serializable;
import java.util.Properties;

import lombok.Data;

@SuppressWarnings("serial")
public @Data class APIGatewayResponse implements Serializable {
	    private boolean isBase64Encoded = false;
	    private String statusCode = "200";
	    private Properties headers;
	    private String body;
}
