package com.americancsm.gdpr.webassess.subscriber;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class S3Subscriber extends AbstractSubscriber<GDPRAssessmentRequest,String> implements Observer<GDPRAssessmentRequest,String> {
	
	private static final String S3_BUCKET_NAME = "S3_BUCKET_NAME";
    private static final String S3_BUCKET_PATH = "S3_BUCKET_PATH";
    
	private AmazonS3 s3Client;
	private String s3BucketName;
	private String s3BucketPath;
	
	public S3Subscriber() {
		super();
		this.initialize(); 
	}

	@Override
	public ObserverResult<String> update(GDPRAssessmentRequest assessment) {
		String publishProduct = null;
		ObserverResult<String> result = new ObserverResult<>();
		
		// Publish to a S3 Bucket
		try {
			// Convert the AssessmentInfo Java Object into JSON
            StringBuilder sb = new StringBuilder(this.s3BucketPath);
            sb.append(context.getAwsRequestId());
            String keyName = sb.toString();
            ObjectMapper mapper = new ObjectMapper();
            String gdprAssessment = mapper.
            		writerWithDefaultPrettyPrinter().
            		writeValueAsString(assessment.getAssessmentInfo());
                 
            // Write the JSON String to the S3 Bucket
            PutObjectResult putObjectResult = 
            		s3Client.putObject(s3BucketName, keyName, gdprAssessment);
            
            publishProduct = putObjectResult.getETag();
            LOGGER.log("GDPR Quick Assessment sent to S3 Bucket: " + this.s3BucketName + 
				   " with ETag ID = " + publishProduct + "\n");
            result = new ObserverResult<>(publishProduct);

         } catch (AmazonServiceException ase) {
            LOGGER.log("Caught an AmazonServiceException, which means your request made it " +
                       "to Amazon S3, but was rejected with an error response for some reason.\n");
            LOGGER.log("Error Message:    " + ase.getMessage()    + "\n");
            LOGGER.log("HTTP Status Code: " + ase.getStatusCode() + "\n");
            LOGGER.log("AWS Error Code:   " + ase.getErrorCode()  + "\n");
            LOGGER.log("Error Type:       " + ase.getErrorType().toString()  + "\n");
            LOGGER.log("Request ID:       " + ase.getRequestId()  + "\n");
            result = new ObserverResult<String>(ase.getErrorCode(), 
            		                                ase.getErrorType().toString(), 
            		                                ase.getErrorMessage(),
            								        ase.getRequestId());
        } catch (AmazonClientException ace) {
            LOGGER.log("Caught an AmazonClientException, which means the client encountered " +
                       "an internal error while trying to communicate with S3, " +
                       "such as not being able to access the network.\n");
            LOGGER.log("Error Message: " + ace.getMessage() + "\n");
            result = new ObserverResult<String>("", "", ace.getMessage(), "");
        } catch (JsonProcessingException jpe) {
            LOGGER.log("Error Message: " + jpe.getMessage() + "\n");
            result = new ObserverResult<String>("", "", jpe.getMessage(), "");
		}
		return result;
	}

	@Override
	protected void initialize() {
		
		super.initialize();
		
		this.s3BucketName = System.getenv(S3_BUCKET_NAME);
		if (this.s3BucketName == null) {
			throw new IllegalStateException("Unable to get " + S3_BUCKET_NAME + " from the environment!\n");
		}
		
		this.s3BucketPath = System.getenv(S3_BUCKET_PATH);
		if (this.s3BucketPath == null) {
			throw new IllegalStateException("Unable to get " + S3_BUCKET_PATH + " from the environment!\n");
		}
		
        // Build the SNS Client with the STS derived credentials
		s3Client = AmazonS3ClientBuilder.standard()
			.withRegion(Regions.US_WEST_2)
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .build();
	}

}
