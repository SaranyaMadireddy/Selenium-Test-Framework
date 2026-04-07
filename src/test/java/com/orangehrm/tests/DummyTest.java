package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyTest extends BaseClass {

	@Test
	public void DummyTestCase() {
		ExtentManager.logStep("Verifying the title");
		Assert.assertEquals(getDriver().getTitle(), "OrangeHRM", "TestFailed -Title Mismatch");
		System.out.println("Test Passed -Title is matching");
	throw new SkipException("Skipping the test as part of Testing");

	}

}
