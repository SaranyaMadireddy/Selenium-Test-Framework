package com.orangehrm.tests;

import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyser;

import io.restassured.response.Response;

public class ApiTest {
	@Test
	public void verifyGetUserApi() {

		SoftAssert soft = new SoftAssert();

		// Step 1:Define API endpoint
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API endpoint" + endPoint);

		// Step 2:Send Get Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step 3: Validate Status Code
		ExtentManager.logStep("Validating API response status code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		soft.assertTrue(isStatusCodeValid, "Status code is not Expected");
		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status code validation Passed");
		} else {
			ExtentManager.logFaliureAPI("Status code validation Failed");
		}

		// Step 4: Validate Json response username
		ExtentManager.logStep("Validating response body for username");
		String username = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(username);
		soft.assertTrue(isUserNameValid, "Username is not valid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("username validation Passed");
		} else {
			ExtentManager.logFaliureAPI("username validation Failed");
		}

		// Step 5: Validate Json response email
		ExtentManager.logStep("Validating response body for email");
		String useremail = ApiUtility.getJsonValue(response, "email");
		boolean isEmailValid = "Sincere@april.biz".equals(useremail);
		soft.assertTrue(isEmailValid, "email is not valid");
		if (isEmailValid) {
			ExtentManager.logStepValidationForAPI("email validation Passed");
		} else {
			ExtentManager.logFaliureAPI("email validation Failed");
		}
		soft.assertAll();
	}

}
