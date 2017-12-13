package com.americancsm.gdpr.webassess.model;

import static com.americancsm.gdpr.webassess.model.CertificationEnum.ISO;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.apache.commons.text.WordUtils;

import lombok.Data;

public @Data class GDPRAssessmentInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String PHONE_REGEX = "\\D*([2-9]\\d{2})(\\D*)([2-9]\\d{2})(\\D*)(\\d{4})\\D*";
	
	@NotNull
	private CountryEnum hqLocation;
	@Positive
	private Integer officeCount;
	@NotNull
	private CountryEnum[] officeLocations;
	@Positive
	private Integer employeeCount;
	@NotNull
	private CountryEnum[] employeeLocations;
	@PositiveOrZero
	private Integer contractorCount;
	@NotNull
	private CountryEnum[] contractorLocations;
	@Positive
	private Integer servicedCountriesCount;
	@Positive
	private Integer productTypeCount;
	@Positive
	private Integer customerCount;
	@PositiveOrZero
	private Integer iaasProviderCount;
	@NotNull
	private IAASProviderEnum[] iaasProviders;
	@NotNull
	private CountryEnum[] iaasProviderLocations;
	@NotNull
	private Boolean isPrivacyShieldCertified;
	@NotNull
	private CertificationEnum[] certifications;
	@Positive @Min(value = 1) @Max(value = 10)
	private Integer dataClassificationLevels;
	@NotNull @Positive
	private Integer acsmComplexityValue;
	
	public void computeComplexityValue() {
		// Calculation=
		//     ((Emp+Contract)*Num countries serviced) + 
		//     (Num of Systems * (Num Users/100,000) * Num Iaas Providers) - 
		//     (2=Yes Privacy Shield OR 1=No) - (Num Compliance Certs) - (1=ISO)
		int isoPresenceValue = 0;
		for (CertificationEnum cert : this.certifications) {
			if (cert.equals(ISO)) {
				isoPresenceValue = 1;
				break;
			}
		}
		
		this.acsmComplexityValue = 
			((this.employeeCount + this.contractorCount) * this.servicedCountriesCount) +
			(this.productTypeCount * (this.customerCount/100000) * this.iaasProviderCount) -
			((this.isPrivacyShieldCertified ? 2 : 1) - this.certifications.length - isoPresenceValue);
	}
	
	private static final String COMMA = ", ";
	private static final String LINE_END = "\n";
	private static final String TAB = "\t";
	private static final String THERE_ARE = "There are ";
	private static final String LOCATIONS_ACROSS = " locations across the following countries: \n ";
	
	public String formatForDocument() {
		StringBuilder sb = new StringBuilder();
		sb.append(LINE_END);
		sb.append("HQ Location: " + this.hqLocation.ProperName() + LINE_END);
		
		sb.append(LINE_END);
		// Office Locations
		sb.append("Offices:\n");
		sb.append(THERE_ARE + this.officeCount + " offices in " + this.officeLocations.length + LOCATIONS_ACROSS);
		sb.append(this.formatEnumArray(this.officeLocations, true));
		sb.append(LINE_END);
		
		// Employee Locations
		sb.append("Employees:\n");
		sb.append(THERE_ARE + this.employeeCount + " employees in " + this.employeeLocations.length + LOCATIONS_ACROSS);
		sb.append(this.formatEnumArray(this.employeeLocations, true));
		sb.append(LINE_END);
		
		// Contractor Locations
		sb.append("Contractors:\n");
		sb.append(THERE_ARE + this.contractorCount + " contractors in " + this.contractorLocations.length + LOCATIONS_ACROSS);
		sb.append(this.formatEnumArray(this.contractorLocations, true));
		sb.append(LINE_END);
		
		sb.append("Number of Countries Serviced: " + this.servicedCountriesCount + LINE_END);
		sb.append("Number of Systems/Products/Services: " + this.productTypeCount + LINE_END);
		sb.append("Number of System User's (Subscribers, Consumers, etc.): " + this.customerCount + LINE_END);
		sb.append(LINE_END);
		
		// IAAS Locations
		sb.append("IAAS:\n");
		sb.append("The following (" + this.iaasProviderCount + ") IAAS Providers: \n");
		sb.append(this.formatEnumArray(this.iaasProviders, true));
		sb.append("are found in (" + this.iaasProviderLocations.length + ")" + LOCATIONS_ACROSS);
		sb.append(this.formatEnumArray(this.iaasProviderLocations, true));
		sb.append(LINE_END);
		
		// Certifications
		sb.append("Certifications:\n");
		sb.append("Is PrivacyShield Compliance in place? " + this.isPrivacyShieldCertified + LINE_END);
		sb.append("Compliance in the following certifications: \n");
		sb.append(this.formatEnumArray(this.certifications, false));
		sb.append(LINE_END);
		
		return sb.toString();
	}
	
	private <E extends Enum<E>> String formatEnumArray(E[] enumArray, boolean fullyCapitalize){
		StringBuilder sb = new StringBuilder(TAB);
		if (enumArray != null) {
    			for (E enumInstance : enumArray) {
    				sb.append(enumInstance.name());
    				if (enumInstance.equals(enumArray[enumArray.length -1])) {
    					sb.append(LINE_END);
    				} else {
    					sb.append(COMMA);
    				}
        		}
		} else {
			sb.append("None");
		}
		String result;
		if (fullyCapitalize) {
    	        result = WordUtils.capitalizeFully(sb.toString().replaceAll("_", " "));
		} else {
    	        result = WordUtils.capitalize(sb.toString().replaceAll("_", " "));
		}
		return result;
	}
	/*
	private static final String INTERNAL_USE = "Internal Policy=All data destroyed upon use.";
	private static final String DISCLAIMER = "Disclaimer\n" + 
			"This GDPR Quick Assessment is intended to assist organizations with assessing their GDPR compliance " +
			"complexity.  This GDPR Quick Assessment is provided for general public informational purposes only. " +
			"Any results, scoring or recommendations produced by the GDPR Quick Assessment should not be relied " + 
			"upon to determine how GDPR applies to an organization or an organization’s compliance with GDPR, and " +
			"they do not constitute legal advice, certifications or guarantees regarding GDPR compliance.  " +
			"Instead, we hope the GDPR Detailed Assessment identifies technologies and additional steps that " + 
			"organizations can implement to simplify their GDPR compliance efforts.  The application of GDPR is " +
			"highly fact-specific. We encourage all organizations using this GDPR Quick Assessment to work with a " +
			"legally qualified professional to discuss GDPR, how it applies specifically to their organization, " + 
			"and how best to ensure compliance.\n" + 
			"AMERICAN CYBER SECURITY MANAGEMENT MAKES NO WARRANTIES, EXPRESS, IMPLIED, OR STATUTORY, AS TO THE " +
			"INFORMATION IN THIS GDPR DETAILED ASSESSMENT. American CyberSecurity Management disclaims any " + 
			"conditions, express or implied, or other terms that use of the  products or services will ensure " + 
			"the organization’s compliance with the GDPR.  This GDPR Quick Assessment is provided \"as-is\".  " +
			"Information and recommendations expressed in this GDPR Detailed Assessment may change without notice.\n" + 
			"This GDPR Detailed Assessment does not provide the user with any legal rights to any intellectual " +
			"property in any American Cyber Security Management product or service.  Use of the tool is for " +
			"internal, reference purposes only; however, American Cyber Security Management partners may " +
			"distribute the GDPR Quick Assessment to their customers for such customers’ internal, reference " +
			"purposes only. Any distribution of the GDPR Quick Assessment by a American Cyber Security Management " +
			"partner to its customers must include terms consistent with those set forth in this disclaimer.   \n" + 
			"© 2017 American Cyber Security Management.  All rights reserved ";
	*/
}
