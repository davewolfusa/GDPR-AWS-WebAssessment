package com.americancsm.gdpr.webassess.subscriber;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;

public class TopicSubscriber extends AbstractSubscriber<GDPRAssessmentRequest,String> 
	implements Observer<GDPRAssessmentRequest,String> {

	private static final String TOPIC_ARN = "TOPIC_ARN";
	
	private AmazonSNS snsClient;
	private String topicARN = null;
	
	public TopicSubscriber() {
		super();
		this.initialize(); 
	}

	@Override
	public ObserverResult<String> update(GDPRAssessmentRequest assessment) {
		PublishResult publishResult = null;
		ObserverResult<String> result;
		
		// Publish to an SNS topic
		String msg = assessment.formatForDocument();
		PublishRequest publishRequest = new PublishRequest(topicARN, msg);
		publishResult = snsClient.publish(publishRequest);
		if (publishResult.getMessageId() != null) {
    		    LOGGER.log("GDPR Quick Assessment sent to SNS topic: " + topicARN + 
    				   " with message ID = " + publishResult.getMessageId() + "\n");
            result = new ObserverResult<>(publishResult.getMessageId());
		} else {
    		    LOGGER.log("GDPR Quick Assessment failed attempt to send to SNS topic: " + topicARN + "\n");
            result = new ObserverResult<>();
		}
		
		return result;
	}

	@Override
	protected void initialize() {
		
		super.initialize();
		
		topicARN  = System.getenv(TOPIC_ARN);
		if (this.topicARN == null) {
			throw new IllegalStateException("Unable to get " + TOPIC_ARN + " from the environment!");
		}
				
        // Build the SNS Client with the STS derived credentials
		snsClient = 
			AmazonSNSClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();
	}
}
