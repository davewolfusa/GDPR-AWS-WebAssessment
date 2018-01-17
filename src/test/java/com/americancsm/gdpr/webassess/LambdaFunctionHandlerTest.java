package com.americancsm.gdpr.webassess;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentRequest;
import com.americancsm.gdpr.webassess.model.GDPRAssessmentResponse;
import com.americancsm.gdpr.webassess.util.BuildTestObjects;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

    private static GDPRAssessmentRequest input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
		input = BuildTestObjects.buildTestObject1();
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("LambdaFunctionHandler");

        return ctx;
    }

    @Test
    public void testLambdaFunctionHandler() {
        LambdaFunctionHandler handler = new LambdaFunctionHandler();
        Context ctx = createContext();

        GDPRAssessmentResponse response = handler.handleRequest(input, ctx);
        
        // TODO: validate output here if needed.
        Assert.assertEquals(LambdaFunctionHandler.SUCCESS, response.getResult());
    }
}
