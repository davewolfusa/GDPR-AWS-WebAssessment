package com.americancsm.gdpr.webassess;
	
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.americancsm.gdpr.webassess.model.GDPRQuickAssessmentBean;
import com.americancsm.gdpr.webassess.model.GDPRQuickAssessmentValidator;
import com.americancsm.gdpr.webassess.subscriber.QuickAssessmentPublisher;
import com.americancsm.gdpr.webassess.subscriber.S3Subscriber;
import com.americancsm.gdpr.webassess.subscriber.TopicSubscriber;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;

public class LambdaFunctionHandler implements RequestHandler<GDPRQuickAssessmentBean, String> {
	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";
	private QuickAssessmentPublisher publisher;
	private AWSContextLocator contextLocator;
	private static Object mutex = new Object();
	private static boolean isInitialized = false;

	@Override
	public String handleRequest(GDPRQuickAssessmentBean assessmentBean, Context context) {
		
		String result = FAILURE;

		// Lazily Initialize Function
		if (!isInitialized) {
        		contextLocator = AWSContextLocator.getInstance();
        		contextLocator.setContext(context);
			initialize();
		}
		contextLocator.setContext(context);
		
		// Validate GDPR Request
		GDPRQuickAssessmentValidator validator = new GDPRQuickAssessmentValidator();
		boolean isValid = validator.validate(assessmentBean);
		
		if (isValid) {
			
			// Update state
			publisher.setAssessmentBean(assessmentBean);
			result = SUCCESS;
		}
		
	    return result;
	}
	
	private void initialize() {
			synchronized (mutex) {
			if (!isInitialized) {
                	
            		// Construct Publish/Subscribe objects
            		publisher = new QuickAssessmentPublisher();
            		// Add Observer / Subscriber objects to Observable / Publisher.
                		
            		// Topic Subscriber
            		TopicSubscriber topicSubscriber = new TopicSubscriber();
            		topicSubscriber.setObservable(publisher);
            		publisher.addObserver(topicSubscriber);
                		
            		// S3 Bucket Subscriber
            		S3Subscriber s3Subscriber = new S3Subscriber();
            		s3Subscriber.setObservable(publisher);
            		publisher.addObserver(s3Subscriber);
            		isInitialized = true;
			}
		}
	}
}
