package com.americancsm.gdpr.webassess.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Test;

import com.americancsm.gdpr.webassess.util.BuildTestObjects;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateTestFiles {
	
	@Test
	public void geneateTestFile1() {
		String testOutputDirectory = System.getProperty("testOutputDirectory");
		if (testOutputDirectory == null) {
			testOutputDirectory = "src/test/resources/";
		}
		String testFile1Path = testOutputDirectory + "testFile1.json";
		GDPRAssessmentRequest bean1 = BuildTestObjects.buildTestObject1();

		ObjectMapper mapper = new ObjectMapper();

	    try {  
	        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(testFile1Path), bean1);
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	@Test
	public void geneateTestFile2() {
		String testOutputDirectory = System.getProperty("testOutputDirectory");
		if (testOutputDirectory == null) {
			testOutputDirectory = "src/test/resources/";
		}
		String testFile1Path = testOutputDirectory + "testFile2.json";
		GDPRAssessmentRequest bean2 = BuildTestObjects.buildTestObject2();

		ObjectMapper mapper = new ObjectMapper();

	    try {  
	        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(testFile1Path), bean2);
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	@Test
	public void testConstructingAssessmentBean() {
		//read json file data to String
		byte[] jsonData = null;
		try {
			jsonData = Files.readAllBytes(Paths.get("src/test/resources/testFile1.json"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		
		//convert json string to object
		GDPRAssessmentRequest bean = null;
		try {
			bean = objectMapper.readValue(jsonData, GDPRAssessmentRequest.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("GDPRAssessmentRequest Object\n" + bean);
	}
	
	@Test
	public void geneateCountryEnumFile() {
		String testOutputDirectory = System.getProperty("testOutputDirectory");
		if (testOutputDirectory == null) {
			testOutputDirectory = "src/test/resources/";
		}
		String countriesPath = testOutputDirectory + "countries.json";
		ArrayList<Country> countryList = new ArrayList<Country>();
		for (CountryEnum enumItem : CountryEnum.values()) {
			Country country = new Country();
			country.setId(enumItem.toString());
			country.setName(enumItem.getProperName()); 
			country.setContinent(enumItem.getContinent().toString()); 
			country.setIsEUMember(enumItem.isEUMemberCountry());
			countryList.add(country);
		}
		
		Country[] countryArray = new Country[countryList.size()];
		countryArray = countryList.toArray(countryArray);

		ObjectMapper mapper = new ObjectMapper();

	    try {  
	        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(countriesPath), countryArray);
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}

}
