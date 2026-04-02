package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void setupPages() {
		this.loginpage = new LoginPage(getDriver());
		this.homepage = new HomePage(getDriver());
	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
		System.out.println("Running testMethod1 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginpage.login(username, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin Tab should be visible after successful login");
		ExtentManager.logStep("Validation Successful");
		homepage.logout();
		ExtentManager.logStep("Logged out successfully");
		staticWait(2);
	}

	@Test(dataProvider = "invalidLoginData", dataProviderClass = DataProviders.class)
	public void invalidLoginTest(String username, String password) {
		System.out.println("Running testMethod2 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginpage.login(username, password);
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginpage.verifyErrorMessage(expectedErrorMessage), "Invalid Error Message");
		ExtentManager.logStep("Validation Successful");
		ExtentManager.logStep("Logged out successfully");
	}
}
