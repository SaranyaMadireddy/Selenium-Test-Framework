package com.orangehrm.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test(dataProvider = "employeeDetails", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameFromDB(String empId, String empName) throws InterruptedException {

		SoftAssert softAssert = getSoftAssert();
		ExtentManager.logStep("Logging with User Credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

		ExtentManager.logStep("Click on Pim Tab");
		homePage.clickOnPimTab();

		ExtentManager.logStep("Search for Employee");
		homePage.employeeSearch(empName);

		ExtentManager.logStep("Get the Employee Name from DB");

		Map<String, String> empdetails = DBConnection.getEmployeeDetails(empId);
		String emplFirstName = empdetails.get("firstName");
		String emplMiddleName = empdetails.get("middleName");
		String emplLastName = empdetails.get("lastName");

		String emplFirstAndMiddleName = (emplFirstName + " " + emplMiddleName).trim();

		ExtentManager.logStep("verify the employee first and middle name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName),
				"First and Middle name are not Matching");

		ExtentManager.logStep("verify the employee last name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName), "LastName is not Matching");

		softAssert.assertAll();
	}

}
