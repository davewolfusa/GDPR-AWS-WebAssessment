package com.americancsm.gdpr.webassess;

import java.util.Properties;

import lombok.Data;

public @Data class APIGatewayRequest {
	
	private String resource;
	private String path;
	private String httpMethod;
	private Properties headers;
	private Properties queryStringParameters;
	private Properties pathParameters;
	private Properties stageVariables;
	private Properties requestContext;
	private String body;
	private boolean isBase64Encoded;
}
