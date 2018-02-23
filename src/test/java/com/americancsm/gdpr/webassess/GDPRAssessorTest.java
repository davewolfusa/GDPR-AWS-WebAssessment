package com.americancsm.gdpr.webassess;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GDPRAssessorTest {
	private String testDirectory = null;

	@Before
	public void setUp() throws Exception {
		testDirectory = System.getProperty("testOutputDirectory");
		if (testDirectory == null) {
			testDirectory = "src/test/resources/";
		}
	}

	@Test
	public void testComputeComplexityValue1() throws Exception {
	    String testFilePath = testDirectory + "testFile1.json";
		byte[] jsonData = Files.readAllBytes(Paths.get(testFilePath));
		testFunction(jsonData);
	}

	@Test
	public void testComputeComplexityValue2() throws Exception {
		String testFilePath = testDirectory + "testFile2.json";
		byte[] jsonData = Files.readAllBytes(Paths.get(testFilePath));
		testFunction(jsonData);
	}

	@Test
	public void testComputeComplexityValue3() throws Exception {
		String testFilePath = testDirectory + "testFile3.json";
		byte[] jsonData = Files.readAllBytes(Paths.get(testFilePath));
		testFunction(jsonData);
	}

	@Test
	public void testComputeComplexityValue4() throws Exception {
		String testFilePath = testDirectory + "testFile4.json";
		byte[] jsonData = Files.readAllBytes(Paths.get(testFilePath));
		testFunction(jsonData);
	}

	@Test
	public void testComputeComplexityValue5() throws Exception {
		String testFilePath = testDirectory + "testFile5.json";
		byte[] jsonData = Files.readAllBytes(Paths.get(testFilePath));
		testFunction(jsonData);
	}
	
	private void testFunction(byte[] jsonData) {
		
		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
				
		//convert json string to object
		GDPRAssessmentRequest gdprRequest = null;
		try {
			gdprRequest = objectMapper.readValue(jsonData, GDPRAssessmentRequest.class);
		} catch (IOException e) {
			fail();
		}
		
		assertNotNull(gdprRequest);
    		GDPRAssessor assessor = new GDPRAssessor(gdprRequest.getAssessmentInfo());
    		Integer result = assessor.computeComplexityValue();
    		assertNotNull(result);
    		assertTrue(result > 0 && result <= 100);
		
	}

}
