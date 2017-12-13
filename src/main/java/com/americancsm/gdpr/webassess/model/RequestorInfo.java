package com.americancsm.gdpr.webassess.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

public @Data class RequestorInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String PHONE_REGEX = "\\D*([2-9]\\d{2})(\\D*)([2-9]\\d{2})(\\D*)(\\d{4})\\D*";
	public static final String OUTPUT_DATE_FORMAT = "EEEEE MMMMM dd, yyyy HH:mm:ss Z";
	
	@NotEmpty @NotNull @Size(min=2, max=50)
	private String firstName;
	@NotEmpty @NotNull @Size(min=2, max=50)
	private String lastName;
	@NotEmpty @NotNull @Size(min=2, max=50)
	private String title;
	@NotNull @Email 
	private String email;
	@NotNull @Pattern(regexp = PHONE_REGEX)
	private String phone;
	@NotNull // @PastOrPresent
	private Date   submissionDate;
	@NotEmpty @NotNull @Size(min=2, max=60)
	private String companyName;
	@NotEmpty @NotNull @Size(min=2, max=60)
	private String companyAddress;
	
	private static final String COMMA = ", ";
	private static final String LINE_END = "\n";
	
	public String formatForDocument() {
		StringBuilder sb = new StringBuilder();
		sb.append(LINE_END);
		sb.append("\t" + this.firstName + " " + this.lastName + COMMA + this.title + LINE_END);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
		TimeZone timeZone = TimeZone.getTimeZone("America/Denver");
		simpleDateFormat.setTimeZone(timeZone);
		sb.append("\ton " + simpleDateFormat.format(this.submissionDate) + LINE_END);
		
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(PHONE_REGEX);
		Matcher matcher = pattern.matcher(this.phone);
		sb.append("\tEmail: " + this.email + ", Phone: " + matcher.replaceFirst("($1) $3-$5") + LINE_END);
		sb.append("\tCompany Address: " + this.companyAddress + LINE_END);
		
		return sb.toString();
	}
}
