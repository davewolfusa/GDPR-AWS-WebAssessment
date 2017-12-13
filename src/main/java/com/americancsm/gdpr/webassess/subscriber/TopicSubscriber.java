package com.americancsm.gdpr.webassess.subscriber;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.americancsm.gdpr.webassess.model.GDPRQuickAssessmentBean;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;

public class TopicSubscriber implements Observer {

	private static final String TOPIC_ARN = "TOPIC_ARN";
	
	private Observable publisher;
	private AmazonSNS snsClient;
	private String topicARN = null;
	
	private Context context;
	private final LambdaLogger LOGGER;
	
	// TODO: Update to make use of temporary profiles
	// private String credentialsProviderProfile = null;
	
	public TopicSubscriber() {
		super();
		this.context = AWSContextLocator.getInstance().getContext();
		LOGGER = this.context.getLogger();
		this.initialize(); 
	}

	@Override
	public String update(GDPRQuickAssessmentBean assessment) {
		//publish to an SNS topic
		String msg = assessment.formatForDocument();
		PublishRequest publishRequest = new PublishRequest(topicARN, msg);
		PublishResult publishResult = snsClient.publish(publishRequest);
		LOGGER.log("GDPR Quick Assessment sent to SNS topic: " + topicARN + 
				   " with message ID = " + publishResult.getMessageId());
		return publishResult.getMessageId();
	}
	
	@Override
	public void setObservable(Observable observable) {
		this.publisher = observable;
	}
	
	private void initialize() {
		// TODO: Update to make use of temporary profiles
		topicARN  = System.getenv(TOPIC_ARN);
		snsClient = 
			AmazonSNSClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                // .withCredentials(new ProfileCredentialsProvider(credentialsProviderProfile))
                // .withCredentials(new ClasspathPropertiesFileCredentialsProvider())
                .build();
		// LOGGER.log("Exiting TopicSubscriber.initialize");
	}
	
	public void unsubscribe() {
		publisher.removeObserver(this);
	}
}
