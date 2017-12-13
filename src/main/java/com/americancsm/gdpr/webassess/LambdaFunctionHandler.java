package com.americancsm.gdpr.webassess;

	
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.americancsm.gdpr.webassess.model.GDPRQuickAssessmentBean;
import com.americancsm.gdpr.webassess.model.GDPRQuickAssessmentValidator;
import com.americancsm.gdpr.webassess.subscriber.QuickAssessmentPublisher;
import com.americancsm.gdpr.webassess.subscriber.TopicSubscriber;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;

public class LambdaFunctionHandler implements RequestHandler<GDPRQuickAssessmentBean, String> {
	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";

	@Override
	public String handleRequest(GDPRQuickAssessmentBean assessmentBean, Context context) {
		String result = FAILURE;
		AWSContextLocator contextLocator = AWSContextLocator.getInstance();
		contextLocator.setContext(context);
		
		// Validate GDPR Request
		GDPRQuickAssessmentValidator validator = new GDPRQuickAssessmentValidator();
		boolean isValid = validator.validate(assessmentBean);
		
		if (isValid) {
			// Construct Publish/Subscribe objects
			QuickAssessmentPublisher publisher = new QuickAssessmentPublisher();
			// Add Observer / Subscriber objects to Observable / Publisher.
			TopicSubscriber topicSubscriber = new TopicSubscriber();
			topicSubscriber.setObservable(publisher);
			publisher.addObserver(topicSubscriber);
			
			// Update Observable / Publisher state
			publisher.setAssessmentBean(assessmentBean);
			result = SUCCESS;
		}
		
	    return result;
	}
}
