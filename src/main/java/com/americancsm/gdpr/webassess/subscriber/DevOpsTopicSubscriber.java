package com.americancsm.gdpr.webassess.subscriber;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class DevOpsTopicSubscriber extends AbstractSubscriber<ObserverResult<String>,String> 
	implements Observer<ObserverResult<String>,String> {

	private static final String DEVOPS_TOPIC_ARN = "DEVOPS_TOPIC_ARN";
	
	private AmazonSNS snsClient;
	private String devOpsTopicARN = null;
	
	public DevOpsTopicSubscriber() {
		super();
		this.initialize(); 
	}

	@Override
	public ObserverResult<String> update(ObserverResult<String> failedResult) {
		PublishResult publishResult = null;
		ObserverResult<String> result;
		
		// Publish to an SNS topic
		String msg = failedResult.toString();
		PublishRequest publishRequest = new PublishRequest(devOpsTopicARN, msg);
		publishResult = snsClient.publish(publishRequest);
		if (publishResult.getMessageId() != null) {
    		    LOGGER.log("GDPR Quick Assessment sent to SNS topic: " + devOpsTopicARN + 
    				   " with message ID = " + publishResult.getMessageId() + "\n");
            result = new ObserverResult<>(publishResult.getMessageId());
		} else {
    		    LOGGER.log("GDPR Quick Assessment failed attempt to send to SNS topic: " + devOpsTopicARN + "\n");
            result = new ObserverResult<>();
		}
		
		return result;
	}

	@Override
	protected void initialize() {
		
		super.initialize();
		
		devOpsTopicARN  = System.getenv(DEVOPS_TOPIC_ARN);
		if (this.devOpsTopicARN == null) {
			throw new IllegalStateException("Unable to get " + DEVOPS_TOPIC_ARN + " from the environment!");
		}
				
        // Build the SNS Client with the STS derived credentials
		snsClient = 
			AmazonSNSClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();
	}
}
