package com.americancsm.gdpr.webassess.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;

public class GDPRAssessmentValidator {
	
	private Context context;
	private Validator validator;
	private Set<ConstraintViolation<GDPRAssessmentRequest>> violations;

	public GDPRAssessmentValidator() {
		super();
		AWSContextLocator locator = AWSContextLocator.getInstance();
		this.context = locator.getContext();
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	public boolean validate(GDPRAssessmentRequest quickAssessmentBean) {
		LambdaLogger LOGGER = context.getLogger();
		
		violations = validator.validate(quickAssessmentBean);
		
		if (violations.size() > 0) {
			LOGGER.log("\nFound " + violations.size() + " validation errors!\n");
	
        		for (ConstraintViolation<GDPRAssessmentRequest> violation : violations) {
        			context.getLogger().log(	"\t" + violation.getMessage());
        		}
		}
		return violations.isEmpty();
	}
	
	public Set<ConstraintViolation<GDPRAssessmentRequest>> getViolations() {
		return violations;
	}

}

