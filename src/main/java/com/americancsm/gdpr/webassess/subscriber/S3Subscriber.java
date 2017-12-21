package com.americancsm.gdpr.webassess.subscriber;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.americancsm.gdpr.webassess.model.GDPRQuickAssessmentBean;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class S3Subscriber implements Observer {
	
	private static final String S3_BUCKET_NAME = "com.americancsm.data";
	private Observable publisher;
	private AmazonS3 s3Client;
	private String s3BucketName;
	
	// TODO: Update to make use of temporary profiles
	// private String credentialsProviderProfile = null;
	
	private Context context;
	private final LambdaLogger LOGGER;
	
	public S3Subscriber() {
		super();
		this.context = AWSContextLocator.getInstance().getContext();
		LOGGER = this.context.getLogger();
		this.initialize(); 
	}

	@Override
	public String update(GDPRQuickAssessmentBean assessment) {
		String publishResult = "FAILED";
		try {
            LOGGER.log("Uploading a new object to S3 from a file\n");
            StringBuilder sb = new StringBuilder("marketing/GDPRQuickAssessment/");
            sb.append(context.getAwsRequestId());
            String keyName = sb.toString();
            ObjectMapper mapper = new ObjectMapper();
            String gdprAssessment = mapper.
            		writerWithDefaultPrettyPrinter().
            		writeValueAsString(assessment.getAssessmentInfo());
                    
            PutObjectResult putObjectResult = 
            		s3Client.putObject(new PutObjectRequest(s3BucketName, keyName, gdprAssessment));
            publishResult = putObjectResult.getETag();
            LOGGER.log("GDPR Quick Assessment sent to S3 Bucket: " + this.s3BucketName + 
				   " with ETag ID = " + publishResult);

         } catch (AmazonServiceException ase) {
            LOGGER.log("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            LOGGER.log("Error Message:    " + ase.getMessage());
            LOGGER.log("HTTP Status Code: " + ase.getStatusCode());
            LOGGER.log("AWS Error Code:   " + ase.getErrorCode());
            LOGGER.log("Error Type:       " + ase.getErrorType());
            LOGGER.log("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            LOGGER.log("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            LOGGER.log("Error Message: " + ace.getMessage());
        } catch (JsonProcessingException jpe) {
			LOGGER.log(jpe.getMessage());
		}
		return publishResult;
	}

	private void initialize() {
		this.s3BucketName = System.getenv(S3_BUCKET_NAME);
		s3Client = 
			AmazonS3ClientBuilder.standard()
				.withRegion(Regions.US_WEST_2)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                // .withCredentials(new ProfileCredentialsProvider(credentialsProviderProfile))
                .build();
	}

	@Override
	public void setObservable(Observable observable) {
		this.publisher = observable;
	}
	
	@Override
	public void unSubscribe() {
		publisher.removeObserver(this);
	}

}
