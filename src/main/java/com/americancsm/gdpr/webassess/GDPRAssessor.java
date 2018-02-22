package com.americancsm.gdpr.webassess;

import static com.americancsm.gdpr.webassess.model.CertificationEnum.ISO;
import static com.americancsm.gdpr.webassess.model.CertificationEnum.NONE;

import java.util.HashSet;
import java.util.Set;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.americancsm.gdpr.webassess.model.CertificationEnum;
import com.americancsm.gdpr.webassess.model.CountryEnum;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentInfo;
import com.americancsm.gdpr.webassess.util.AWSContextLocator;

import lombok.Data;

public @Data class GDPRAssessor {
	private static final int TOTAL_NUMBER_OF_EU_COUNTRIES = 27;
	private GDPRAssessmentInfo message;
	private LambdaLogger logger;
	
	public GDPRAssessor(GDPRAssessmentInfo message) {
		this.message = message;
		AWSContextLocator locator = AWSContextLocator.getInstance();
		logger = locator.getContext().getLogger();
	}

	public Integer computeComplexityValue() {
		boolean isISOPresent = false;
		int certificationCount = 0;
		if (this.message.getCertifications() != null) {
        		for (CertificationEnum cert : this.message.getCertifications()) {
        			if (cert.equals(NONE)) {
        				certificationCount = 0;
        				isISOPresent = false;
        				break;
        			}
        			certificationCount++;
        			if (cert.equals(ISO)) {
        				isISOPresent = true;
        			}
        		}
		}
		
		Double employeeCountRanking = 
			EmployeeLevelEnum.computeRanking(this.message.getEmployeeCount() + 
						                     this.message.getContractorCount()); 
		logger.log("Employee Count Ranking: " + employeeCountRanking + "\n");
		
		Integer officesInEUCountries     = numberOfEUCountries(message.getOfficeLocations());
		logger.log("EU Offices: " + officesInEUCountries + "\n");
		Integer employeesInEUCountries   = numberOfEUCountries(message.getEmployeeLocations());
		logger.log("EU Employees: " + employeesInEUCountries + "\n");
		Integer contractorsInEUCountries = numberOfEUCountries(message.getContractorLocations());
		logger.log("EU Contractors: " + contractorsInEUCountries + "\n");
        	Integer servicedEUCountries      = numberOfEUCountries(message.getServicedCountries());
		logger.log("EU serviced countries: " + servicedEUCountries + "\n");
        	Integer iaasEUCountries          = numberOfEUCountries(message.getIaasProviderLocations());
		logger.log("EU IAAS Provider countries: " + iaasEUCountries + "\n");
		
		Set<String> uniqueEUCountries = getSetOfEUCountries(message);
        	
		Integer countriesWeight      =
				(officesInEUCountries > 0 ? 1 : 0) +
				(employeesInEUCountries > 0 ? 1 : 0) +
				(contractorsInEUCountries > 0 ? 1 : 0) +
				(servicedEUCountries > 0 ? 1 : 0) +
				(iaasEUCountries > 0 ? 1 : 0);
		logger.log("EU countries weight: " + countriesWeight + "\n");
		
		Double totalEUCountries = 
		    			(Double) ((
            		    		officesInEUCountries +
            		    		employeesInEUCountries +
            		    		contractorsInEUCountries +
            		    		servicedEUCountries +
            		    		iaasEUCountries
            		    	) * 1.0d);
		logger.log("Total EU countries: " + totalEUCountries + "\n");
		
		Double percentOfEUCountries =
		    countriesWeight > 0 ? 
		    	  ( ((double)uniqueEUCountries.size()) / ((double) TOTAL_NUMBER_OF_EU_COUNTRIES ))
		    	  : 0.0d;
		logger.log("Percent of EU countries: " + percentOfEUCountries + "\n");
		
		Double productCountRanking = 
			ProductLevelEnum.computeRanking(this.message.getProductTypeCount());
		logger.log("Product Type Count Ranking: " + productCountRanking + "\n");
		
		Double customerCountRanking = 
			CustomerLevelEnum.computeRanking(this.message.getCustomerCount());
		logger.log("Customer Count Ranking: " + customerCountRanking + "\n");
		
		Double acsmComplexityValue = Math.ceil(
			// Apply primary rankings
			(Double) (
				(
        				(employeeCountRanking * 2.0d) +
        				(productCountRanking  * 1.00) +
        				(customerCountRanking * 4.0d) 
				) / 7
			)
			* (message.getIsPrivacyShieldCertified() ? .9d : 1.0d)
			* (isISOPresent ? .8d : 1.0d)
			);
		acsmComplexityValue += (100.0d - acsmComplexityValue) * percentOfEUCountries;
		acsmComplexityValue += (100.0d - acsmComplexityValue) * countriesWeight * 0.03d;
		if (!message.getCertifications()[0].equals(NONE)) {
			acsmComplexityValue -= acsmComplexityValue * message.getCertifications().length * .05d;
		}
		acsmComplexityValue -= acsmComplexityValue * message.getDataClassificationLevels() * .02d;
		acsmComplexityValue += (100.0d - acsmComplexityValue) * message.getIaasProviderCount() * 
				((message.getIaasProviderCount() > 1 ?.05d : 0.0d) + 0.02d);
		
		logger.log("ACSM Complexity Value: " + acsmComplexityValue.intValue() + "\n");
		return acsmComplexityValue < 1.0 ? 1 : acsmComplexityValue.intValue() ;
	}
	
	private Set<String> getSetOfEUCountries(GDPRAssessmentInfo message) {
		Set<String> result = new HashSet<>();
		for (CountryEnum enumItem : message.getOfficeLocations()) {
			if (enumItem.isEUMemberCountry()) {
				result.add(enumItem.name());
			}
		}
		for (CountryEnum enumItem : message.getEmployeeLocations()) {
			if (enumItem.isEUMemberCountry()) {
				result.add(enumItem.name());
			}
		}
		for (CountryEnum enumItem : message.getContractorLocations()) {
			if (enumItem.isEUMemberCountry()) {
				result.add(enumItem.name());
			}
		}
		for (CountryEnum enumItem : message.getServicedCountries()) {
			if (enumItem.isEUMemberCountry()) {
				result.add(enumItem.name());
			}
		}
		for (CountryEnum enumItem : message.getIaasProviderLocations()) {
			if (enumItem.isEUMemberCountry()) {
				result.add(enumItem.name());
			}
		}
		
		return result;
	}
	
	private int numberOfEUCountries(CountryEnum[] countries) {
		int countEUCountries = 0;
		if (countries != null) {
        		for (CountryEnum enumItem : countries) {
        			if (enumItem.isEUMemberCountry()) {
        				countEUCountries++;
        			}
        		}
		}
		return countEUCountries;
	}
}
