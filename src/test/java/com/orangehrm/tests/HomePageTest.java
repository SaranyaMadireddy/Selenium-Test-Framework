package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void setupPages() {
		this.loginpage = new LoginPage(getDriver());
		this.homepage = new HomePage(getDriver());
	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMLogo(String username, String password) {
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginpage.login(username, password);
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homepage.verifyOrangeHRMLogo(), "Logo is not visible");
		ExtentManager.logStep("Validation Successful");
		ExtentManager.logStep("Logged out successfully");
	}
}
