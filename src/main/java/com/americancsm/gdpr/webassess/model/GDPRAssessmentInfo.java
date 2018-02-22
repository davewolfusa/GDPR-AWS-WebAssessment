package com.americancsm.gdpr.webassess.model;

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
	@NotNull
	private CountryEnum[] servicedCountries;
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
	
	private static final String COMMA = ", ";
	private static final String LINE_END = "\n";
	private static final String TAB = "\t";
	private static final String THERE_ARE = "There are ";
	private static final String LOCATIONS_ACROSS = " locations across the following countries: \n ";
	
	public String formatForDocument() {
		StringBuilder sb = new StringBuilder();
		sb.append(LINE_END);
		sb.append("HQ Location: " + this.hqLocation.getProperName() + LINE_END);
		
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
		int contractorLocationCount = (this.contractorCount > 0) ? this.contractorLocations.length : 0;
		sb.append("Contractors:\n");
		if (this.contractorCount > 0) {
			if (this.contractorCount > 1) {
        			sb.append(THERE_ARE + this.contractorCount + " contractors in " + contractorLocationCount + LOCATIONS_ACROSS);
			} else {
        			sb.append("There is " + this.contractorCount + " contractor in " + contractorLocationCount + LOCATIONS_ACROSS);
			}
        		sb.append(this.formatEnumArray(this.contractorLocations, true));
		} else {
        		sb.append(THERE_ARE + "no contractors.");
		}
		sb.append(LINE_END);
		
		if (this.servicedCountries.length > 1) {
        		sb.append(THERE_ARE + this.servicedCountries.length + " countries serviced in ");
        		sb.append(this.formatEnumArray(this.servicedCountries, true));
		} else {
        		sb.append("Serviced country: " + this.servicedCountries[0]);
		}
		sb.append("Number of Systems/Products/Services: " + this.productTypeCount + LINE_END);
		sb.append("Number of System User's (Subscribers, Consumers, etc.): " + this.customerCount + LINE_END);
		sb.append(LINE_END);
		
		// IAAS Locations
		int iaasProviderLocationCount = (this.iaasProviderCount > 0) ? this.iaasProviderLocations.length : 0;
		sb.append("IAAS:\n");
		if (this.iaasProviderCount > 0) {
        		sb.append("The following (" + this.iaasProviderCount + ") IAAS Providers: \n");
        		sb.append(this.formatEnumArray(this.iaasProviders, true));
        		sb.append("are found in (" + iaasProviderLocationCount + ")" + LOCATIONS_ACROSS);
        		sb.append(this.formatEnumArray(this.iaasProviderLocations, true));
		} else {
        		sb.append("There are no IAAS Providers. \n");
		}
		sb.append(LINE_END);
		
		// Certifications
		sb.append("Certifications:\n");
		sb.append("Is PrivacyShield Compliance in place? " + this.isPrivacyShieldCertified + LINE_END);
		sb.append("Compliance in the following certifications: \n");
		sb.append(this.formatEnumArray(this.certifications, false));
		sb.append(LINE_END);
		
		// ACSM Complexity Score
		sb.append("ACSM Complexity Score: " + this.acsmComplexityValue + "\n");
		
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
}
