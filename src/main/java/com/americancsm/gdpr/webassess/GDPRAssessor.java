package com.americancsm.gdpr.webassess;

import static com.americancsm.gdpr.webassess.model.CertificationEnum.ISO;
import static com.americancsm.gdpr.webassess.model.CertificationEnum.NONE;

import java.util.HashSet;
import java.util.Set;

import com.americancsm.gdpr.webassess.model.CertificationEnum;
import com.americancsm.gdpr.webassess.model.CountryEnum;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentInfo;

import lombok.Data;

public @Data class GDPRAssessor {
	private static final int TOTAL_NUMBER_OF_EU_COUNTRIES = 27;
	private GDPRAssessmentInfo message;
	
	public GDPRAssessor(GDPRAssessmentInfo message) {
		this.message = message;
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
			EmployeeLevelEnum.computeRanking(message.getEmployeeCount() + message.getContractorCount()); 
		
		Integer officesInEUCountries     = numberOfEUCountries(message.getOfficeLocations());
		Integer employeesInEUCountries   = numberOfEUCountries(message.getEmployeeLocations());
		Integer contractorsInEUCountries = 
				message.getContractorCount() > 0 ? numberOfEUCountries(message.getContractorLocations()) : 0;
        	Integer servicedEUCountries      = numberOfEUCountries(message.getServicedCountries());
        	Integer iaasEUCountries          = 
        			message.getIaasProviderCount() > 0 ? numberOfEUCountries(message.getIaasProviderLocations()) : 0;
		
		Set<String> uniqueEUCountries = getSetOfEUCountries(message);
        	
		Integer countriesWeight      =
				(officesInEUCountries > 0 ? 1 : 0) +
				(employeesInEUCountries > 0 ? 1 : 0) +
				(contractorsInEUCountries > 0 ? 1 : 0) +
				(servicedEUCountries > 0 ? 1 : 0) +
				(iaasEUCountries > 0 ? 1 : 0);
		
		Double percentOfEUCountries =
		    countriesWeight > 0 ? 
		    	  ( ((double)uniqueEUCountries.size()) / ((double) TOTAL_NUMBER_OF_EU_COUNTRIES ))
		    	  : 0.0d;
		
		Double productCountRanking = 
			ProductLevelEnum.computeRanking(this.message.getProductTypeCount());
		
		Double customerCountRanking = 
			CustomerLevelEnum.computeRanking(this.message.getCustomerCount());
		
		Double acsmComplexityValue = 0d;
		
		if (percentOfEUCountries > 0) {
        		acsmComplexityValue = Math.ceil(
        			// Apply primary rankings
        			(Double) 
        			    percentOfEUCountries == 0d ?  0d : (
        				(
                			(productCountRanking  * 1d) +
                			(employeeCountRanking * 2d) +
                			(customerCountRanking * 2.5d) 
        				) / 5.5
        				* (message.getIsPrivacyShieldCertified() ? .9d : 1.0d)
        			    * (isISOPresent ? .8d : 1.0d)
        			)
        		);
        		acsmComplexityValue += (100.0d - acsmComplexityValue) * percentOfEUCountries;
        		acsmComplexityValue += (100.0d - acsmComplexityValue) * countriesWeight * 0.05d;
        		acsmComplexityValue += (100.0d - acsmComplexityValue) * message.getIaasProviderCount() * 
        				((message.getIaasProviderCount() > 1 ?.05d : 0.0d) + (message.getIaasProviderCount() - 1) * 0.25d);
        		if (certificationCount > 0) {
        			acsmComplexityValue -= acsmComplexityValue * message.getCertifications().length * .05d;
        		}
        		acsmComplexityValue -= acsmComplexityValue * message.getDataClassificationLevels() * .05d;
        		if (acsmComplexityValue > 100d) {
        			acsmComplexityValue = 100d;
        		}
		} else {
			acsmComplexityValue = 1d;
		}
		
		return acsmComplexityValue.intValue() ;
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
		if (message.getContractorCount() > 0) {
        		for (CountryEnum enumItem : message.getContractorLocations()) {
        			if (enumItem.isEUMemberCountry()) {
        				result.add(enumItem.name());
        			}
        		}
		}
		for (CountryEnum enumItem : message.getServicedCountries()) {
			if (enumItem.isEUMemberCountry()) {
				result.add(enumItem.name());
			}
		}
		if (message.getIaasProviderCount() > 0) {
        		for (CountryEnum enumItem : message.getIaasProviderLocations()) {
        			if (enumItem.isEUMemberCountry()) {
        				result.add(enumItem.name());
        			}
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
