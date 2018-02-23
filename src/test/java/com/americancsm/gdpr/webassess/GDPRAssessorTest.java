package com.americancsm.gdpr.webassess;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;

import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GDPRAssessorTest {

	private ClassLoader classLoader = getClass().getClassLoader();
	
	@Test
	public void testComputeComplexityValue1() throws Exception {
		byte[] jsonData = Files.readAllBytes(this.getFilePath("testFile1.json"));
		testFunction(jsonData);
	}

	@Test
	public void testComputeComplexityValue2() throws Exception {
		byte[] jsonData = Files.readAllBytes(this.getFilePath("testFile2.json"));
		testFunction(jsonData);
	}

	@Test
	public void testComputeComplexityValue3() throws Exception {
		byte[] jsonData = Files.readAllBytes(this.getFilePath("testFile3.json"));
		testFunction(jsonData);
	}

	@Test
	public void testComputeComplexityValue4() throws Exception {
		byte[] jsonData = Files.readAllBytes(this.getFilePath("testFile4.json"));
		testFunction(jsonData);
	}

	@Test
	public void testComputeComplexityValue5() throws Exception {
		byte[] jsonData = Files.readAllBytes(this.getFilePath("testFile5.json"));
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
	
	private Path getFilePath(String fileName) {
		File file = new File(classLoader.getResource(fileName).getFile());
		return file.toPath();
	}
}
