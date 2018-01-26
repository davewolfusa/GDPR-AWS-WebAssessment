package com.americancsm.gdpr.webassess;
	
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentResponse;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentValidator;
import com.americancsm.gdpr.webassess.subscriber.QuickAssessmentPublisher;
import com.americancsm.gdpr.webassess.subscriber.S3Subscriber;
import com.americancsm.gdpr.webassess.subscriber.TopicSubscriber;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LambdaFunctionHandler implements RequestHandler<GDPRAssessmentRequest, APIGatewayResponse> {
	
	protected LambdaLogger LOGGER = null;
	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";
	private QuickAssessmentPublisher publisher;
	private AWSContextLocator contextLocator;
	private static Object mutex = new Object();
	private static boolean isInitialized = false;

	@Override
	public APIGatewayResponse handleRequest(GDPRAssessmentRequest apiGatewayRequest, Context context) {
		
		LOGGER = context.getLogger();
		
		// Log Request Data
		ObjectMapper mapper = new ObjectMapper();
		String requestString = "";
		try {
			requestString = mapper.
				writerWithDefaultPrettyPrinter().
			    writeValueAsString(apiGatewayRequest);
		} catch (JsonProcessingException e) {
			LOGGER.log(e.getLocalizedMessage());
			e.printStackTrace();
		}
		LOGGER.log("Request: " + requestString);
		
		// Create Default (Error) Response Objects
		APIGatewayResponse apiResponse = new APIGatewayResponse();
		apiResponse.setStatusCode("400");
		GDPRAssessmentResponse response = new GDPRAssessmentResponse();
		response.setResult(FAILURE);
		apiResponse.setBody(response);

		// Lazily Initialize Function
		if (!isInitialized) {
        		contextLocator = AWSContextLocator.getInstance();
        		contextLocator.setContext(context);
			initialize();
		}
		contextLocator.setContext(context);
		
		// Extract AssessmentInfo
		GDPRAssessmentRequest assessmentRequest = apiGatewayRequest;
		
		// Validate GDPR Request
		GDPRAssessmentValidator validator = new GDPRAssessmentValidator();
		boolean isValid = validator.validate(assessmentRequest);
		
		if (isValid) {
			// Compute Complexity Value
			assessmentRequest.getAssessmentInfo().computeComplexityValue();
			
			// Publish to Subscribers (S3 & Topic)
			publisher.setAssessmentBean(assessmentRequest);
			
			// Set the response
			response.setResult(SUCCESS);
			response.setScore(assessmentRequest.getAssessmentInfo().getAcsmComplexityValue());
			apiResponse.setBody(response);
			apiResponse.setStatusCode("200");
		}
		
	    return apiResponse;
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
