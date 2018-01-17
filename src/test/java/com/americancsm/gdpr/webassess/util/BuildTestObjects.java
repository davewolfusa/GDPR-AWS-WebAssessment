package com.americancsm.gdpr.webassess.util;

import static com.americancsm.gdpr.webassess.model.CountryEnum.*;
import static com.americancsm.gdpr.webassess.model.CertificationEnum.PCI;
import static com.americancsm.gdpr.webassess.model.CertificationEnum.ISO;
import static com.americancsm.gdpr.webassess.model.CertificationEnum.HIPAA;
import static com.americancsm.gdpr.webassess.model.IAASProviderEnum.*;
import java.util.Date;

import com.americancsm.gdpr.webassess.model.CertificationEnum;
import com.americancsm.gdpr.webassess.model.CountryEnum;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentInfo;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;
import com.americancsm.gdpr.webassess.model.IAASProviderEnum;
import com.americancsm.gdpr.webassess.model.RequestorInfo;

public class BuildTestObjects {
	
	static public GDPRAssessmentRequest buildTestObject1() {
		RequestorInfo requestInfo = new RequestorInfo();
		requestInfo.setFirstName("Dave");
		requestInfo.setLastName("Wolf");
		requestInfo.setTitle("Secure DevOps Consultant");
		requestInfo.setEmail("dave.wolf@americancsm.com");
		requestInfo.setPhone("(303) 956-9106");
		requestInfo.setSubmissionDate(new Date());
		requestInfo.setCompanyName("Metageek Inc.");
		requestInfo.setCompanyAddress("2356 Clermont St., Denver, CO 80207");
		
		GDPRAssessmentInfo assessmentInfo = new GDPRAssessmentInfo();
		assessmentInfo.setHqLocation(UNITED_STATES);
		assessmentInfo.setOfficeCount(1);
		assessmentInfo.setOfficeLocations(new CountryEnum[]{ UNITED_STATES });
		assessmentInfo.setEmployeeCount(1);
		assessmentInfo.setEmployeeLocations(new CountryEnum[]{ UNITED_STATES });
		assessmentInfo.setContractorCount(0);
		assessmentInfo.setContractorLocations(new CountryEnum[] {});
		assessmentInfo.setServicedCountriesCount(1);
		assessmentInfo.setProductTypeCount(3);
		assessmentInfo.setCustomerCount(1);
		assessmentInfo.setIaasProviderCount(1);
		assessmentInfo.setIaasProviders(new IAASProviderEnum[] { AMAZON_WEB_SERVICES });
		assessmentInfo.setIaasProviderLocations(new CountryEnum[] { UNITED_STATES});
		assessmentInfo.setIsPrivacyShieldCertified(false);
		assessmentInfo.setDataClassificationLevels(1);
		assessmentInfo.setCertifications(new CertificationEnum[] { PCI });
		assessmentInfo.setAcsmComplexityValue(1);
		
		GDPRAssessmentRequest bean = new GDPRAssessmentRequest();
		bean.setRequestor(requestInfo);
		bean.setAssessmentInfo(assessmentInfo);
		
		return bean;
	}
	
	static public GDPRAssessmentRequest buildTestObject2() {
		RequestorInfo requestInfo = new RequestorInfo();
		requestInfo.setFirstName("Carlin");
		requestInfo.setLastName("Dornbusch");
		requestInfo.setTitle("CEO");
		requestInfo.setEmail("carlin.dornbusch@americancsm.com");
		requestInfo.setPhone("(303) 956-9106");
		requestInfo.setSubmissionDate(new Date());
		requestInfo.setCompanyName("American Cyber Security Manageemnt Inc.");
		requestInfo.setCompanyAddress("2356 Clermont St., Denver, CO 80207");
		
		GDPRAssessmentInfo assessmentInfo = new GDPRAssessmentInfo();
		assessmentInfo.setHqLocation(UNITED_STATES);
		assessmentInfo.setOfficeCount(1);
		assessmentInfo.setOfficeLocations(new CountryEnum[]{ UNITED_STATES, UNITED_KINGDOM, JAPAN });
		assessmentInfo.setEmployeeCount(1);
		assessmentInfo.setEmployeeLocations(new CountryEnum[]{ UNITED_STATES, UNITED_KINGDOM, JAPAN,
			BRAZIL, MEXICO, COLUMBIA, ARGENTINA, CANADA, CHINA, INDIA, INDONESIA, PAKISTAN, BANGLADESH, 
			AUSTRIA, BELGIUM, BULGARIA, CROATIA, CYPRUS, CZECH_REPUBLIC, DENMARK, 
			RUSSIA, UKRAINE, BELARUS, SERBIA, SWITZERLAND, NORWAY, AUSTRAILIA, PAPUA_NEW_GUINEA, NEW_ZEALAND, 
            FIJI, SOLOMON_ISLANDS, FRENCH_POLYNESIA, NEW_CALEDONIA, VANUATU });
		assessmentInfo.setContractorCount(5);
		assessmentInfo.setContractorLocations(new CountryEnum[] {UNITED_STATES, UNITED_KINGDOM, JAPAN });
		assessmentInfo.setServicedCountriesCount(1);
		assessmentInfo.setProductTypeCount(3);
		assessmentInfo.setCustomerCount(1);
		assessmentInfo.setIaasProviderCount(1);
		assessmentInfo.setIaasProviders(new IAASProviderEnum[] { AMAZON_WEB_SERVICES, GOOGLE_CLOUD_PLATFORM });
		assessmentInfo.setIaasProviderLocations(new CountryEnum[] { UNITED_STATES, MEXICO, UNITED_KINGDOM, JAPAN });
		assessmentInfo.setIsPrivacyShieldCertified(true);
		assessmentInfo.setDataClassificationLevels(1);
		assessmentInfo.setCertifications(new CertificationEnum[] { PCI, HIPAA, ISO });
		assessmentInfo.setAcsmComplexityValue(1);
		
		GDPRAssessmentRequest bean = new GDPRAssessmentRequest();
		bean.setRequestor(requestInfo);
		bean.setAssessmentInfo(assessmentInfo);
		
		return bean;
	}

}
